import static java.lang.System.out;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineTranslator {

	private Dictionary dict;
	private List<String> sentences;
	private MaxentTagger tagger;

	private final boolean DISABLE_TAGGER = true;

	public static void main(String[] args) {
		// write rules
		// open the 10 Spanish sentences, and map each word into English
		// Run the POS tagger on the English
		// (Almost there) take rules and apply them to the English words to
		// reorder them into sentences
		// print out results!
		String inputPath = args.length > 0 ? args[0] : "data/test.txt";
		String dictionaryPath = args.length > 1 ? args[1] : "data/dict.dat";

		MachineTranslator translator = new MachineTranslator(inputPath,
				dictionaryPath);
		translator.run();
	}

	private MachineTranslator(String inputPath, String dictionaryPath) {
		// TODO Auto-generated constructor stub
		dict = new Dictionary(dictionaryPath);
		try {
			if (!DISABLE_TAGGER) {
				tagger = new MaxentTagger(
						"libs/models/english-left3words-distsim.tagger");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sentences = GetSentencesFromFile(inputPath);
	}

	private ArrayList<String> GetSentencesFromFile(String inputPath) {
		BufferedReader reader = null;
		ArrayList<String> sentences = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(inputPath));
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				sentences.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sentences;
	}

	public void run() {
		// Translate sentence
		int i = 0;
		for (String sentence : sentences) {
			i++;
			String translated = TranslateSentence(sentence);
			out.println("Sentence " + i);
			out.println("==========");
			out.println("Spanish version: " + sentence);
			out.println("English translation: " + translated);
			if (tagger != null) {
				out.println("Tagged English translation: "
						+ tagger.tagString(translated));
			}
			out.println();
		}
	}

	private String TranslateSentence(String foreignSentence) {
		String englishSentence = "";
		Pattern wordPattern = Pattern
				.compile("([-,¿¡]?)([ÁáÉéÍíÑñÓóÚúÜü\\w]+)([-,.¿¡]?)");
		for (String word : foreignSentence.split(" ")) {
			Matcher wordMatcher = wordPattern.matcher(word);

			if (wordMatcher.find()) {
				int count = wordMatcher.groupCount();
				for (int i = 1; i <= count; ++i) {
					String entry = wordMatcher.group(i);
					if (!entry.equals("")) {
						englishSentence += dict.getWord(entry.toLowerCase());
					}
				}

			}
			englishSentence += " ";
			// englishSentence += " " + dict.getWord(word.toLowerCase());

		}
		return englishSentence;
	}
}
