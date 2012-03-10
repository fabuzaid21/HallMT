import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Prepends a subject to the verb if none provided
 * 
 * @author kingston
 *
 */
public class PrependSubjectRule extends RegexReorderRule {
	public PrependSubjectRule() {
		reorderRegex = "(^|[{WRB}{.}])([{VB}{VBD}{VBP}{VBZ}])";
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
