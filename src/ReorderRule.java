import java.util.List;
import edu.stanford.nlp.ling.*;

/**
 * Base class for reordering rules
 * @author kingston
 *
 */
public abstract class ReorderRule {
	public abstract List<TaggedWord> processSentence(List<TaggedWord> sentence);
}
