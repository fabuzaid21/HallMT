import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.*;

/**
 * Matches "To XYZ hour/day/week/month" and changes it to "on XYZ hour/day/week/month"
 *
 */
public class ToPlusTimePhraseRule extends RegexReorderRule {
	public ToPlusTimePhraseRule() {
		this.reorderRegex = "({TO})([{RB}{JJ}{DT}]*)([{NOUN}])";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return Pattern.matches("(hour|day|week|month)", words.get(matches.start(3)).word());
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(new TaggedWord("on", "IN"));
		newList.addAll(words.subList(matches.start(2), matches.end(3)));
	}
}
