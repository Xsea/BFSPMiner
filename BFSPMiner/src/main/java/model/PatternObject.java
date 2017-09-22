package model;

import java.util.ArrayList;

/**
 *
 *
 * simple bean that stores pattern information
 * 
 * @author Daniel Toews
 * @version 1.0
 */
public class PatternObject {
	String pattern;
	int count;
	double support;
	double confidence;
	boolean changed;
	ArrayList<Integer> idList;

	public PatternObject(final String pattern, final int count, final double support, final double confidence,
			final boolean changed) {
		this.pattern = pattern;
		this.count = count;
		this.support = support;
		this.confidence = confidence;
		this.changed = changed;
	}

	public String getPattern() {
		return this.pattern;
	}

	public int getCount() {
		return this.count;
	}

	public double getSupport() {
		return this.support;
	}

	public double getConfidence() {
		return this.confidence;
	}

	public void setConfidence(final double x) {
		this.confidence = x;
	}
}
