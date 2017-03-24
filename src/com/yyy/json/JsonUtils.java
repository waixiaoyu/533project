package com.yyy.json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class JsonUtils {
	public static void write(String jsonString, String filePath) {
		try {
			FileOutputStream fos = null;
			PrintWriter writer = null;
			fos = new FileOutputStream(filePath);
			writer = new PrintWriter(new OutputStreamWriter(fos, "utf-8"));
			writer.write(jsonString);
			writer.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
