package model;

import java.util.ArrayList;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class StatusObject {
	private int count;
	private String event;
	private ArrayList<PatternObject> patterns = new ArrayList<PatternObject>();

	/**
	 * The current status of the mining results
	 *
	 * @param count
	 *            - how many items were processed
	 * @param event
	 *            - which item was processed last
	 * @param patterns
	 *            - all sequential patterns (depending on the threshold given while
	 *            generating this) in a list
	 */
	public StatusObject(final int count, final String event, final ArrayList<PatternObject> patterns) {
		this.count = count;
		this.event = event;
		this.patterns = patterns;
	}

	public int getCount() {
		return this.count;
	}

	public String getEvent() {
		return this.event;
	}

	public ArrayList<PatternObject> getPatterns() {
		return this.patterns;
	}

}
