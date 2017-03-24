package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yyy.hbase.KeyValuePairs.Pair;
import com.yyy.json.JsonUtils;
import com.yyy.mahout.Seqdumper;

public class CreateARTICLE_TOPIC20 {

	static String path = "D:\\mahout-work-ubuntu\\reuters-lda-topics\\";
	static String[] inputArgs = { "-i", path + "part-m-00000", "-o", path + "lad-topics.txt" };
	public static String PATH = path + File.separator + "lad-topics.txt";
	public static String[] family = { "topic" };
	public static String tableName = "ARTICLE_TOPIC20";

	public static void main(String[] args) throws Exception {
		Seqdumper.run(inputArgs);
		HBaseDAO.deleteTable(tableName);
		HBaseDAO.createTable(tableName, family);
		// HBaseDAO.put(tableName, "zweig", family[0], "41805");

		CreateARTICLE_TOPIC20 w = new CreateARTICLE_TOPIC20();
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
					// System.out.println(lineTxt);
					String[] strs = lineTxt.split("Key")[1].trim().split("Value");
					if (strs.length == 2) {
						// HBaseDAO.put(tableName, strs[1].trim(), family[0],
						// strs[3].trim());
						String key = strs[0].replaceAll(":", "").trim();
						String value = strs[1].substring(strs[1].indexOf("{") + 1, strs[1].indexOf("}"));
						KeyValuePairs keyValuePairs = new KeyValuePairs(value.split(","));
						List<Pair> pairs = keyValuePairs.sort();

						for (int i = 0; i < 20; i++) {
							// HBaseDAO.put(tableName, key, family[0],
							// pairs.get(i).key, pairs.get(i).value.toString());
							Put put = new Put(key.getBytes());
							put.addColumn(family[0].getBytes(), pairs.get(i).key.getBytes(),
									pairs.get(i).value.toString().getBytes());
							lPuts.add(put);
							System.out.println(key + "--" + pairs.get(i).key + "--" + pairs.get(i).value.toString());
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
