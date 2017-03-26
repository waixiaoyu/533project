package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.HBaseDAO;
import com.yyy.model.WordTopicProb;

import edu.stanford.nlp.Tagging;

/**
 * in this class, we can use Wtps (corresponding topic id) to get article id
 * from TABLE "TOPIC_ARTICLE". The number of articles must be too many, and the
 * IDs are stored in a list
 * 
 * @author Yayu Yao
 *
 */
public class FindRelatedArticleId {

	private static final String TABLE_NAME = "TOPIC_ARTICLE";

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
		f.searchArticleId(wtps);
	}

	private List<Cell> queryArticleByTopicRowKey(String rowKey) throws IOException {
		return HBaseDAO.getCellsByRowKey(TABLE_NAME, rowKey);
	}

	/**
	 * from Result, and get article ids as a String split by ",". For example,
	 * if the article ids are 13212 and 23221, the string can be 13212,23221.
	 * 
	 */
	public String searchArticleId(List<WordTopicProb> wtps) throws IOException {
		for (WordTopicProb wordTopicProb : wtps) {
			Result r = queryArticleByTopicRowKey(wordTopicProb.getTopicId());
			System.out.println(r.getColumnCells("article".getBytes(), "".getBytes()));
		}
		return null;
	}

	/**
	 * fill up List<WordTopicProb>
	 * 
	 */
	public void getHighestProbInTopic(List<WordTopicProb> wtps) throws IOException {
		List<Result> lResults = HBaseDAO.scanColumnByFilter("TOPIC_WORD", "word", null);
		/**
		 * each result contains a rowkey with all familys and values.
		 */
		for (Result result : lResults) {
			CellScanner cScanner = result.cellScanner();
			while (cScanner.advance()) {
				/**
				 * each cell contains a column, like: row:19, column=word:34837,
				 * timestamp=1490467209306, value=0.014274839791157254
				 */
				Cell c = cScanner.current();
				String topicId = new String(CellUtil.cloneRow(c));
				String wordId = new String(CellUtil.cloneQualifier(c));
				double prob = Double.parseDouble(new String(CellUtil.cloneValue(c)));
				for (WordTopicProb wtp : wtps) {
					if (wtp.getWi().getId().equals(wordId)) {
						if (wtp.getProb() < prob) {
							wtp.setProb(prob);
							wtp.setTopicId(topicId);
						}
						break;
					}
				}
			}
		}
	}
}
