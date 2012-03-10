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
		reorderRegex = "(^|[^{WRB}]{,})([^{PUNC}]+{,})([{VERB}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		int start = matches.start(1);
		int end = matches.end(2);
		for (int i = start; i < end; ++i) {
			newList.add(words.get(i));
		}
		// TODO: Use better subject than just "he"
		newList.add(new TaggedWord("he", "PRP"));
		newList.add(words.get(matches.start(3)));
	}
}
