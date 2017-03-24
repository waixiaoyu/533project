package com.mechaglot_Alpha2.controller;

import java.io.BufferedReader;
import java.io.FileReader;

public class test2 {

	public static void main(String[] args) {
	    try {
	    	Calculate c = new Calculate();
	    	BufferedReader br = new BufferedReader(new FileReader("testdata/separate.csv"));
	        String line = br.readLine();
	        while ((line = br.readLine()) != null) {
	           
	        	line = line.substring(0,line.lastIndexOf(","));
	        	float qq = c.calculateForDataRow(line);
	        	float ww = c.calculateForDataRowFast(line);
	        	System.out.println(qq+"\t"+ww);
	        }
	        br.close();
	    }  
	    catch (Exception e) {
	       e.printStackTrace();
	    }

	}

}
