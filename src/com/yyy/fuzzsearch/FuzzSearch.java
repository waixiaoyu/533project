package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.HBaseDAO;
import com.yyy.model.WordTopicProb;

import edu.stanford.nlp.Tagging;

public class FuzzSearch {

	private static final String ARTICLE_TABLE_NAME = "ARTICLE_ALIAS_TITLE_CONTENT";

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
		Set<String> set = f.searchArticleId(wtps);
		System.out.println(set);
	}

	public String getArticleContentById(String rowKey) throws IOException {
		Result r = HBaseDAO.get(ARTICLE_TABLE_NAME, rowKey);

		return null;
	}

}
