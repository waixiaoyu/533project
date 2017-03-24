package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;

public class CreateID_ARTICLE_TITLE_CONTENT {

	static String path = "D:\\mahout-work-ubuntu\\reuters-out-matrix\\";
	static String[] inputArgs = { "-i", path + "docIndex", "-o", path + "docIndex.txt" };

	public static String PATH = path + File.separator + "docIndex.txt";
	public static String[] family = { "article" };
	public static String[] qualifer = { "alias", "title", "content" };
	public static String tableName = "ARTICLE_ALIAS_TITLE_CONTENT";

	static String prefixTitleFile = "D:\\mahout-work-ubuntu\\reuters-out";

	public static void main(String[] args) throws Exception {
		// Seqdumper.run(inputArgs);
		HBaseDAO.deleteTable(tableName);
		HBaseDAO.createTable(tableName, family);
		//
		CreateID_ARTICLE_TITLE_CONTENT w = new CreateID_ARTICLE_TITLE_CONTENT();
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
					/**
					 * ex Key, 0, Value, /reuters-out/reut2-000.sgm-0.txt
					 */
					String[] strs = lineTxt.split(":");
					if (strs.length == 4) {
						String title = readTitle(prefixTitleFile + strs[3].trim());
						String content = readContent(prefixTitleFile + strs[3].trim());
						if (title != null) {
							System.out.println(strs[3].trim());
							// HBaseDAO.put(tableName, strs[1].trim(),
							// family[0], qualifer[0], strs[3].trim());
							// HBaseDAO.put(tableName, strs[1].trim(),
							// family[0], qualifer[1], title);
							Put put = new Put(strs[1].trim().getBytes());
							put.addColumn(family[0].getBytes(), qualifer[0].getBytes(), strs[3].trim().getBytes());
							lPuts.add(put);
							put = new Put(strs[1].trim().getBytes());
							put.addColumn(family[0].getBytes(), qualifer[1].getBytes(), title.getBytes());
							lPuts.add(put);
							put = new Put(strs[1].trim().getBytes());
							put.addColumn(family[0].getBytes(), qualifer[2].getBytes(), content.getBytes());
							lPuts.add(put);
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

	private static String readTitle(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				for (int i = 0; i < 2; i++) {
					bufferedReader.readLine();
				}
				String title = bufferedReader.readLine();
				System.out.println(title);
				read.close();
				return title;
			} else {
				System.out.println("file not exist");
			}
		} catch (

		Exception e) {
			System.out.println("content error");
			e.printStackTrace();
		}
		return null;
	}

	private static String readContent(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				for (int i = 0; i < 4; i++) {
					bufferedReader.readLine();
				}
				String content = "";
				String lineTxt = "";
				while ((lineTxt = bufferedReader.readLine()) != null) {
					content += lineTxt;
				}
				read.close();
				return content;
			} else {
				System.out.println("file not exist");
			}
		} catch (

		Exception e) {
			System.out.println("content error");
			e.printStackTrace();
		}
		return null;
	}
}
