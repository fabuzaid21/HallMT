import static java.lang.System.out;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineTranslator {

	private Dictionary dict;
	private List<String> sentences;
	private MaxentTagger tagger;
	private ReorderEngine reorderer;

	private final boolean DISABLE_TAGGER = false;

	public static void main(String[] args) {
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
		reorderer = new ReorderEngine();
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
			out.println("Tagged version: " + tagger.tagString(translated));
			out.println();
		}
	}

	private String TranslateSentence(String foreignSentence) {
		// Parse out words
		Pattern wordPattern = Pattern.compile("([\\p{L}]+|[\\p{P}]+)");
		Matcher wordMatcher = wordPattern.matcher(foreignSentence);
		List<String> words = new ArrayList<String>();
		while (wordMatcher.find()) {
			words.add(wordMatcher.group(1));
		}
		// Translate individual words
		List<TaggedWord> translatedWords = new ArrayList<TaggedWord>();
		for (int i = 0; i < words.size(); i++) {
			String translation = null;
			for (int j = 3; j > 0; j--) {
				String testPhrase = "";
				int pos;
				for (pos = i; pos < Math.min(words.size() - 1, i + j); pos++) {
					testPhrase += words.get(pos) + " ";
				}
				translation = dict.getWord(testPhrase.trim());
				if (translation != null) {
					i = pos - 1;
					break;
				}
			}
			if (translation == null) translation = words.get(i);
			for (String w : translation.split(" ")) {
				translatedWords.add(new TaggedWord(w, "UNK"));
			}
		}
		// Tag the words
		List<TaggedWord> taggedWords;
		if (tagger != null) {
			taggedWords = tagger.tagSentence(translatedWords);
		} else {
			taggedWords = translatedWords;
		}
		// Apply reorder engine
		taggedWords = reorderer.reorderSentence(taggedWords);
		// Reform sentence
		String translatedSentence = "";
		boolean firstWord = true;
		for (TaggedWord word : taggedWords) {
			String wordStr = word.word();
			// Check if punctuation is in it or first word
			if (!firstWord && !".,".contains(wordStr)) {
				translatedSentence += " ";
			}
			if (firstWord) {
				wordStr = wordStr.substring(0, 1).toUpperCase() + wordStr.substring(1);
				firstWord = false;
			}
			translatedSentence += wordStr;
		}
		return translatedSentence.trim();
	}
}
