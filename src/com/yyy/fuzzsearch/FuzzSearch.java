package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import com.mechaglot_Alpha2.controller.Calculate;
import com.yyy.hbase.HBaseDAO;
import com.yyy.model.ArticleMeasure;
import com.yyy.model.WordTopicProb;

import edu.stanford.nlp.Tagging;

public class FuzzSearch {

	private static final String ARTICLE_TABLE_NAME = "ARTICLE_ALIAS_TITLE_CONTENT";
	private static final String SEPARATOR_DOT = "\\.";
	// use a table in this class to avoid get table repeatly
	private static Table table;

	private static Calculate cc = new Calculate();

	static {
		try {
			table = HBaseDAO.createTable(ARTICLE_TABLE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		FindRelatedArticleId f = new FindRelatedArticleId();
		Tagging t = new Tagging();
		String sent = "Showers continued throughout the week in the Bahia cocoa zone.";
		List<String> l = t.searchByTag(sent, "NN");
		List<WordTopicProb> wtps = new LinkedList<WordTopicProb>();
		for (String str : l) {
			wtps.add(new WordTopicProb(str));
		}
		f.getHighestProbInTopic(wtps);

		Iterator<WordTopicProb> it = wtps.iterator();
		while (it.hasNext()) {
			WordTopicProb wordTopicProb = (WordTopicProb) it.next();
			if (StringUtils.isEmpty(wordTopicProb.getTopicId())) {
				it.remove();
			}
		}
		System.out.println(wtps);
		Set<String> setId = f.searchArticleId(wtps);
		System.out.println(setId);

		FuzzSearch fs = new FuzzSearch();
		List<ArticleMeasure> lAms = new ArrayList<>();

		for (String id : setId) {
			ArticleMeasure am = fs.getArticleAMById(id);
			if (am != null) {
				lAms.add(fs.matchArticle(sent, am));
			}
			System.out.println(id);
		}
		// sort by max score
		lAms.sort(new Comparator<ArticleMeasure>() {
			@Override
			public int compare(ArticleMeasure o1, ArticleMeasure o2) {
				return (int) Math.signum(o2.getMaxScore() - o1.getMaxScore());
			}
		});
		return;
	}

	@Deprecated
	public String getArticleContentById(String rowKey) throws IOException {
		Cell cell = HBaseDAO.getCells(ARTICLE_TABLE_NAME, rowKey, "article", "content");
		return new String(CellUtil.cloneValue(cell));
	}

	/**
	 * using article id to create a AM, and return
	 * 
	 * @param rowKey
	 * @return
	 * @throws IOException
	 */
	private ArticleMeasure getArticleAMById(String rowKey) throws IOException {

		Get get = new Get(rowKey.getBytes());
		get.addFamily("article".getBytes());
		Result result = table.get(get);
		CellScanner cScanner = result.cellScanner();
		List<Cell> cells = HBaseDAO.cellScannerToCellList(cScanner);
		String content = null;
		String title = null;
		for (Cell cell : cells) {
			String str = new String(CellUtil.cloneQualifier(cell));
			if (str.equals("title")) {
				title = new String(CellUtil.cloneValue(cell));
			} else if (str.equals("content")) {
				content = new String(CellUtil.cloneValue(cell));
			}
		}
		return StringUtils.isEmpty(content) ? null : new ArticleMeasure(rowKey, content, title);
	}

	/**
	 * use source sentence to match target sentence, and return a score
	 */
	private double computeSentenceScore(String srcSen, String tarSen) {
		double dScore = 0;
		dScore = cc.calculateRBFSimilarity(srcSen, tarSen);
		return dScore;
	}

	/**
	 * use source sentence to match target article, and return scores sentence
	 * by sentence, which store in AM
	 */
	private ArticleMeasure matchArticle(String srcSen, ArticleMeasure am) {
		String[] sentences = am.getContent().trim().split(SEPARATOR_DOT);
		for (String string : sentences) {
			am.addScore(computeSentenceScore(srcSen, string.trim()));
			am.addSentence(string);
		}
		return am;
	}
}
