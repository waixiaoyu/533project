package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;

import com.yyy.hbase.KeyValuePairs.Pair;
import com.yyy.mahout.Seqdumper;

public class CreateARTICLE_TOPIC_FUZZ {

	private static String path = "D:\\mahout-work-ubuntu\\reuters-lda-topics\\";
	private static String[] inputArgs = { "-i", path + "part-m-00000", "-o", path + "lad-topics.txt" };
	private static final double THRESHOLD = 0.9;

	public static String PATH = path + File.separator + "lad-topics.txt";
	public static String[] family = { "topic" };
	public static String tableName = "ARTICLE_TOPIC_FUZZ";

	public static void main(String[] args) throws Exception {
		Seqdumper.run(inputArgs);
		HBaseDAO.deleteTable(tableName);
		HBaseDAO.createTable(tableName, family);
		// HBaseDAO.put(tableName, "zweig", family[0], "41805");

		CreateARTICLE_TOPIC_FUZZ w = new CreateARTICLE_TOPIC_FUZZ();
		w.readTxtAndImport(PATH);
	}

	private void readTxtAndImport(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				for (int i = 0; i < 2; i++) {
					bufferedReader.readLine();
				}
				List<Put> lPuts = new ArrayList<>();

				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] strs = lineTxt.split("Key")[1].trim().split("Value");
					if (strs.length == 2) {
						String key = strs[0].replaceAll(":", "").trim();
						String value = strs[1].substring(strs[1].indexOf("{") + 1, strs[1].indexOf("}"));
						KeyValuePairs keyValuePairs = new KeyValuePairs(value.split(","));
						/**
						 * first, sort the kvp. In the following loop, because
						 * the list has been sorted from high to low, so if the
						 * value is lower than threshold (the value is bigger,
						 * the article is belongs to the topic in higher prob),
						 * it can break (not write lower prob kvp to Hbase)
						 */
						List<Pair> pairs = keyValuePairs.sort();

						for (Pair p : pairs) {
							if (p.value < THRESHOLD) {
								break;
							}
							Put put = new Put(key.getBytes());
							put.addColumn(family[0].getBytes(), p.key.getBytes(), p.value.toString().getBytes());
							lPuts.add(put);
							System.out.println(key + "--" + p.key + "--" + p.value.toString());
						}
						if (key.equals("21577")) {
							break;
						}
					}
				}
				read.close();
				HBaseDAO.putAll(tableName, lPuts);
			} else {
				System.out.println("file not exist");
			}
		} catch (Exception e) {
			System.out.println("content error");
			e.printStackTrace();
		}
	}

}
