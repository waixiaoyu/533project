package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yyy.json.JsonUtils;
import com.yyy.mahout.Seqdumper;

public class CreateID_WORD {

	static String path = "D:\\mahout-work-ubuntu\\reuters-out-seqdir-sparse-lda\\";
	static String[] inputArgs = { "-i", path + "dictionary.file-0", "-o", path + "dictionary.txt" };

	public static String PATH = path + File.separator + "dictionary.txt";
	public static String[] family = { "word" };
	public static String tableName = "ID_WORD";

	public static void main(String[] args) throws Exception {
		// Seqdumper.run(inputArgs);
		HBaseDAO.createTable(tableName, family);
		// HBaseDAO.put(tableName, "zweig", family[0], "41805");

		CreateID_WORD w = new CreateID_WORD();
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
					String[] strs = lineTxt.split(":");
					if (strs.length == 4) {
						Put put = new Put(strs[3].trim().getBytes());
						put.addColumn(family[0].getBytes(), null, strs[1].trim().getBytes());
						lPuts.add(put);
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
