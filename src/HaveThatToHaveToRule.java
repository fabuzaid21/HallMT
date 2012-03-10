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
