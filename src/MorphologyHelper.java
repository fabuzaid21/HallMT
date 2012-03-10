import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

public class MorphologyHelper {
	public static TaggedWord PastTenseToBaseTense(TaggedWord verb) {
		Morphology wordMorpher = new Morphology();
		return new TaggedWord(wordMorpher.stem(verb.word()), "VB");
	}
	
	public static TaggedWord verbToGerund(TaggedWord verb) {
		Morphology wordMorpher = new Morphology();
		String stem = wordMorpher.stem(verb.word());
		if (!stem.equals("do")) {
			stem = stem.replaceAll("[aeiou]?$", "");
		}
		return new TaggedWord(stem + "ing", "VBG");
	}
}
