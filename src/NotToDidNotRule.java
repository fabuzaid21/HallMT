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
		Morphology wordMorpher = new Morphology();
		TaggedWord oldVerb = words.get(matches.start(3));
		newList.add(new TaggedWord(wordMorpher.stem(oldVerb.word()), "VB"));
	}
}
