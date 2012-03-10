import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Translating the "lo que" expression in Spanish
 * 
 * @author kingston
 *
 */
public class ItThatToThatRule extends RegexReorderRule {
	public ItThatToThatRule() {
		this.reorderRegex = "({PRP})({IN})";
		this.reorderOrder = new int[] { 2 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		return words.get(matches.start(1)).word().equals("it") &&
			   words.get(matches.start(2)).word().equals("that");
	}
}
