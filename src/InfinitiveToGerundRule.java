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
