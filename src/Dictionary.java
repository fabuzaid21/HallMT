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
				dict.put(words[0], words[1]);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getWord(String spanishWord) {
		return dict.get(spanishWord);
	}

}
