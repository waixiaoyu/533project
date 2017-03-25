package com.yyy.model;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.HBaseDAO;

public class WordID {

	// table name
	private static final String WORD_ID = "WORD_ID";
	private static final String ID_WORD = "ID_WORD";

	private String word;
	private String id;

	// given word, and search its id from WORD_ID in hbase
	public WordID(String word) throws IOException {
		super();
		this.word = word.toLowerCase();
		this.id = getDataFromHbase(WORD_ID, "id", this.word);

	}

	// given id, and search its word from WORD_ID in hbase
	public WordID(int id) throws IOException {
		super();
		this.id = String.valueOf(id);
		this.id = getDataFromHbase(ID_WORD, "word", this.id);
	}

	private String getDataFromHbase(String tableName, String familyName, String searchWord) throws IOException {
		Result r = HBaseDAO.get(tableName, searchWord);
		Cell c = r.getColumnLatestCell(familyName.getBytes(), "".getBytes());
		return new String(CellUtil.cloneValue(c));
	}

	public String getWord() {
		return word;
	}

	public String getId() {
		return id;
	}

	public static void main(String[] args) throws IOException {
		Result r = HBaseDAO.get("WORD_ID", "apple");
		Cell c = r.getColumnLatestCell("id".getBytes(), "".getBytes());
		System.out.println(new String(CellUtil.cloneValue(c)));
	}
}
