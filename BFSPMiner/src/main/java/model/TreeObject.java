package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import utils.PatternMetrics;
import utils.Util;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class TreeObject {
	// the four Attributes each Node needs for the SS-BE Algorithmn
	// the timestamps Attribute is different
	private String value;
	private ArrayList<Integer> timeStamps = new ArrayList<Integer>();
	private int count = 0;
	private int batchCount = 0;
	private int tid;
	private TreeObjectList<TreeObject> children = new TreeObjectList<TreeObject>();
	private TreeObject parent;

	public TreeObject(final String value, final TreeObject parent, final int time, final int batchTime) {
		this.value = value;
		this.timeStamps.add(time);
		this.count++;
		this.batchCount++;
		this.tid = batchTime;
		this.parent = parent;
	}

	public TreeObject(final String value, final TreeObject parent, final int batchTime) {
		this.value = value;
		this.count++;
		this.batchCount++;
		this.tid = batchTime;
		this.parent = parent;
	}

	public String getValue() {
		return this.value;
	}

	public TreeObject getParent() {
		return this.parent;
	}

	public void setParent(final TreeObject t) {
		this.parent = t;
	}

	public void setCount(final int x) {
		this.count = x;
	}

	// returns all timestamps as an integerArray
	public Object[] getTime() {
		return this.timeStamps.toArray();
	}

	public void addToTime(final int x) {
		this.timeStamps.add(x);
		this.count++;
	}

	public TreeObjectList<TreeObject> getChildren() {
		return this.children;
	}

	public void addChild(final TreeObject t) {
		this.children.add(t);
	}

	public int getBatchCount() {
		return this.batchCount;
	}

	public int getCount() {
		return this.count;
	}

	public void increaseCount() {
		this.count++;
	}

	public boolean containsTime(final int x) {

		for (int i = 0; i < this.timeStamps.size(); i++) {
			if (x == this.timeStamps.get(i)) {
				return true;
			}
		}

		return false;
	}

	public int getTid() {
		return this.tid;
	}

	/**
	 * makes a status from this subtree
	 *
	 * @param timeStamp
	 * @param event
	 * @param iteration
	 * @param suppThresh
	 * @return
	 */
	public StatusObject makeToStatus(final int timeStamp, final String event, final int iteration,
			final double suppThresh) {
		ArrayList<PatternObject> patterns = new ArrayList<PatternObject>();
		String pattern = "";
		boolean finished = false;
		boolean skip = false;
		// fillList(t,patterns, "", suppThresh, confThresh, iteration);
		while (!finished) {
			pattern = Util.nextPattern(pattern, this, skip);
			skip = false;
			if (pattern.equals("finished")) {
				finished = true;
			}

			else {
				String patternArray[] = pattern.split(";");
				patternArray[patternArray.length - 1] = patternArray[patternArray.length - 1].trim();
				double support = PatternMetrics.getCount(patternArray, this);
				support = support / this.getCount();
				if (support >= suppThresh) {
					int count = PatternMetrics.getCount(patternArray, this);
					TreeObject patternNode = this.findPattern(this, patternArray, 0);
					boolean update = patternNode.containsTime(iteration);
					List<String> list = Arrays.asList(patternArray);
					Collections.reverse(list);
					patternArray = (String[]) list.toArray();
					String save = "";
					for (int i = 0; i < patternArray.length; i++) {
						if (i != 0) {
							save = save + ";" + patternArray[i];
						} else {
							save = save + patternArray[i];
						}
					}
					double confidence = 0;
					if (save.split(";").length > 1) {
						confidence = PatternMetrics.getConfidence(save.split(";"), this, count);
					}
					PatternObject p = new PatternObject(save, count, support, confidence, update);
					patterns.add(p);

				} else {
					skip = true;
				}

			}
		}
		StatusObject current = new StatusObject(timeStamp, event, patterns);
		return current;
	}

	private TreeObject findPattern(TreeObject t, final String[] pattern, final int iteration) {
		if (iteration != pattern.length) {
			TreeObject tryOut = t.getChildren().getObject(pattern[iteration]);
			if (tryOut == null) {
				return null;
			} else {
				t = this.findPattern(t.getChildren().getObject(pattern[iteration]), pattern, iteration + 1);
			}
		} else {
			return t;
		}

		return t;
	}
}
