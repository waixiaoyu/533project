package com.yyy.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

import com.yyy.hbase.KeyValuePairs.Pair;
import com.yyy.mahout.Seqdumper;

public class CreateTOPIC_WORD10_filter {

	static String path = "D:\\mahout-work-ubuntu\\reuters-lda\\";
	static String[] inputArgs = { "-i", path + "part-m-00000", "-o", path + "topic-word-0.txt" };
	public static String PATH = path + File.separator + "topic-word-0.txt";
	public static String[] family = { "word" };
	public static String tableName = "TOPIC_WORD5";

	private static final int MAX_WORDS = 10;
	/**
	 * use a hashset to store common words in memory at first, from a txt file
	 */
	private Set<String> setCommonWords = new HashSet<>();
	/**
	 * use a hashmap to store the content of table "ID_WORD" in memory, in case
	 * querying the table every times when inserting put into List<puts>
	 */
	private Map<String, String> mapIDWord = new HashMap<>();

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 10; i++) {
			inputArgs[1] = inputArgs[1].substring(0, inputArgs[1].length() - 1) + i;
			inputArgs[3] = path + "topic-word-" + i + ".txt";
			Seqdumper.run(inputArgs);
		}
		HBaseDAO.deleteTable(tableName);
		HBaseDAO.createTable(tableName, family);
		// HBaseDAO.put(tableName, "zweig", family[0], "41805");

		CreateTOPIC_WORD10_filter w = new CreateTOPIC_WORD10_filter();
		w.loadCommonWords("commonwords.txt");
		w.loadIDWordMap("ID_WORD");
		for (int i = 0; i < 10; i++) {
			w.readTxtAndImport(path + "topic-word-" + i + ".txt");
		}
	}

	/**
	 * read the words list to filter the common words, like ' am is are have has
	 * and more '
	 * 
	 * @throws IOException
	 */
	private void loadCommonWords(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			setCommonWords.add(str);
		}
		bufferedReader.close();
		System.out.println("load common words set success");
	}

	/**
	 * load ID->Word map to memory
	 * 
	 * @throws IOException
	 */
	private void loadIDWordMap(String tableName) throws IOException {
		List<Result> list = HBaseDAO.scanRowKeyByFilter(tableName, null);
		for (Result result : list) {
			List<Cell> cells = result.getColumnCells("word".getBytes(), null);
			for (Cell c : cells) {
				mapIDWord.put(new String(result.getRow()), new String(CellUtil.cloneValue(c)));
			}
		}
		System.out.println("load id->word map success");
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

				for (int j = 0; j < 2; j++) {
					// System.out.println(lineTxt);
					lineTxt = bufferedReader.readLine();
					String[] strs = lineTxt.split("Key")[1].trim().split("Value");
					if (strs.length == 2) {
						// HBaseDAO.put(tableName, strs[1].trim(), family[0],
						// strs[3].trim());
						String key = strs[0].replaceAll(":", "").trim();
						// complement bits for key
						key = key.length() < 2 ? "0" + key : key;
						String value = strs[1].substring(strs[1].indexOf("{") + 1, strs[1].indexOf("}"));
						KeyValuePairs keyValuePairs = new KeyValuePairs(value.split(","));
						List<Pair> pairs = keyValuePairs.sort();

						int i = 0, nNum = 0;

						while (nNum < MAX_WORDS) {

							String strCurrentWord = mapIDWord.get(pairs.get(i).key);
							/**
							 * here to filter the common words, if the word is
							 * number, or in common list, will be filtered
							 */
							if (!((strCurrentWord.length() < 4 && StringUtils.isNumeric(strCurrentWord))
									|| setCommonWords.contains(strCurrentWord) || strCurrentWord.length() < 2)) {
								Put put = new Put(key.getBytes());
								put.addColumn(family[0].getBytes(), pairs.get(i).key.getBytes(),
										pairs.get(i).value.toString().getBytes());
								lPuts.add(put);
								System.out
										.println(key + "--" + pairs.get(i).key + "--" + pairs.get(i).value.toString());
								nNum++;
							}
							i++;
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
