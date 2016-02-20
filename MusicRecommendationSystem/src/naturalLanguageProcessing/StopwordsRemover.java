package naturalLanguageProcessing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

/*  Usando Lucene, data una stringa di input effettua tokening e stopping, 
 * e restituisce un'arrayList con i tokens filtrati ricavati*/
public class StopwordsRemover {
	
	
	public ArrayList<String> tokenizeAndRemoveStopwords(String inputText) throws IOException{
		
		/*VERIFICA SE FUNZIONA SUL MAIN*/
		inputText = inputText.replaceAll("https?://\\S+\\s?", "");

		ArrayList<String> tokens = new ArrayList<String>() ;

		StandardAnalyzer analyzer = new StandardAnalyzer();
		TokenStream stream  = analyzer.tokenStream(null, new StringReader(inputText));
		CharArraySet stopWords = generateStopwordsCorpus();
		
	    TokenStream filteredStream = new StopFilter(stream, stopWords);
	    stream.reset();
		while (filteredStream.incrementToken()) {
			
			String token = filteredStream.getAttribute(CharTermAttribute.class).toString();
			
				
			if(token.length() >= 3) {
				tokens.add(token);
			}
		}
		analyzer.close(); 
		filteredStream.close();
		return tokens;
	}	

	/*Crea un set di stopwords a partire da un corpus di default, già contenente:
	 * 	 * "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it",
		 * "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these",
		 * "they", "this", "to", "was", "will", "with"*/
	private CharArraySet generateStopwordsCorpus() {
		CharArraySet stopWords = CharArraySet.copy(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

		stopWords.add("i'm");	 		stopWords.add("i've");
	    stopWords.add("you're"); 	    stopWords.add("you've");
	    stopWords.add("he's");	 		stopWords.add("she's");		stopWords.add("it's");
	    stopWords.add("we're"); 	    stopWords.add("we've");
	    stopWords.add("they're"); 	    stopWords.add("they've");

	    return stopWords;
	}
}
