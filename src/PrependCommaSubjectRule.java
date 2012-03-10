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
