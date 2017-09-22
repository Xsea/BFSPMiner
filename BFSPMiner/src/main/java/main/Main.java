package main;

import java.io.IOException;
import java.util.List;

import algorithm.BFSPMiner;
import algorithm.Predictor;
import model.StatusObject;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class Main {

	public static void main(final String args[]) throws IOException {
		MakeStreams streamCreator = new MakeStreams();
		streamCreator.setInput("C:/Users/daniel.toews/Desktop/Arbeit/MSNBC.csv");

		List<String> stream = streamCreator.csvIntoStream();
		long start = System.currentTimeMillis();
		patternMining(stream);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	/**
	 * an example method that recieves a "Stream" of Strings and generates an output
	 * at the end
	 *
	 * @param stream
	 * @throws IOException
	 */
	public static void patternMining(final List<String> stream) throws IOException {
		// since pruning is turned to false, all parameters after false will not have an
		// effect on the output. Still put them in to see example values that were used
		// in the paper
		BFSPMiner miner = new BFSPMiner(10, false, 4, 500, 0.00995, 0.00999);

		for (int i = 0; i < stream.size(); i++) {
			String pattern = stream.get(i);
			if (i == stream.size() - 1) {
				StatusObject status = miner.createPatterns(pattern, 0.01, true);
				MakeOutput.makeOutput(status, 0.01, "");
			} else {
				miner.createPatterns(pattern, 0.01, false);
			}

		}
	}

	/**
	 * an example method that recieves a "Stream" of Strings and predicts the next
	 * items. Prints out the quality at the end
	 *
	 * @param stream
	 * @throws IOException
	 */
	public static void withPrediction(final List<String> stream) throws IOException {
		// parameters that determine how long predictions are active and how many will
		// be made
		int span = 1, k = 1;
		BFSPMiner miner = new BFSPMiner(10, true, 4, 500, 0.00995, 0.00999);
		Predictor predictor = new Predictor(span, k, 8);

		for (int i = 0; i < stream.size(); i++) {
			String pattern = stream.get(i);
			StatusObject status = miner.createPatterns(pattern, 0.01, true);
			// normally returns a String[][], but we do not need it in this example
			predictor.naivePred(status);
		}
		// all the same when span == k == 1
		System.out.println("Precision: " + predictor.getPrecision());
		System.out.println("Recall: " + predictor.getRecall());
		System.out.println("F1: " + predictor.getF1());
	}

}
