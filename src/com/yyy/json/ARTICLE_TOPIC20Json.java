package com.yyy.json;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yyy.hbase.HBaseDAO;

public class ARTICLE_TOPIC20Json {

	private static JSONArray jArray = new JSONArray();
	public static String tableName = "ARTICLE_TOPIC20";

	public static void main(String[] args) throws IOException {

		String rowKey = "1";
		ARTICLE_TOPIC20Json w = new ARTICLE_TOPIC20Json();
		w.scanHbase(rowKey);
		JsonUtils.write(jArray.toString(), "article_" + rowKey + "_topic20" + ".json");
	}

	/**
	 * input=rowkey, output=20 topic probs
	 * 
	 * @param rowKey
	 * @throws IOException
	 */
	private void scanHbase(String rowKey) throws IOException {
		Result r = HBaseDAO.get(tableName, rowKey);
		NavigableMap<byte[], byte[]> maps = r.getFamilyMap("topic".getBytes());
		for (Entry<byte[], byte[]> e : maps.entrySet()) {
			System.out.println(new String(e.getKey()) + "-" + new String(e.getValue()));
			createJsonObject(new String(e.getKey()), new String(e.getValue()));
		}
	}

	private void createJsonObject(String key, String value) {
		JSONObject jObj = new JSONObject();
		jObj.put("name", key);
		jObj.put("y", value);
		jArray.put(jObj);
	}
}
