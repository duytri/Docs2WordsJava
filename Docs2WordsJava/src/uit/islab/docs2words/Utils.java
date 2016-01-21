package uit.islab.docs2words;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static List<String> removeSignToGetWords(String[] wordsTmpArray) {
		List<String> result = new ArrayList<String>();
		String[] specialChars = { " ", ".", ",", "\t", "..." };
		for (String word : wordsTmpArray) {
			if (!specialChars.equals(word)) {
				result.add(word.toLowerCase());
			}
		}
		return result;
	}

	public static void writeFile(String content, String output) throws IOException {
		File file = new File(output);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.flush();
		bw.write(content);
		bw.close();
	}
}
