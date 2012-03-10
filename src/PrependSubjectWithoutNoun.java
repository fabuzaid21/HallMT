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
