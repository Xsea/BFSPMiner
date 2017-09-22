package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import model.PatternObject;
import model.StatusObject;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class MakeOutput {
	/**
	 * receives a status object, threshold and location. Will print a .csv file
	 *
	 * @param status
	 * @param minSupp
	 * @param outputLocation
	 * @throws IOException
	 */
	public static void makeOutput(final StatusObject status, final double minSupp, final String outputLocation)
			throws IOException {
		File output = new File(outputLocation);

		// write the rules down and underneath their confidence
		StringBuilder text = new StringBuilder();
		String header = "Patterns,Length,Support,Confidence,Count";
		text.append(header).append("\n");
		ArrayList<PatternObject> patterns = status.getPatterns();
		for (int i = 0; i < patterns.size(); i++) {
			// if(confidence.get(currentConf) > minConf){
			PatternObject p = patterns.get(i);
			String patternString = p.getPattern();
			double support = p.getSupport();
			int count = p.getCount();
			double confidence = p.getConfidence();
			if (support > minSupp) {
				String line = patternString + "," + patternString.split(";").length + "," + support + "," + confidence
						+ "," + count;
				text.append(line).append("\n");
			}
		}
		writeUtf8File(output + "output.csv", text.toString());
	}

	/**
	 * writes a string into a file
	 *
	 * @param filename
	 * @param content
	 */
	public static void writeUtf8File(final String filename, final String content) {
		File file = new File(filename);
		try {
			file.getParentFile().mkdirs();
		} catch (NullPointerException e) {
			// Might happen if parent does not exist
		}

		PrintWriter pstream = null;
		try {
			pstream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8), true);
			pstream.println(content);
		} catch (Exception e) {
			throw new RuntimeException("Cannot write to file " + filename, e);
		} finally {
			if (pstream != null) {
				pstream.close();
			}
		}
	}
}