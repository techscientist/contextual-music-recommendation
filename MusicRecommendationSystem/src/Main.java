import java.io.IOException;

import twitter4j.TwitterException;
import twitterData.extraction.BasicUserExtractor;
import twitterData.transformation.CompleteUsersGenerator;
import twitterData.transformation.IntermediateUsersGenerator;

public class Main {
	public static void main (String[] args) throws TwitterException, IOException {
		
		/*METODI PER OTTENERE/TRASFORMARE I PROFILI UTENTE  */
		//extractAndStoreBasicUsers();
		//createAndStoreIntermediateUsers();
		//createAndStoreCompleteUsers();
		
		/*ESECUZIONE DELLA RACCOMANDAZIONE (due modalità)*/
		//RecommendationSystem.randomUserRecommendation();
		RecommendationSystem.specificUserRecommendation("AndreaRossi516");
	}

	/**
	 * Estrae 100 utenti rilevanti da Twitter e ne registra
	 * le informazioni basilari in una collection di un database MongoDB
	 */
	private static void extractAndStoreBasicUsers() {
		try {
			BasicUserExtractor.extractUsers();
		} catch (TwitterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Recupera tutti i BasicUser dal database, ne estrae e processa i tweet,
	 * ne ricava la mappa term2frequency e li trasforma in IntermediateUsers;
	 * quindi li registra in una collezione apposita del database MongoDB
	 */
	private static void createAndStoreIntermediateUsers() {
		try {
			IntermediateUsersGenerator.updateBasicUsersToIntermediateUsers();
		} catch (TwitterException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recupera tutti gli IntermediateUser dal database, ne incrocia e processa i dati,
	 * ne ricava la mappa term2tfIdf e li trasforma in CompleteUsers;
	 * quindi li registra in una collezione apposita del database MongoDB
	 */
	private static void createAndStoreCompleteUsers() {
			try {
				CompleteUsersGenerator.updateIntermediateUsersToCompleteUsers();
			} catch (TwitterException | IOException e) {
				e.printStackTrace();
			}
	}
}