package com.yyy.model;

public class WordProb {
	String word;
	String prob;

	public WordProb(String word, String prob) {
		this.word = word;
		this.prob = prob;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getProb() {
		return prob;
	}

	public void setProb(String prob) {
		this.prob = prob;
	}
}