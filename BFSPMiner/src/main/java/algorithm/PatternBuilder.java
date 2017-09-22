package algorithm;

import java.util.ArrayList;

import model.StatusObject;
import model.TreeObject;
import model.TreeObjectList;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class PatternBuilder {

	ArrayList<String> events = new ArrayList<String>();
	private int MAXRULELENGTH;
	TreeObject head = new TreeObject("root", null, -1, 0);

	protected PatternBuilder(final int patternLength) {
		this.MAXRULELENGTH = patternLength;
	}

	// Pattern collection and create StatusObject of this time stamp
	protected StatusObject createPatterns(final String eventLabel, final int timeStamp, final double suppThresh,
			final boolean output, final int batch) {
		this.events.add(eventLabel);
		TreeObject t = this.getInterRules(this.events.size() - 1, batch, this.head);
		t.setCount(this.events.size());
		if (output) {
			StatusObject current = t.makeToStatus(timeStamp, eventLabel, this.events.size() - 1, suppThresh);
			return current;
		}
		return null;
	}

	protected TreeObject getInterRules(final int currentTime, final int batch, final TreeObject t) {

		String pattern = this.events.get(this.events.size() - 1);

		for (int i = 1; i < this.MAXRULELENGTH; i++) {
			if (currentTime - i >= 0) {
				pattern = this.events.get(currentTime - i) + "," + pattern;
			}
		}
		String[] patternList = pattern.split(",");
		patternList = this.invert(patternList);
		this.addListToTree(patternList, currentTime, batch, t);

		return t;
	}

	protected void ssbePrune(TreeObject t, final int batch, final double alpha, final double eps, final int l,
			final int delta) {
		if (t == null) {
			t = this.head;
		}
		TreeObjectList<TreeObject> children = t.getChildren();
		for (int i = 0; i < children.size(); i++) {
			TreeObject child = (TreeObject) children.get(i);
			int lastPrune = child.getTid();
			lastPrune = lastPrune - (lastPrune % delta);
			int b = batch - lastPrune;
			int bPrime = b - child.getBatchCount();
			if (child.getCount() + bPrime * (Math.ceil(alpha * l) - 1) <= eps * b * l) {
				children.remove(i);
				i--;
			} else {

				this.ssbePrune(child, batch, alpha, eps, l, delta);
			}
		}
	}

	protected String[] invert(final String[] toInvert) {
		String[] ret = new String[toInvert.length];
		for (int i = 0; i < toInvert.length; i++) {
			ret[i] = toInvert[toInvert.length - 1 - i];
		}
		return ret;
	}

	protected void addListToTree(final String pattern[], final int currentTime, final int batch, TreeObject t) {
		for (int i = 0; i < pattern.length; i++) {
			int childNumber = t.getChildren().indexOf(pattern[i]);
			if (i == pattern.length - 1) {
				if (childNumber != -1) {
					t = (TreeObject) t.getChildren().get(childNumber);
					t.addToTime(currentTime);
					// here make to increase count
				} else {
					// here use different constructor
					TreeObject p = new TreeObject(pattern[i], t, currentTime, batch);
					t.addChild(p);
					t = p;
				}
			} else {
				if (childNumber != -1) {
					t = (TreeObject) t.getChildren().get(childNumber);
					t.increaseCount();
					// here make to increase count
				} else {
					// here use different constructor
					TreeObject p = new TreeObject(pattern[i], t, batch);
					t.addChild(p);
					t = p;
				}
			}
		}
	}
}
