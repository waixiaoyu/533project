package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.HBaseDAO;
import com.yyy.model.WordID;

import edu.stanford.nlp.Tagging;

public class WordsProbsFromTopic {

	private static final String TABLE_NAME = "TOPIC_WORD";

	public static void main(String[] args) throws IOException {
		Tagging t = new Tagging();
		String sent = "Showers continued throughout the week in the Bahia cocoa zone.";
		List<String> l = t.searchByTag(sent, "NN");
		List<WordTopicProb> wtps = new LinkedList<WordTopicProb>();
		for (String str : l) {
			wtps.add(new WordTopicProb(str));
		}
		WordsProbsFromTopic wpft = new WordsProbsFromTopic();
		wpft.getHighestProbInTopic(wtps);
		System.out.println(wtps);
	}

	private static class WordTopicProb {
		private WordID wi;
		private String topicId;
		private double prob = 0;

		public WordTopicProb(String word) throws IOException {
			super();
			this.wi = new WordID(word);
		}

		@Override
		public String toString() {
			return "WordTopicProb [wi=" + wi + ", topicId=" + topicId + ", prob=" + prob + "]";
		}

	}

	public void getHighestProbInTopic(List<WordTopicProb> wtps) throws IOException {
		List<Result> lResults = HBaseDAO.scanColumnByFilter(TABLE_NAME, "word", null);
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
					if (wtp.wi.getId().equals(wordId)) {
						if (wtp.prob < prob) {
							wtp.prob = prob;
							wtp.topicId = topicId;
						}
						break;
					}
				}
			}
		}
	}
}
