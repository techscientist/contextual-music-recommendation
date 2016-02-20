package twitterData.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import persistence.TagDatasetManager;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterData.extraction.TwitterConnection;

/** Classe che gestisce la creazione, dato un utente, della mappa 
 * che associa ad ogni tag di Last.fm usato da quell'utente 
 * la frequenza con cui quel tag è usato nei tweet di quell'utente. 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class UserTerm2FrequencyMapGenerator {

	/**
	 * Genera la mappa termine-frequenza dell'utente.
	 * In particolare:
	 * <br> ogni occorrenza di un termine nei tweet dell'utente incrementa il valore di frequenza di 1
	 * <br> ogni occorrenza di un termine nella description dell'utente incrementa il valore di frequenza di 5
	 * 
	 * @param currentUserId id Twitter dell'utente corrente
	 * @return la mappa termine-frequenza, come HashMap<String, Integer>
	 * 
	 * @exception TwitterException
	 * @exception IOException
	 */
	public static HashMap<String, Integer> generateTerm2FrequencyMap(long currentUserId) throws TwitterException, IOException {
		Twitter twitter = TwitterConnection.connect();
		
		ArrayList<String> tweets = getLastTweets(twitter, currentUserId);
		List<String> lastFmTags = TagDatasetManager.getTagStringList();
		String userDescription = twitter.showUser(currentUserId).getDescription();
		
		
		/*Inizializzo la mappa term2frequency
		 * inserendo per ogni tag di last.fm una entry (tag, 0)*/
		HashMap<String, Integer> term2frequency = new HashMap<String, Integer>(lastFmTags.size());
		for(String tag: lastFmTags) {
			term2frequency.put(tag, 0);
		}
			
		/*aggiorno la mappa in base ai tweet dell'utente*/
		for(String tweet : tweets) {
			for(String tag : lastFmTags) {
				/*considero gli underscore usati per i tag come spazi
				 * (nel dataset i tag sono rappresentati con le parole separate da spazi)
				 * e porto tutto il tweet a lettere minuscole*/
				if(tweet.replaceAll("_", " ").toLowerCase().contains(tag))
					term2frequency.put(tag, term2frequency.get(tag) + 1);
			}
		}
		
		/*aggiorno la mappa in base alla description dell'utente
		 * (nota: i termini della description hanno un peso maggiore rispetto a quelli dei tweet)*/
		for(String tag : lastFmTags) {
			if(userDescription.replaceAll("_", " ").toLowerCase().contains(tag))
				term2frequency.put(tag, term2frequency.get(tag) + 5);
		}
		return term2frequency;
	}

	/**
	 * metodo ausiliario per ottenere gli ultimi 100 tweets di un utente,
	 * rappresentati ciascuno attraverso la sola String del testo
	 *
	 * @param twitter punto di accesso ai servizi di twitter
	 * @param currentUserId id Twitter dell'utente corrente
	 * @return la lista di tweets dell'utente
	 * 
	 * @exception TwitterException
	 */
	public static ArrayList<String> getLastTweets(Twitter twitter, long currentUserId) throws TwitterException {
		/*lista dei Tweet scritti dall'utente*/
		ArrayList<String> lastTweets = new ArrayList<String>();

		/*timeline degli status dell'utente, in una pagina da 100 status massimi*/
		Paging paging = new Paging(1, 100);
		ResponseList<Status> userStatusResponseList = twitter.getUserTimeline(currentUserId, paging);
		/*inserisce il testo di ogni tweet nella lista */
		for(Status s : userStatusResponseList) {
			lastTweets.add(s.getText());
		}
		return lastTweets;
	}
}
