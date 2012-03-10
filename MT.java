import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Checks if "a" is followed by a noun starting with a vowel and if so, change it to an "an"
 *
 */
public class AVsAnRule extends ReorderRule {

	@Override
	public List<TaggedWord> processSentence(List<TaggedWord> sentence) {
		for (int i = 0; i < sentence.size() - 1; i++) {
			if (sentence.get(i).word().equals("a")) {
				if (sentence.get(i + 1).word().matches("[aeiou]\\w+")) {
					sentence.get(i).setWord("an");
				}
			}
		}
		return sentence;
	}

}

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class AlPlusInfinitiveRule extends RegexReorderRule {
	public AlPlusInfinitiveRule() {
		this.reorderRegex = "({TO})({DT})({TO})([{VERB}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(new TaggedWord("upon", "IN"));
		TaggedWord newVerb = MorphologyHelper.verbToGerund(words.get(matches.start(4)));
		newList.add(newVerb);
	}
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {

	private Map<String, String> dict;

	public Dictionary(String file) {
		dict = new HashMap<String, String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (true) {
				String line = reader.readLine();

				if (line == null) {
					break;
				}
				String[] words = line.split("\t");
				if (words.length == 2) {
					String key = words[0];
					String value = words[1];
					dict.put(key.toLowerCase(), value);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getWord(String spanishWord) {
		return dict.get(spanishWord.toLowerCase());
	}

}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class DoubleNegativeRule extends RegexReorderRule {
	public DoubleNegativeRule() {
		reorderRegex = "({RB})([{VERB}]+)([^{PUNC}{CC}]+)";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		Pattern keyWords = Pattern.compile("(nothing|never|no|nobody)");
		boolean foundKeyWord = false;
		// You need to make sure a keyword is in the phrase otherwise it'll infinitely loop (continually matching
		// an already processed phrase)
		for (TaggedWord word : words.subList(matches.start(3), matches.end(3))) {
			if (keyWords.matcher(word.word()).matches()) foundKeyWord = true;
		}
		return foundKeyWord && words.get(matches.start(1)).word().equals("not");
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(words.get(matches.start(1)));
		newList.addAll(words.subList(matches.start(2), matches.end(2)));
		// Switch all words
		for (TaggedWord word : words.subList(matches.start(3), matches.end(3))) {
			String wordStr = word.word();
			TaggedWord newWord = new TaggedWord(wordStr, word.tag());
			if (wordStr.equals("nothing")) newWord.setWord("anything");
			else if (wordStr.equals("never")) newWord.setWord("always");
			else if (wordStr.equals("no")) newWord.setWord("some");
			else if (wordStr.equals("nobody")) newWord.setWord("somebody");
			newList.add(newWord);
		}
	}

}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

public class DoubleToEliminationRule extends RegexReorderRule {
	public DoubleToEliminationRule() {
		this.reorderRegex = "({TO})({TO})([{VERB}])";
		this.reorderOrder = new int[] { 1, 3 };
	}

}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

public class HaveThatToHaveToRule extends RegexReorderRule {
	public HaveThatToHaveToRule() {
		this.reorderRegex = "([{VERB}])({IN})({TO}?)";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return MorphologyHelper.verbToBaseTense(words.get(matches.start(1))).word().equals("have") &&
			   words.get(matches.start(2)).word().equals("that");
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(words.get(matches.start(1)));
		newList.add(new TaggedWord("to", "TO"));
	}

}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class InfinitiveToGerundRule extends RegexReorderRule {
	public InfinitiveToGerundRule() {
		this.reorderRegex = "([{IN}])({TO})([{VERB}])";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return !Pattern.matches("(that)", words.get(matches.start(1)).word());
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(words.get(matches.start(1)));
		TaggedWord newVerb = MorphologyHelper.verbToGerund(words.get(matches.start(3)));
		newList.add(newVerb);
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Translating the "lo que" expression in Spanish
 * 
 * @author kingston
 *
 */
public class ItThatToThatRule extends RegexReorderRule {
	public ItThatToThatRule() {
		this.reorderRegex = "({PRP})({IN})";
		this.reorderOrder = new int[] { 2 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return words.get(matches.start(1)).word().equals("it") &&
			   words.get(matches.start(2)).word().equals("that");
	}
}
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class MorphologyHelper {
	public static TaggedWord verbToBaseTense(TaggedWord verb) {
		Morphology wordMorpher = new Morphology();
		return new TaggedWord(wordMorpher.stem(verb.word()), "VB");
	}
	
	public static TaggedWord verbToGerund(TaggedWord verb) {
		Morphology wordMorpher = new Morphology();
		String stem = wordMorpher.stem(verb.word());
		if (!stem.equals("do")) {
			stem = stem.replaceAll("[aeiou]?$", "");
		}
		return new TaggedWord(stem + "ing", "VBG");
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class NotToDidNotRule extends RegexReorderRule {
	public NotToDidNotRule() {
		reorderRegex = "([^{VBD}])({RB})([{VERB}])";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return words.get(matches.start(2)).word().equals("not");
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(words.get(matches.start(1)));
		newList.add(new TaggedWord("did", "VBD"));
		newList.add(words.get(matches.start(2)));
		TaggedWord newVerb = MorphologyHelper.verbToBaseTense(words.get(matches.start(3)));
		newList.add(newVerb);
	}
}

public class ObjectPronounAfterRule extends RegexReorderRule {
	public ObjectPronounAfterRule() {
		reorderRegex = "({PRP})([{VERB}{MD}]+)";
		reorderOrder = new int[] { 2, 1 };
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Prepends a subject to the verb if none provided for past , phrase
 * @author tina
 *
 */
public class PrependCommaSubjectRule extends RegexReorderRule {
	public PrependCommaSubjectRule() {
		reorderRegex = "(^|[^{NOUN}][{,}{-}])([^{PUNC}]+[{,}{-}])([{VERB}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.addAll(words.subList(matches.start(1), matches.end(2)));
		// TODO: Use better subject than just "he"
		newList.add(new TaggedWord("he", "PRP"));
		newList.add(words.get(matches.start(3)));
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Prepends a subject to the verb if none provided
 *
 */
public class PrependSubjectRule extends RegexReorderRule {
	public PrependSubjectRule() {
		reorderRegex = "(^|[{WRB}{.}]|[^{PUNC}][{IN}])([{VB}{VBD}{VBP}{VBZ}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		if (matches.start(1) < matches.end(1)) {
			newList.add(words.get(matches.start(1)));
		}
		// TODO: Use better subject than just "he"
		newList.add(new TaggedWord("he", "PRP"));
		newList.add(words.get(matches.start(2)));
	}
}
import java.util.List;
import java.util.regex.Matcher;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Prepends a subject if there are no subjects between the verb and the previous verb
 * @author kingston
 *
 */
public class PrependSubjectWithoutNoun extends RegexReorderRule {
	public PrependSubjectWithoutNoun() {
		reorderRegex = "([{VB}{VBD}{VBN}{VBP}{VBZ}][^{NOUN}{PRP}{MD}{VERB}{TO}]*)([{VB}{VBD}{VBP}{VBZ}{MD}])";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		for (TaggedWord word : words.subList(matches.start(1), matches.end(1))) {
			if (word.word().equals("not")) return false;
		}
		return true;
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.addAll(words.subList(matches.start(1), matches.end(1)));
		// TODO: Use better subject than just "he"
		newList.add(new TaggedWord("he", "PRP"));
		newList.add(words.get(matches.start(2)));
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Move reflexive pronouns after the direct object pronoun
 * 
 * @author kingston
 *
 */
public class ReflexiveAtEndRule extends RegexReorderRule {
	public ReflexiveAtEndRule() {
		this.reorderRegex = "([{VERB}])({PRP})({PRP})";
		this.reorderOrder = new int[] { 1, 3, 2 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		Pattern reflexiveRegex = Pattern.compile("(himself|herself|itself)");
		return reflexiveRegex.matcher(words.get(matches.start(2)).word()).matches();
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.*;

/**
 * Base class for reordering rules that use the specialized regex format
 * @author kingston
 *
 */
public abstract class RegexReorderRule extends ReorderRule {
	/**
	 * Pseudo-regex of groups (all tags must be surrounded with a {}), e.g. "({PRP})({VRB}+)"
	 */
	protected String reorderRegex = "";
	/**
	 * Comma-delimited reorder order based off groups, e.g. "2,1"
	 */
	protected int[] reorderOrder = new int[] {};
	
	private String[] tags = new String[] {"UNK", ",", ":", ".", "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB"}
;
	
	private final int MAX_REGEX_REODERINGS = 100; // The maximum number of times to apply a rule (to prevent infinite looping)
	
	private static Map<String, String> tagMapping;
	
	private Map<String, String> getTagMapping()
	{
		if (tagMapping == null) {
			tagMapping = new HashMap<String, String>();
			for (int i = 0; i < tags.length; i++) {
				char target;
				if (i < 10) target = (char) ('0' + i);
				else if (i < 36) target = (char) ('a' + i - 10);
				else target = (char) ('A' + i - 36);
				tagMapping.put(tags[i], "" + target);
			}
		}
		return tagMapping;
	}
	
	private String generateTagMappedSentence(List<TaggedWord> sentence) {
		Map<String, String> mapping = getTagMapping();
		String tagStr = "";
		for (TaggedWord word : sentence) {
			String tag = mapping.get(word.tag());
			if (tag == null) throw new IllegalStateException("Unknown tag detected: " + tag);
			tagStr += tag;
		}
		return tagStr;
	}
	
	private Pattern regexPattern;
	
	private Pattern getProcessedRegex() {
		Map<String, String> mapping = getTagMapping();
		if (regexPattern == null) {
			String regex = reorderRegex;
			// Short hand notation - NOTE: You must add [] around a shorthand as it will simply expand out the sequence
			regex = regex.replaceAll("\\{VERB\\}", "{VB}{VBD}{VBG}{VBN}{VBP}{VBZ}");
			regex = regex.replaceAll("\\{NOUN\\}", "{NN}{NNS}{NNP}{NNPS}");
			regex = regex.replaceAll("\\{PUNC\\}", "{,}{:}{.}");
			for (String tag : mapping.keySet()) {
				// Escape bad tags (warning: does not escape non-single-character tags with bad characters.
				regex = regex.replaceAll("\\{" + ("[\\^$.|?*+(){}".contains(tag) ? "\\" : "") + tag + "\\}", mapping.get(tag));
			}
			regexPattern = Pattern.compile(regex);
		}
		return regexPattern;
	}
	
	/**
	 * Optional overridable method to determine if a match is valid and worth reordering
	 * @param matches
	 * @param words
	 * @return
	 */
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return true;
	}
	
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		for (int i = 0; i < reorderOrder.length; i++) {
			newList.addAll(words.subList(matches.start(reorderOrder[i]), matches.end(reorderOrder[i])));
		}
	}
	
	protected List<TaggedWord> reorderSentence(Matcher matches, List<TaggedWord> words) {
		List<TaggedWord> newList = new ArrayList<TaggedWord>();
		// Create beginning, middle, and end
		newList.addAll(words.subList(0, matches.start()));
		appendNewOrder(matches, words, newList);
		newList.addAll(words.subList(matches.end(), words.size()));
		return newList;
	}
	
	public List<TaggedWord> processSentence(List<TaggedWord> sentence) {
		Pattern regexPattern = getProcessedRegex();
		if (regexPattern.pattern().isEmpty()) return sentence;
		// Continue searching until nothing is found
		int maxLoops = MAX_REGEX_REODERINGS;
		while (true) {
			String tagStr = generateTagMappedSentence(sentence);
			Matcher tagMatcher = regexPattern.matcher(tagStr);
	
			boolean matchFound = false;
			while (tagMatcher.find()) {
				if (IsMatchValid(tagMatcher, sentence)) {
					sentence = reorderSentence(tagMatcher, sentence);
					matchFound = true;
					break;
				}
			}
			if (!matchFound) break;
			maxLoops--;
			if (maxLoops == 0) throw new IllegalStateException("Infinite loop in reordering detected - make sure your transformations don't retrigger the match conditions!");
		}
		return sentence;
	}
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class RemoveExtraneousOfRule extends RegexReorderRule {
	public RemoveExtraneousOfRule() {
		reorderRegex = "([{VERB}])({IN})";
		reorderOrder = new int[] { 1 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return words.get(matches.start(2)).word().equals("of");
	}
}
 import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.*;

/**
 * Manages the reordering rules and applies them as necessary
 * @author kingston
 *
 */
public class ReorderEngine {
	private ArrayList<ReorderRule> rules = new ArrayList<ReorderRule>();
	
	public ReorderEngine() {
		// Add reorder rules here
		// e.g. rules.add(new RandomRule());
		// Processing of verbs rules
		rules.add(new InfinitiveToGerundRule());
		rules.add(new DoubleToEliminationRule());
		rules.add(new ObjectPronounAfterRule());
		rules.add(new ReflexiveAtEndRule());
		// English grammar rules
		rules.add(new AVsAnRule());
		// Extraneous Of
		rules.add(new RemoveExtraneousOfRule());
		// Phrase rules
		rules.add(new HaveThatToHaveToRule());
		rules.add(new ItThatToThatRule());
		// Preposition handling
		rules.add(new ToPlusTimePhraseRule());
		rules.add(new AlPlusInfinitiveRule());
		// Negative handling
		rules.add(new NotToDidNotRule());
		rules.add(new DoubleNegativeRule());
		// Prepend subject rules
		rules.add(new PrependSubjectRule());
		rules.add(new PrependCommaSubjectRule());
		rules.add(new PrependSubjectWithoutNoun());
	}

	public List<TaggedWord> reorderSentence(List<TaggedWord> words) {
		for (ReorderRule rule : rules) {
			words = rule.processSentence(words);
		}
		return words;
	}
}
import java.util.List;
import edu.stanford.nlp.ling.*;

/**
 * Base class for reordering rules
 * @author kingston
 *
 */
public abstract class ReorderRule {
	public abstract List<TaggedWord> processSentence(List<TaggedWord> sentence);
}
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.*;

/**
 * Matches "To XYZ hour/day/week/month" and changes it to "on XYZ hour/day/week/month"
 *
 */
public class ToPlusTimePhraseRule extends RegexReorderRule {
	public ToPlusTimePhraseRule() {
		this.reorderRegex = "({TO})([{RB}{JJ}{DT}]*)([{NOUN}])";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return Pattern.matches("(hour|day|week|month)", words.get(matches.start(3)).word());
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(new TaggedWord("on", "IN"));
		newList.addAll(words.subList(matches.start(2), matches.end(3)));
	}
}
