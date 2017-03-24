package com.yyy.mahout;

import org.apache.mahout.utils.*;

public class Seqdumper {
	static String path = "D:\\mahout-work-ubuntu\\reuters-out-matrix\\";
	static String[] inputArgs = { "-i", path + "docIndex", "-o", path + "docIndex.txt" };

	public static void main(String[] args) throws Exception {
		SequenceFileDumper sfDumper = new SequenceFileDumper();
		sfDumper.run(inputArgs);
	}

	public static void run(String[] inputArgs) throws Exception {
		SequenceFileDumper sfDumper = new SequenceFileDumper();
		sfDumper.run(inputArgs);
	}
}
