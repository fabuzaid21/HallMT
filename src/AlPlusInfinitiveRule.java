import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class AlPlusInfinitiveRule extends RegexReorderRule {
	public AlPlusInfinitiveRule() {
		this.reorderRegex = "({TO})({DT})({TO})([{VERB}])";
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(new TaggedWord("upon", "IN"));
		TaggedWord newVerb = MorphologyHelper.verbToGerund(words.get(matches.start(4)));
		newList.add(newVerb);
	}
}
