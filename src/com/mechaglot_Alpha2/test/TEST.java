package com.mechaglot_Alpha2.test;

import com.mechaglot_Alpha2.controller.Calculate;

public class TEST {

	public static void main(String[] args) {
		
		//this is also an example of how-to use this program
		
		//let us initiate the variables
		Calculate cc = new Calculate();
		
		String a = "Pete and Rob have found a dog near the station.";
		String b = "Pete and Rob have never found a dog near the station.";
		String c = "Patricia found a dog near the station.";
		String d = "It was a dog who found Pete and Rob under the snow.";
		String e = "I am fine, thanks!";
		String f = "Hello there, how are you?";
		
		float s1 = 0;
		float s2 = 0;
		float s3 = 0;
		float s4 = 0;
		
		float ss1 = 0;
		float ss2 = 0;
		float ss3 = 0;
		float ss4 = 0;
		
		
		//Now, let us calculate the similarity between those sentences, using the Neural Net model file
		final long startTime3 = System.currentTimeMillis(); //initiate the timer
		
		s1 = cc.calculateNNSimilarity(a, b); //this is how the similarity is calculated.
		s2 = cc.calculateNNSimilarity(c, d);
		s3 = cc.calculateNNSimilarity(c, e);
		s4 = cc.calculateNNSimilarity(f, e);
		final long endTime3 = System.currentTimeMillis(); //stop the timer
		long execution3 = endTime3 - startTime3;

		//Now, let us calculate the similarity between those sentences, using the RBF model file
		final long startTime1 = System.currentTimeMillis(); //initiate the timer
		
		s1 = cc.calculateRBFSimilarity(a, b); //this is how the similarity is calculated.
		s2 = cc.calculateRBFSimilarity(c, d);
		s3 = cc.calculateRBFSimilarity(c, e);
		s4 = cc.calculateRBFSimilarity(f, e);
		
		final long endTime1 = System.currentTimeMillis(); //stop the timer
		long execution1 = endTime1 - startTime1;
		
		//Now, let us calculate the similarity between those sentences, using the fast calculating
		final long startTime2 = System.currentTimeMillis(); //initiate the timer

		ss1 = cc.calculateRBFSimilarityFast(a, b); //this is how the fast-similarity is calculated.
		ss2 = cc.calculateRBFSimilarityFast(c, d);
		ss3 = cc.calculateRBFSimilarityFast(c, e);
		ss4 = cc.calculateRBFSimilarityFast(f, e);
		
		final long endTime2 = System.currentTimeMillis(); //stop the timer
		long execution2 = endTime2 - startTime2;
		
		
		//Now, let us output the results
		System.out.println("---------START-----------");

		System.out.println("-Using the Neural Net model method");
		System.out.println("Similarity between the sentences:");
		System.out.println(a);
		System.out.println(b);
		System.out.println("is: "+s1);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(d);
		System.out.println("is: "+s2);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(e);
		System.out.println("is: "+s3);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(f);
		System.out.println(e);
		System.out.println("is: "+s4);
		System.out.println("The overall time to compute the examples using this method was: "+execution3+" nanoseconds.");
		System.out.println("--------------------");
		
		
		System.out.println("--------------------");
		System.out.println("-Using the RBF model method");
		System.out.println("Similarity between the sentences:");
		System.out.println(a);
		System.out.println(b);
		System.out.println("is: "+s1);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(d);
		System.out.println("is: "+s2);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(e);
		System.out.println("is: "+s3);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(f);
		System.out.println(e);
		System.out.println("is: "+s4);
		System.out.println("The overall time to compute the examples using this method was: "+execution1+" nanoseconds.");
		System.out.println("--------------------");
		
		
		
		System.out.println("--------------------");
		System.out.println("-Using the fast method");
		System.out.println("Similarity between the sentences:");
		System.out.println(a);
		System.out.println(b);
		System.out.println("is: "+ss1);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(d);
		System.out.println("is: "+ss2);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(c);
		System.out.println(e);
		System.out.println("is: "+ss3);
		System.out.println("--------------------");
		
		System.out.println("Similarity between the sentences:");
		System.out.println(f);
		System.out.println(e);
		System.out.println("is: "+ss4);
		System.out.println("The overall time to compute the examples using this method was: "+execution2+" nanoseconds.");
		System.out.println("---------END-----------");
	}

}
