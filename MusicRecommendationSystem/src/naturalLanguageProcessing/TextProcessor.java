package naturalLanguageProcessing;

import java.io.IOException;
import java.util.ArrayList;

public class TextProcessor {
	
	public ArrayList<String> processString (String input) throws IOException {
		ArrayList<String> tokens = new StopwordsRemover().tokenizeAndRemoveStopwords(input);
		ArrayList<String> stems = new Stemmer().porterStem(tokens);
		return stems;
	}
}
