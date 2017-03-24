package com.yyy.hbase;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KeyValuePairs {
	List<Pair> pairs = new LinkedList<>();

	public KeyValuePairs(String[] strs) {
		super();
		for (String string : strs) {
			String key = string.split(":")[0];
			String value = string.split(":")[1];
			pairs.add(new Pair(key, conversion(value)));
		}

	}

	public List<Pair> sort() {
		Collections.sort(pairs);
		return pairs;
	}

	// 将科学计数转换成正常数字
	public double conversion(String str) {
		// System.out.println(str);
		if (str.contains("E-")) {
			String[] s = str.split("E-");
			double num1 = Double.parseDouble(s[0]);
			double num2 = Double.parseDouble(s[1]);
			return num1 * Math.pow(0.1, num2);
		} else {
			return Double.parseDouble(str);
		}

	}

	class Pair implements Comparable<Pair> {
		String key;
		Double value;

		public Pair(String key, Double value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(Pair o) {
			if (this.value > o.value) {
				return -1;
			} else if (this.value < o.value) {
				return 1;
			} else {
				return 0;
			}
		}

	}
}
