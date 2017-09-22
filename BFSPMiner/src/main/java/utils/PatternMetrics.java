package utils;

import model.TreeObject;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class PatternMetrics {

	public static double getConfidence(final String[] pattern, TreeObject t, final double count) {
		double countB = 0;
		for (int i = 0; i < pattern.length - 1; i++) {
			t = t.getChildren().getObject(pattern[i]);
		}
		countB = t.getCount();
		return count / countB;
	}

	public static int getCount(final String[] pattern, TreeObject t) {
		int countB = 0;
		for (int i = 0; i < pattern.length; i++) {
			t = t.getChildren().getObject(pattern[i]);
		}

		countB = t.getCount();
		return countB;
	}
}
