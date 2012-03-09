
public class ObjectPronounAfterRule extends RegexReorderRule {
	public ObjectPronounAfterRule() {
		reorderRegex = "({PRP})([{VERBS}]+)";
		reorderOrder = new int[] { 2, 1 };
	}
}
