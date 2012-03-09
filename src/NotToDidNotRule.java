import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

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
		// TODO: Need a better way to turn from past tense to base form
		String oldVerb = words.get(matches.start(3)).word();
		newList.add(new TaggedWord(oldVerb.replaceAll("ed$", ""), "VB"));
	}
}
