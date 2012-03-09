import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import edu.stanford.nlp.io.EncodingPrintWriter.out;
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
	
	private String[] tags = new String[] {"UNK", "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB"}
;
	
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
			String tag = word.tag();
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
			// Short hand notation
			regex = regex.replaceAll("\\{VERBS\\}", "{VB}{VBD}{VBG}{VBN}{VBP}{VBZ}");
			for (String tag : mapping.keySet()) {
				regex = regex.replaceAll("\\{" + tag + "\\}", mapping.get(tag));
			}
			regexPattern = Pattern.compile(regex);
		}
		return regexPattern;
	}
	
	/**
	 * Optional overridable method to determine if a match is valid and worth reordering
	 * @param match
	 * @return
	 */
	protected boolean IsMatchValid(Matcher match) {
		return true;
	}
	
	private List<TaggedWord> reorderSentence(Matcher matches, List<TaggedWord> words) {
		List<TaggedWord> newList = new ArrayList<TaggedWord>();
		// Create beginning, middle, and end
		newList.addAll(words.subList(0, matches.start()));
		for (int i = 0; i < reorderOrder.length; i++) {
			newList.addAll(words.subList(matches.start(reorderOrder[i]), matches.end(reorderOrder[i])));
		}
		newList.addAll(words.subList(matches.end(), words.size()));
		return newList;
	}
	
	public List<TaggedWord> processSentence(List<TaggedWord> sentence) {
		Pattern regexPattern = getProcessedRegex();
		// Continue searching until nothing is found
		while (true) {
			String tagStr = generateTagMappedSentence(sentence);
			Matcher tagMatcher = regexPattern.matcher(tagStr);
	
			boolean matchFound = false;
			while (tagMatcher.find()) {
				if (IsMatchValid(tagMatcher)) {
					sentence = reorderSentence(tagMatcher, sentence);
					matchFound = true;
					break;
				}
			}
			if (!matchFound) break;
		}
		return sentence;
	}
}
