package utils;

import model.TreeObject;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class Util {

	public static boolean contain(final String[][] predicted, final String pattern) {
		boolean contain = false;
		for (int i = 0; i < predicted.length; i++) {
			for (int j = 0; j < predicted[i].length; j++) {
				if (predicted[i][j].equals(pattern)) {
					contain = true;
					// prediction will be deleted, as it was verified. If we would not delete it,
					// with a higher span one prediction could be verified multiple times
					predicted[i][j] = "";
				}
			}
		}
		return contain;
	}

	// returns an int to count how many of newPredict are actually new
	public static int filterExisting(final String[][] predicted, final String[] newPredicted) {
		int newAdd = newPredicted.length;
		for (int i = 0; i < newPredicted.length; i++) {
			for (int j = 1; j < predicted.length; j++) {
				for (int k = 0; k < predicted[j].length; k++) {
					if (predicted[j][k].equals(newPredicted[i])) {
						newAdd--;
						newPredicted[i] = "";
						k = predicted[j].length;
						j = predicted.length - 1;

					}
				}

			}
		}
		return newAdd;
	}

	public static void fill(final String[][] predicted) {
		for (int i = 0; i < predicted.length; i++) {
			for (int j = 0; j < predicted[i].length; j++) {
				predicted[i][j] = "";
			}
		}

	}

	public static void moveArray(final String[][] array) {
		for (int i = array.length - 1; i >= 1; i--) {
			array[i] = array[i - 1];
		}
	}

	public static void moveArray(final String[] array) {
		for (int i = array.length - 1; i >= 1; i--) {
			array[i] = array[i - 1];
		}
	}

	public static String nextPattern(String currentPattern, final TreeObject t, final boolean skip) {
		if (currentPattern.equals("")) {
			String pattern = ((TreeObject) t.getChildren().get(0)).getValue();
			return pattern;
		}
		String[] pattern = currentPattern.split(";");
		TreeObject pointer = t;
		boolean found = false;
		for (int i = 0; i < pattern.length; i++) {
			pointer = pointer.getChildren().getObject(pattern[i]);
		}
		// we are now at old position (code above) and want to the next, which is the
		// first child of pointer
		if (pointer.getChildren().size() > 0 && !skip) {
			pointer = (TreeObject) pointer.getChildren().get(0);
			found = true;
		} else {

			int i = pattern.length - 1;
			// this node has no child, so go up in the tree and get the next child
			while (i != -1 && !found) {
				pointer = pointer.getParent();
				// is there a child after the one in the pattern?
				if (!(pointer.getChildren().indexOf(pattern[i]) == pointer.getChildren().size() - 1)) {
					// if so, get the next child
					pointer = (TreeObject) pointer.getChildren().get(pointer.getChildren().indexOf(pattern[i]) + 1);

					found = true;
					currentPattern = "";
					for (int j = 0; j < i; j++) {
						if (!currentPattern.equals("")) {
							currentPattern = currentPattern + ";" + pattern[j];
						} else {
							currentPattern = pattern[j];
						}
					}
				}
				// go up again
				else {
					i--;
				}
			}
		}
		if (found) {
			if (currentPattern.equals("")) {

				return pointer.getValue();
			} else {

				return currentPattern + ";" + pointer.getValue();
			}
		} else {
			return "finished";
		}
	}
}
