import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class Dictionary {

	private Map<String, String> dict;

	public Dictionary(String file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				String[] words = line.split(" ");
				if (words.length == 2) {
					dict.put(words[0], words[1]);
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
