package naturalLanguageProcessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class UserStem2FrequencyMapGenerator {
	Twitter twitter;
	TextProcessor textProcessor;
	
	public UserStem2FrequencyMapGenerator(Twitter twitter) {
		this.twitter = twitter;
		this.textProcessor = new TextProcessor();
	}

	public HashMap<String, Integer> generateStem2FrequencyMap(long currentUserId) throws TwitterException, IOException {
		
		HashMap<String, Integer> stem2frequencyMap = new HashMap<String, Integer>();
		
		/*ottengo tutti gli stems degli ultimi 100 tweets dell'utente*/
		ArrayList<String> tweetsStems = this.getAllTweetsStems(currentUserId);
		/*ottengo tutti gli stems dal testo della descrizione dell'utente*/
		ArrayList<String> descriptionStems = this.getDescriptionStems(currentUserId);

		/*aggiorno la mappa*/
		updateStem2FrequencyMapUsingTweetsStems (stem2frequencyMap, tweetsStems);
		updateStem2FrequencyMapUsingDescriptionStems (stem2frequencyMap, descriptionStems);
		
		return stem2frequencyMap;
	}
	
	/* Data la mappa stem2frequency di un utente (già esistente, eventualmente vuota)
	 * e data la lista di stems estratti dalla descrizione dell'utente (ottenuta da getDescriptionStems)
	 * aggiorna la mappa stem2frequency
	 * 
	 * Ho assegnato peso 5 ad ogni stem derivante dalla descrizione, 
	 * perché la descrizione è molto più rilevante rispetto ai tweet*/
	public void updateStem2FrequencyMapUsingDescriptionStems(HashMap<String, Integer> stem2frequency, 
				 											ArrayList<String> descriptionStems) throws IOException {
		for(String stem : descriptionStems) {
			if(stem2frequency.containsKey(stem)) {
				stem2frequency.put(stem, stem2frequency.get(stem) + 5);
			}
			else {
				stem2frequency.put(stem, 1);
			}
		}
	}


	/* Data la mappa stem2frequency di un utente (già esistente, eventualmente vuota)
	 * e data la lista di stems relativa a tutti i tweet dell'utente (ottenuta da getAllTweetsStems)
	 * aggiorna la mappa stem2frequency*/
	public void updateStem2FrequencyMapUsingTweetsStems(HashMap<String, Integer> stem2frequency, 
			 											ArrayList<String> tweetsStems) {
		for(String stem : tweetsStems) {
			if(stem2frequency.containsKey(stem)) {
				stem2frequency.put(stem, stem2frequency.get(stem) + 1);
			}
			else {
				stem2frequency.put(stem, 1);
			}
		}
	}	


	
	/*a partire dalla lista di tweets dell'utente, 
	 * restituisce una lista gli stems ottenuti da tutti i tweet*/
	public ArrayList<String> getAllTweetsStems(long currentUserId) throws IOException, TwitterException {
		ArrayList <String> tweets = this.getTweets(currentUserId);	
		ArrayList <String> allTweetsStems = new ArrayList<String>();	

		for(String tweet : tweets) {
			ArrayList<String> tweetStems = this.textProcessor.processString(tweet);
			for(String tweetStem : tweetStems) {
				allTweetsStems.add(tweetStem);
			}
		}
		return allTweetsStems;
	}

	
	/*ottiene la lista di tutti degli status di un utente, 
	 * ne estrae i testi, ne ottiene gli stems 
	 * e li restituisce in una ArrayList<String> */
	public ArrayList<String> getTweets (long currentUserId) throws TwitterException {
		ArrayList<String> tweets = new ArrayList<String>();

		Paging paging = new Paging();
		ResponseList<Status> currentUserTimeLine = this.twitter.getUserTimeline(currentUserId, paging);

		for(Status status:currentUserTimeLine) {
			tweets.add(status.getText());
		}

		return tweets;
	}

	/*ottiene la descrizione di un utente e ne estrae gli stems*/
	public ArrayList<String> getDescriptionStems (long currentUserId) throws TwitterException, IOException {
		String description = this.twitter.showUser(currentUserId).getDescription();
		ArrayList<String> stems = this.textProcessor.processString(description);
		return stems;
	}
}
