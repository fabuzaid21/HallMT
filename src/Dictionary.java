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
				String[] words = line.split("\t");
				if (words.length == 2) {
					String key = words[0];
					String value = words[1];
					dict.put(key.toLowerCase(), value);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getWord(String spanishWord) {
		return dict.get(spanishWord.toLowerCase());
	}

}
