package com.yyy.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

public class CreateTOPIC_ARTICLE {
	public static String desTableName = "TOPIC_ARTICLE";
	public static String srcTableName = "ARTICLE_TOPIC";
	public static String[] desFamily = { "article" };

	public static void main(String[] args) throws Exception {
		HBaseDAO.deleteTable(desTableName);
		HBaseDAO.createTable(desTableName, desFamily);
		CreateTOPIC_ARTICLE c = new CreateTOPIC_ARTICLE();
		List<Put> lPuts = c.queryAndGeneratePuts(srcTableName);
		HBaseDAO.putAll(desTableName, lPuts);

	}

	/**
	 * query data from source table, and generate puts for creating new table
	 */
	public List<Put> queryAndGeneratePuts(String sourceTableName) throws IOException {
		List<Result> lResults = HBaseDAO.scanColumnByFilter(sourceTableName, "topic", null);

		List<Put> lPuts = new ArrayList<>();

		/**
		 * each result contains a rowkey with all familys and values.
		 */
		for (Result result : lResults) {
			CellScanner cScanner = result.cellScanner();
			while (cScanner.advance()) {
				Cell c = cScanner.current();
				String articleId = new String(CellUtil.cloneRow(c));
				String topicId = new String(CellUtil.cloneQualifier(c));
				String prob = new String(CellUtil.cloneValue(c));
				topicId = topicId.length() < 2 ? "0" + topicId : topicId;
				Put put = new Put(topicId.getBytes());
				put.addColumn("article".getBytes(), articleId.getBytes(), prob.getBytes());
				lPuts.add(put);
			}
		}
		return lPuts;
	}

}
