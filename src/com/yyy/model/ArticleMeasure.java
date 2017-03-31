package com.yyy.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleMeasure {
	private List<Double> lScores;
	private List<String> lSentences;

	private String id;
	private String content;
	private String title;

	public void addScore(double d) {
		lScores.add(d);
	}

	public void addSentence(String str) {
		lSentences.add(str);
	}

	public ArticleMeasure(String id, String content, String title) {
		super();
		this.lScores = new ArrayList<>();
		this.lSentences = new ArrayList<>();
		this.id = id;
		this.content = content;
		this.title = title;
	}

	public double sum() {
		double dSum = 0;
		for (Double double1 : lScores) {
			dSum += double1;
		}
		return dSum;
	}

	public double mean() {
		return sum() / (double) (lScores.size());
	}

	public int size() {
		return lScores.size();
	}

	public double max() {
		double dMax = Double.NEGATIVE_INFINITY;
		for (Double double1 : lScores) {
			dMax = double1 > dMax ? double1 : dMax;
		}
		return dMax;
	}

	public double min() {
		double dMin = Double.POSITIVE_INFINITY;
		for (Double double1 : lScores) {
			dMin = double1 < dMin ? double1 : dMin;
		}
		return dMin;
	}

	@Override
	public String toString() {
		return "ArticleMeasure [Mean of Scores=" + this.mean() + "]";
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public Double getlScores(int index) {
		return lScores.get(index);
	}

	public String getlSentences(int index) {
		return lSentences.get(index);
	}

	public List<Double> getlScores() {
		return lScores;
	}

	public List<String> getlSentences() {
		return lSentences;
	}

}
