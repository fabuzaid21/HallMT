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
		reorderRegex = "([{VB}{VBD}{VBN}{VBP}{VBZ}][^{NOUN}{PRP}{TO}]*)([{VB}{VBD}{VBP}{VBZ}{MD}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.addAll(words.subList(matches.start(1), matches.end(1)));
		// TODO: Use better subject than just "he"
		newList.add(new TaggedWord("he", "PRP"));
		newList.add(words.get(matches.start(2)));
	}
}
