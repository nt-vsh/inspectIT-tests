package test.info.novatec.inspectit.runner.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author Valentin Schraub
 *
 */
public class RunnerTools {

	public static void writeToFile(String fileName, ArrayList<String> content) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				for (String string : content) {
					out.write(string);
					out.newLine();
				}
			} catch (IOException e) {
				System.out.println("IOException while writing results in file " + fileName);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
