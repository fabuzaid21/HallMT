import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class MachineTranslator {

	private Dictionary dict;
	private MaxentTagger tagger;

	public static void main(String[] args) {
		// write rules
		// open the 10 Spanish sentences, and map each word into English
		// Run the POS tagger on the English
		// (Almost there) take rules and apply them to the English words to
		// reorder them into sentences
		// print out results!

		MachineTranslator translator = new MachineTranslator(args[0]);

		translator.run();

	}
	private MachineTranslator(String file) {
		dict = new Dictionary(file);

		try {
			tagger = new MaxentTagger(
					"models/english-left3words-distsim.tagger");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

	}
}
