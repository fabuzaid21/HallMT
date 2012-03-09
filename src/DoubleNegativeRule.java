import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class DoubleNegativeRule extends RegexReorderRule {
	public DoubleNegativeRule() {
		reorderRegex = "({RB})([{VERB}]+)([^{PUNC}{CC}]+)";
	}
	
	@Override
	protected boolean IsMatchValid(Matcher matches, List<TaggedWord> words) {
		Pattern keyWords = Pattern.compile("(nothing|never|no|nobody)");
		boolean foundKeyWord = false;
		// You need to make sure a keyword is in the phrase otherwise it'll infinitely loop (continually matching
		// an already processed phrase)
		for (TaggedWord word : words.subList(matches.start(3), matches.end(3))) {
			if (keyWords.matcher(word.word()).matches()) foundKeyWord = true;
		}
		return foundKeyWord && words.get(matches.start(1)).word().equals("not");
	}
	
	@Override
	protected void appendNewOrder(Matcher matches, List<TaggedWord> words, List<TaggedWord> newList) {
		newList.add(words.get(matches.start(1)));
		newList.addAll(words.subList(matches.start(2), matches.end(2)));
		// Switch all words
		for (TaggedWord word : words.subList(matches.start(3), matches.end(3))) {
			String wordStr = word.word();
			TaggedWord newWord = new TaggedWord(wordStr, word.tag());
			if (wordStr.equals("nothing")) newWord.setWord("anything");
			else if (wordStr.equals("never")) newWord.setWord("always");
			else if (wordStr.equals("no")) newWord.setWord("some");
			else if (wordStr.equals("nobody")) newWord.setWord("somebody");
			newList.add(newWord);
		}
	}

}
