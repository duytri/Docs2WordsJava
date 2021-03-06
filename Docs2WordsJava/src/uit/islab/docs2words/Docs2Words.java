package uit.islab.docs2words;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vn.hus.nlp.sd.SentenceDetector;
import vn.hus.nlp.sd.SentenceDetectorFactory;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class Docs2Words {

	/**
	 * Chuong trinh tach cau va tach tu Java
	 * 
	 * @param args[0]
	 *            input directory has splash
	 * @param args[1]
	 *            output directory has splash
	 * @luu_y thu muc models phai dat cung vi tri voi file jar moi chay duoc
	 */
	public static void main(String[] args) {
		if (args[0].equalsIgnoreCase("--help")) {
			System.out.println(
					"Chuong trinh tach cau va tach tu Java\n\targs[0] input directory has splash.\n\targs[1] output directory has splash.\n\tLUU Y thu muc models phai dat cung vi tri voi file jar moi chay duoc");
		} else {
			int nTokens = 0;
			SentenceDetector senDetector = SentenceDetectorFactory.create("vietnamese");

			// get the current dir
			String currentDir = new File("").getAbsolutePath();
			System.out.println(currentDir);
			String currentLibDir = currentDir + File.separator + "libs";
			System.out.println(currentLibDir);
			String inputDirPath = args[0];
			String outputDirPath = args[1];

			File inputDirFile = new File(inputDirPath);

			Properties property = new Properties();
			property.setProperty("sentDetectionModel", currentLibDir + File.separator + "models" + File.separator
					+ "sentDetection" + File.separator + "VietnameseSD.bin.gz");
			property.setProperty("lexiconDFA", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "automata" + File.separator + "dfaLexicon.xml");
			property.setProperty("unigramModel", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "bigram" + File.separator + "unigram.xml");
			property.setProperty("bigramModel", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "bigram" + File.separator + "bigram.xml");
			property.setProperty("externalLexicon", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "automata" + File.separator + "externalLexicon.xml");
			property.setProperty("normalizationRules", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "normalization" + File.separator + "rules.txt");
			property.setProperty("lexers", currentLibDir + File.separator + "models" + File.separator + "tokenization"
					+ File.separator + "lexers" + File.separator + "lexers.xml");
			property.setProperty("namedEntityPrefix", currentLibDir + File.separator + "models" + File.separator
					+ "tokenization" + File.separator + "prefix" + File.separator + "namedEntityPrefix.xml");

			VietTokenizer tokenizer = new VietTokenizer(property);
			tokenizer.turnOffSentenceDetection();

			// get all input files
			/*
			 * File[] inputFiles = FileIterator.listFiles(inputDirFile, new
			 * TextFileFilter(TokenizerOptions.TEXT_FILE_EXTENSION));
			 */
			File[] inputFiles = inputDirFile.listFiles();
			System.out.println("Tokenizing all files in the directory, please wait...");
			long startTime = System.currentTimeMillis();
			for (File aFile : inputFiles) {
				// get the simple name of the file
				String input = aFile.getName();
				// the output file have the same name with the automatic file
				String output = outputDirPath + input;
				System.out.println(aFile.getAbsolutePath() + "\n" + output);
				// tokenize the content of file
				String[] sentences;
				try {
					sentences = senDetector.detectSentences(aFile.getAbsolutePath());
					List<String> content = new ArrayList<String>();
					for (int i = 0; i < sentences.length; i++) {
						String[] words = tokenizer.tokenize(sentences[i]);
						String[] wordsTmpArr = words[0].split(" ");
						content.addAll(Utils.removeSignToGetWords(wordsTmpArr));
					}
					String sContent = "";
					if (content.size() > 0) {
						for (int i = 0; i < content.size() - 1; i++) {
							sContent += content.get(i) + "\n";
						}
						sContent += content.get(content.size() - 1);
						Utils.writeFile(sContent, output);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			long endTime = System.currentTimeMillis();
			float duration = (float) (endTime - startTime) / 1000;
			System.out.println(
					"Tokenized " + nTokens + " words of " + inputFiles.length + " files in " + duration + " (s).\n");
		}
	}
}
