
public class ObjectPronounAfterRule extends RegexReorderRule {
	public ObjectPronounAfterRule() {
		reorderRegex = "({PRP})([{VERB}{MD}]+)";
		reorderOrder = new int[] { 2, 1 };
		// TODO: Check if PRP is a he/she subject pronoun
	}
}
