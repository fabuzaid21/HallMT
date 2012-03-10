import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.*;

/**
 * Manages the reordering rules and applies them as necessary
 * @author kingston
 *
 */
public class ReorderEngine {
	private ArrayList<ReorderRule> rules = new ArrayList<ReorderRule>();
	
	public ReorderEngine() {
		// Add reorder rules here
		// e.g. rules.add(new RandomRule());
		// Processing of verbs rules
		rules.add(new AlPlusInfinitiveRule());
		rules.add(new InfinitiveToGerundRule());
		rules.add(new DoubleToEliminationRule());
		rules.add(new ObjectPronounAfterRule());
		rules.add(new ReflexiveAtEndRule());
		// English grammar rules
		rules.add(new AVsAnRule());
		// Extraneous Of
		rules.add(new RemoveExtraneousOfRule());
		// Phrase rules
		rules.add(new HaveThatToHaveToRule());
		rules.add(new ItThatToThatRule());
		// Negative handling
		rules.add(new NotToDidNotRule());
		rules.add(new DoubleNegativeRule());
		// Prepend subject rules
		rules.add(new PrependSubjectRule());
		rules.add(new PrependCommaSubjectRule());
		rules.add(new PrependSubjectWithoutNoun());
	}

	public List<TaggedWord> reorderSentence(List<TaggedWord> words) {
		for (ReorderRule rule : rules) {
			words = rule.processSentence(words);
		}
		return words;
	}
}
