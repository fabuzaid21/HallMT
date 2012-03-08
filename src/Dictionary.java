import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {

	private Map<String, String> dict;

	public Dictionary(String file) {
		dict = new HashMap<String, String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (true) {
				String line = reader.readLine();

				if (line == null) {
					break;
				}
				String[] words = line.split(" ", 2);
				if (words.length == 2) {
					dict.put(words[0],
							words[1].substring(1, words[1].length() - 1));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getWord(String spanishWord) {
		return dict.get(spanishWord);
	}

}
