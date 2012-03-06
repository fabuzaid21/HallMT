public class MachineTranslator {

	private Dictionary dict;

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
		// TODO Auto-generated constructor stub
		dict = new Dictionary(file);
	}

	public void run() {

	}
}
