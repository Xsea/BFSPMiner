package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.PatternObject;
import model.StatusObject;
import utils.Util;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class Predictor {

	private int k;
	private String[] snapshot;
	String[][] predicted;
	private int corrPred, allPred, predictorUsage;

	/**
	 * Creates a Precitor that uses a status to predict new events. Additionally
	 * saves statistics of usage and can return quality metrics
	 *
	 * @param span
	 *            - determines how long a prediction is valid (effects recall and
	 *            f1)
	 * @param k
	 *            - determines how many predictions we make each time (effects
	 *            recall, precision and f1)
	 * @param snapshotLength
	 *            - determines how many past items will be used for the prediction
	 *            (effects runtime)
	 */
	public Predictor(final int span, final int k, final int snapshotLength) {
		this.k = k;
		this.snapshot = new String[snapshotLength];
		this.predicted = new String[span][k];
		Util.fill(this.predicted);
		this.corrPred = 0;
		this.allPred = 0;
		this.predictorUsage = 0;
	}

	/**
	 * makes a prediction
	 *
	 * @param status
	 *            - uses the status (event and pattern list) to make a prediction
	 *            for the next item
	 * @return - returns an array with [span][k] elements, the strings are
	 *         predictions for the next span time stamps
	 */
	public String[][] naivePred(final StatusObject status) {
		// first update statistics
		this.predictorUsage++;
		// here verified predictions will be deleted, so that they can not be verified
		// multiple times
		this.updateCorrectPredict(status.getEvent());
		Util.moveArray(this.predicted);
		String event = status.getEvent();
		Util.moveArray(this.snapshot);
		this.snapshot[0] = event;

		List<PatternObject> filtered = this.filterAndCalcConf(status, event, this.snapshot);
		Comparator<PatternObject> confComp = new Comparator<PatternObject>() {

			@Override
			public int compare(final PatternObject p1, final PatternObject p2) {
				double result = (p1.getConfidence() - p2.getConfidence());
				int res = 0;
				if (result > 0) {
					res = 1;
				} else if (result < 0) {
					res = -1;
				}
				return res;
			}
		};

		Collections.sort(filtered, confComp);
		Collections.reverse(filtered);
		String[] newPredicted = new String[this.k];
		if (filtered.size() > 0 && filtered.get(0).getConfidence() > 0.0) {
			for (int i = 0; i < this.k; i++) {
				if (i < filtered.size() && filtered.get(i).getConfidence() > 0.0) {
					String[] pattern = filtered.get(i).getPattern().split(";");
					newPredicted[i] = pattern[pattern.length - 1];
				} else {
					newPredicted[i] = "";
				}
			}
		} else {
			// predict = event;
			Comparator<PatternObject> suppComp = new Comparator<PatternObject>() {

				@Override
				public int compare(final PatternObject p1, final PatternObject p2) {
					double result = (p1.getSupport() - p2.getSupport());
					int res = 0;
					if (result > 0) {
						res = 1;
					} else if (result < 0) {
						res = -1;
					}
					return res;
				}
			};
			List<PatternObject> patterns = status.getPatterns();
			Collections.sort(patterns, suppComp);
			Collections.reverse(patterns);
			if (patterns.size() > 0) {
				PatternObject pred = patterns.get(0);
				// the split is only necessary at the very beginning (when the patterns of
				// length 2 appeared as often as single items)
				newPredicted[0] = pred.getPattern().split(";")[0];
				for (int i = 1; i < this.k; i++) {
					newPredicted[i] = "";
				}
			} else {
				for (int i = 0; i < this.k; i++) {
					newPredicted[i] = "";
				}
			}
		}
		int x = Util.filterExisting(this.predicted, newPredicted);
		this.allPred += x;
		this.predicted[0] = newPredicted;
		return this.predicted;
	}

	private void updateCorrectPredict(final String pattern) {
		if (Util.contain(this.predicted, pattern)) {
			this.corrPred++;
		}
	}

	private List<PatternObject> filterAndCalcConf(final StatusObject status, final String event,
			final String[] snapshot) {
		ArrayList<PatternObject> patterns = status.getPatterns();
		List<PatternObject> filtered = new ArrayList<>();
		String leftSide = "";
		for (int i = 0; i < snapshot.length; i++) {
			if (i != 0) {
				leftSide = snapshot[i] + ";" + leftSide;
			} else {
				leftSide += snapshot[i];
			}
			double parentCount = 0;
			for (int j = 0; j < patterns.size(); j++) {
				PatternObject trial = patterns.get(j);
				if (trial.getPattern().equals(leftSide)) {
					parentCount = trial.getCount();
				}
			}
			for (int j = 0; j < patterns.size(); j++) {
				PatternObject p = patterns.get(j);
				String[] patternArray = p.getPattern().split(";");
				if (patternArray.length > 1) {
					// remove last part:
					// modify, so that we will later only seperate the first item
					String modify = p.getPattern() + " ";
					// declare last item
					String split = ";" + patternArray[patternArray.length - 1] + " ";
					String[] pattern = modify.split(split);
					// first item in array is our pattern we need
					String match = pattern[0];
					if (leftSide.equals(match)) {
						double confidence = p.getCount();
						confidence = confidence / parentCount;
						p.setConfidence(confidence);
						filtered.add(p);
					}
				}
			}

		}
		return filtered;
	}

	public double getPrecision() {
		return (double) this.corrPred / this.allPred;

	}

	public double getRecall() {
		return (double) this.corrPred / this.predictorUsage;
	}

	public double getF1() {
		double precision = this.getPrecision();
		double recall = this.getRecall();
		return 2 * (precision * recall) / (precision + recall);
	}
}
