package com.yyy.json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.HBaseDAO;
import com.yyy.model.WordProb;

public class TOPIC_WORD100tsv {

	public static String tableName = "TOPIC_WORD";
	private static String filepath = "";

	public static void main(String[] args) throws IOException {

		String rowKey = "1";
		filepath = "topic_" + rowKey + "_word100" + ".tsv";
		TOPIC_WORD100tsv w = new TOPIC_WORD100tsv();
		w.scanHbase(rowKey);
	}

	/**
	 * input=rowkey, output=100 words probs
	 * 
	 * @param rowKey
	 * @throws IOException
	 */
	private void scanHbase(String rowKey) throws IOException {
		Result result = HBaseDAO.get(tableName, rowKey);
		NavigableMap<byte[], byte[]> navigableMap = result.getFamilyMap("word".getBytes());
		Set<Entry<byte[], byte[]>> set = navigableMap.entrySet();
		List<WordProb> listWP = new ArrayList<WordProb>();
		for (Entry<byte[], byte[]> entry : set) {
			result = HBaseDAO.get("ID_WORD", new String(entry.getKey()));
			listWP.add(
					new WordProb(new String(result.getValue("word".getBytes(), null)), new String(entry.getValue())));
		}
		write(listWP, filepath);
	}

	/**
	 * use to write tsv
	 * 
	 */

	private void write(List<WordProb> arrayList, String filePath) {
		if (!filePath.equals("")) {
			try {
				FileOutputStream fos = null;
				PrintWriter writer = null;
				fos = new FileOutputStream(filePath);
				writer = new PrintWriter(new OutputStreamWriter(fos, "utf-8"));
				writer.write("word" + "\t" + "prob");
				for (WordProb wordProb : arrayList) {
					writer.write("\n");
					writer.write(wordProb.getWord() + "\t" + wordProb.getProb());
				}
				writer.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("output file path is null");
		}
	}

}
