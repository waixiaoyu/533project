package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.Iterator;
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
		wtps.add(new WordTopicProb("Showers"));
		WordsProbsFromTopic wpft = new WordsProbsFromTopic();
		wpft.getHighestProbInTopic(wtps);
		System.out.println(l);
	}

	private static class WordTopicProb {
		private WordID wi;
		private String topicId;
		private double prob = 0;

		// public WordTopicProb(WordID wi, String topicId, double prob) {
		// super();
		// this.wi = wi;
		// this.topicId = topicId;
		// this.prob = prob;
		// }
		//
		// public WordTopicProb(WordID wi) {
		// super();
		// this.wi = wi;
		// }

		public WordTopicProb(String word) throws IOException {
			super();
			this.wi = new WordID(word);
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
				System.out.println(new String(CellUtil.cloneRow(c)));
				System.out.println(new String(CellUtil.cloneQualifier(c)));
				System.out.println(new String(CellUtil.cloneValue(c)));
			}
		}
	}
}
