package com.yyy.model;

import java.io.IOException;

public class WordTopicProb {
	private WordID wi;
	private String topicId;
	private double prob = 0;

	public WordTopicProb(String word) throws IOException {
		super();
		this.wi = new WordID(word);
	}

	@Override
	public String toString() {
		return "WordTopicProb [wi=" + wi + ", topicId=" + topicId + ", prob=" + prob + "]";
	}

	public WordID getWi() {
		return wi;
	}

	public String getTopicId() {
		return topicId;
	}

	public double getProb() {
		return prob;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

}
