package com.yyy.fuzzsearch;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

	}

	private List<Cell> queryArticleByTopicRowKey(String rowKey) throws IOException {
		return HBaseDAO.getCellsByRowKey(TABLE_NAME, rowKey);
	}

	/**
	 * from Result, and get article ids, stored in a set, which can not contain
	 * repeated elements
	 */
	public Set<String> searchArticleId(List<WordTopicProb> wtps) throws IOException {
		Set<String> set = new HashSet<>();
		for (WordTopicProb wordTopicProb : wtps) {
			List<Cell> lCells = queryArticleByTopicRowKey(wordTopicProb.getTopicId());
			for (Cell cell : lCells) {
				set.add(new String(CellUtil.cloneQualifier(cell)));
			}
		}
		return set;
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
