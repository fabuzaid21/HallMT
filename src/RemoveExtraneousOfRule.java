import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class RemoveExtraneousOfRule extends RegexReorderRule {
	public RemoveExtraneousOfRule() {
		reorderRegex = "([{VERB}])({IN})";
		reorderOrder = new int[] { 1 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return words.get(matches.start(2)).word().equals("of");
	}
}
