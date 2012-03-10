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
		rules.add(new AlPlusInfinitiveRule());
		rules.add(new InfinitiveToGerundRule());
		rules.add(new DoubleToEliminationRule());
		rules.add(new ObjectPronounAfterRule());
		rules.add(new AVsAnRule());
		rules.add(new PrependSubjectRule());
		rules.add(new PrependCommaSubjectRule());
		rules.add(new NotToDidNotRule());
		rules.add(new DoubleNegativeRule());
		rules.add(new RemoveExtraneousOfRule());
	}

	public List<TaggedWord> reorderSentence(List<TaggedWord> words) {
		for (ReorderRule rule : rules) {
			words = rule.processSentence(words);
		}
		return words;
	}
}
