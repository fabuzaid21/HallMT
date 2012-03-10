import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class DoubleToEliminationRule extends RegexReorderRule {
	public DoubleToEliminationRule() {
		this.reorderRegex = "({TO})({TO})([{VERB}])";
		this.reorderOrder = new int[] { 1, 3 };
	}

}
