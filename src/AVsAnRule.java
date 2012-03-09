import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Checks if "a" is followed by a noun starting with a vowel and if so, change it to an "an"
 * 
 * NOTE: Not actually utilized in our test sentences
 * 
 * @author kingston
 *
 */
public class AVsAnRule extends ReorderRule {

	@Override
	public List<TaggedWord> processSentence(List<TaggedWord> sentence) {
		for (int i = 0; i < sentence.size() - 1; i++) {
			if (sentence.get(i).word().equals("a")) {
				if (sentence.get(i + 1).word().matches("[aeiou]\\w+")) {
					sentence.get(i).setWord("an");
				}
			}
		}
		return sentence;
	}

}
