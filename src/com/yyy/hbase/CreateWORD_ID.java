package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yyy.json.JsonUtils;
import com.yyy.mahout.Seqdumper;

public class CreateWORD_ID {

	static String path = "D:\\mahout-work-ubuntu\\reuters-out-seqdir-sparse-lda\\";
	static String[] inputArgs = { "-i", path + "dictionary.file-0", "-o", path + "dictionary.txt" };

	public static String PATH = path + File.separator + "dictionary.txt";
	public static String[] family = { "id" };
	public static String tableName = "WORD_ID";

	public static void main(String[] args) throws Exception {
		// Seqdumper.run(inputArgs);
		HBaseDAO.createTable("WORD_ID", family);
		//HBaseDAO.put(tableName, "zweig", family[0], "41805");
		
		CreateWORD_ID w = new CreateWORD_ID();
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
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					String[] strs = lineTxt.split(":");
					if (strs.length == 4) {
						HBaseDAO.put(tableName, strs[1].trim(), family[0], strs[3].trim());
					}
				}
				read.close();
			} else {
				System.out.println("file not exist");
			}
		} catch (Exception e) {
			System.out.println("content error");
			e.printStackTrace();
		}
	}

}
