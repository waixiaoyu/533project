package com.mechaglot_Alpha2.test;

import com.mechaglot_Alpha2.controller.Calculate;

public class Test2 {
	public static void main(String[] args) {
		// let us initiate the variables
		Calculate cc = new Calculate();

		String a = "Apple pie sounds very bad";
		String b = "yesterday, I eat a apple, it is very good.";

		float s1 = 0;

		s1 = cc.calculateRBFSimilarity(a, b); // this is how the similarity is
												// calculated.
		System.out.println(s1);
	}
}
