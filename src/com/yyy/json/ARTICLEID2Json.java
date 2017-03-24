package com.yyy.json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yyy.hbase.HBaseDAO;

public class ARTICLEID2Json {
	public static String PATH = "G:\\graduate\\CS535 Pattern Recognition\\535-web-demo\\data" + File.separator
			+ "title-id.txt";

	private static JSONArray jArray = new JSONArray();
	public static String tableName = "ARTICLE_ALIAS_TITLE";

	public static void main(String[] args) throws IOException {

		ARTICLEID2Json w = new ARTICLEID2Json();
		w.scanHbase();
		JsonUtils.write(jArray.toString(), PATH.substring(0, PATH.lastIndexOf('.') + 1) + "json");
	}

	private void scanHbase() throws IOException {
		List<Result> l = HBaseDAO.scanRowKeyByFilter(tableName, null);
		for (Result result : l) {
			System.out.println(new String(result.getRow()));
			System.out.println(new String(result.getValue("article".getBytes(), "title".getBytes())));
			createJsonObject(new String(result.getValue("article".getBytes(), "title".getBytes())),
					new String(result.getRow()));
		}
	}

	private void createJsonObject(String key, String value) {
		JSONObject jObj = new JSONObject();
		jObj.put("name", key);
		jObj.put("index", value);
		jArray.put(jObj);
	}
}
