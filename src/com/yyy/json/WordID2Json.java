package com.yyy.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordID2Json {
	public static String PATH = "G:\\graduate\\CS535 Pattern Recognition\\535-web-demo\\data" + File.separator
			+ "word-id.txt";

	private static JSONArray jArray = new JSONArray();

	public static void main(String[] args) {
		WordID2Json w = new WordID2Json();
		w.readTxtFile(PATH);
		JsonUtils.write(jArray.toString(), PATH.substring(0, PATH.lastIndexOf('.') + 1) + "json");
	}

	private void readTxtFile(String filePath) {
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
					System.out.println(lineTxt);
					String[] strs = lineTxt.split(":");
					if (strs.length == 4) {
						createJsonObject(strs[1].trim(), strs[3].trim());
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

	private void createJsonObject(String key, String value) {
		JSONObject jObj = new JSONObject();
		jObj.put("name", key);
		jObj.put("index", value);
		jArray.put(jObj);
	}
}
