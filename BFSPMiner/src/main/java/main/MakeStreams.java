package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class MakeStreams {
	private String inputFile;

	public MakeStreams() {

	}

	public void setInput(final String input) {
		this.inputFile = input;
	}
	// ************************************************************************************************

	public ArrayList<String> csvIntoStream() throws IOException {
		ArrayList<String> temp = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(this.inputFile));
		try {
			String line = br.readLine();
			while (line != null) {
				String[] lineArray = line.split(",");
				for (int i = 0; i < lineArray.length; i++) {
					String pattern = lineArray[i];
					if (!pattern.equals("")) {
						temp.add(pattern);
					}
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return temp;
	}
}
