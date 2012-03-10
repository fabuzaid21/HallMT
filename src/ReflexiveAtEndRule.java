import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Move reflexive pronouns after the direct object pronoun
 * 
 * @author kingston
 *
 */
public class ReflexiveAtEndRule extends RegexReorderRule {
	public ReflexiveAtEndRule() {
		this.reorderRegex = "([{VERB}])({PRP})({PRP})";
		this.reorderOrder = new int[] { 1, 3, 2 };
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		Pattern reflexiveRegex = Pattern.compile("(himself|herself|itself)");
		return reflexiveRegex.matcher(words.get(matches.start(2)).word()).matches();
	}
}
