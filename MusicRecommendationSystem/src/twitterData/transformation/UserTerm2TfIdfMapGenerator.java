package twitterData.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import twitter4j.TwitterException;

 /** Classe che gestisce la creazione, dato un utente, della mappa 
 * che associa ad ogni tag di Last.fm usato da quell'utente 
 * la pesatura TF-IDF con cui quel tag è usato nei tweet di quell'utente,
 * calcolata dalle frequenze di utilizzo di quel tag e degli altri tag 
 * da quell'utente e dagli altri utenti. 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class UserTerm2TfIdfMapGenerator {
	
	/**
	 * Genera la mappa termine-peso tf-idf relativa all'utente.

	 * @param currentUser utente corrente, nella versione Intermediate User
	 * @param allIntermediateUsers lista di tutti gli Intermediate Users nel sistema
	 * @return la mappa termine-tf-idf, come HashMap<String, Double>
	 * 
	 */
	public static HashMap<String, Double> generateUserTerm2TfIdfMap(ArrayList<Document> allIntermediateUsers, Document currentUser) {
		//
		Map<String, Integer> userTerm2frequency = (Map)currentUser.get("term2frequency");
		//
		HashMap<String, Double> term2tfidf = new HashMap<String, Double>(userTerm2frequency.size());

		Integer[] userFrequencyVector = userTerm2frequency.values().toArray(new Integer[userTerm2frequency.size()]);
		int sum = 0;
		for(Integer i : userFrequencyVector) {
			sum += userFrequencyVector[i];
		}
		
		ArrayList<Integer[]> allUsersFrequencyVectors = getAllUsersFrequencyVectors(allIntermediateUsers);
		
		int i = 0;
		for(String term : userTerm2frequency.keySet()) {
			/*lavora sul termine i-esimo*/			
			Double tfIdfValue = 0.0;
			if((Integer)userTerm2frequency.get(term) > 0) {
				tfIdfValue = calculateSingleTermTfIdf(i, userFrequencyVector, allUsersFrequencyVectors);
			}
			term2tfidf.put(term, tfIdfValue);
			i++;
		}
		return term2tfidf;
	}

	
	/**
	 * Metodo ausiliario per il calcolo del peso tf-idf di un termine usato da un utente
	 * 
	 * @param termIndex indice che esprime la posizione del termine nei vettori di chiavi degli utenti  
	 * @param userFrequencyVector vettore che nella posizione i-esima ha il valore di frequenza del tag i-esimo usato dall'utente
	 * @param allUsersFrequencyVectors lista con tutti i vettori di tutti gli utenti
	 * @return pesatura tf-idf dello specifico termine per lo specifico utente
	 * 
	 */
	private static Double calculateSingleTermTfIdf(	Integer termIndex, 
													Integer[] userFrequencyVector, 
													ArrayList<Integer[]> allUsersFrequencyVectors	) {
		
		double tf = calculateTf(termIndex, userFrequencyVector);
		double idf = calculateIdf(termIndex, allUsersFrequencyVectors);
		return tf*idf;
	}
	
	
	

	 /** Metodo ausiliario che effettua il calcolo del peso tf di un termine usato da un utente
	 * 
	 * tf si calcola come numero di volte che il termine viene utilizzato dall'utente 
	 * diviso numero di parole usate dall'utente nei propri tweet
	 * 
	 * @param termIndex indice che esprime la posizione del termine nei vettori di chiavi degli utenti  
	 * @param userFrequencyVector vettore che nella posizione i-esima ha il valore di frequenza del tag i-esimo usato dall'utente
	 * @return peso tf dello specifico termine per lo specifico utente
	 */
	private static Double calculateTf (Integer termIndex, Integer[] userFrequencyVector) {
		double termFrequency = (double)userFrequencyVector[termIndex];
		double frequencySum = 0.0;
		for(Integer i : userFrequencyVector) {
			frequencySum += i;
		}
		double tf = termFrequency/frequencySum;
		return tf;
	}
	
	 /** Metodo ausiliario che effettua il calcolo del peso idf di un termine usato da un utente
	 * 
	 * idf si calcola come logaritmo del numero di utenti totali/utenti che usano il termine in questione 
	 * 
	 * @param termIndex indice che esprime la posizione del termine nei vettori di chiavi degli utenti  
	 * @param allUsersFrequencyVectors lista con tutti i vettori di tutti gli utenti
	 * @return peso idf dello specifico termine per lo specifico utente
	 */
	private static Double calculateIdf (Integer termIndex, ArrayList<Integer[]> allUsersFrequencyVectors) {
		int users = allUsersFrequencyVectors.size();
		int usersUsingTerm = 0;
		for(Integer[] currentUserVector : allUsersFrequencyVectors) {
			if(currentUserVector[termIndex]>0) {
				usersUsingTerm++;
			}
		}
		/*questo caso si può verificare solo se si sta facendo 
		 raccomandazione su un utente specifico non in DB 
		 e che è l'unico ad usare il termine in questione*/
		if(usersUsingTerm == 0)
			usersUsingTerm++;
		
		double idf = Math.log(users/usersUsingTerm);
		return idf;
	}

	
	/** Metodo ausiliario che estrae i vettori delle frequenze dei termini di tutti gli utenti
	 * 
	 * @param allIntermediateUsers lista di tutti gli utenti gestiti dal sistema, in versione intermediate users
	 * @return lista con tutti i vettori delle frequenze degli intermediate users
	 */
	private static ArrayList<Integer[]> getAllUsersFrequencyVectors (ArrayList<Document> allIntermediateUsers) {
		ArrayList<Integer[]> frequencyVectors = new ArrayList<Integer[]>();
		for(Document currentUser : allIntermediateUsers) {
			Document currentUserTerm2frequency = (Document)currentUser.get("term2frequency");
			Integer[] currentUserFrequencyVector = currentUserTerm2frequency.values().toArray(new Integer[currentUserTerm2frequency.size()]);
			frequencyVectors.add(currentUserFrequencyVector);
		}
		return frequencyVectors;
	}
}
