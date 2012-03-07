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
		String inputPath = args.length > 0 ? args[0] : "../data/test.txt";
		String dictionaryPath = args.length > 1 ? args[1] : "../data/dict.dat";

		MachineTranslator translator = new MachineTranslator(inputPath,
				dictionaryPath);
		translator.run();
	}
	private MachineTranslator(String file) {
		dict = new Dictionary(file);

	}

	private MachineTranslator(String inputPath, String dictionaryPath) {
		// TODO Auto-generated constructor stub
		dict = new Dictionary(dictionaryPath);
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
