
public class ObjectPronounAfterRule extends RegexReorderRule {
	public ObjectPronounAfterRule() {
		reorderRegex = "({PRP})([{VERB}{MD}]+)";
		reorderOrder = new int[] { 2, 1 };
	}
}
