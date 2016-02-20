package twitterData.extraction;

import java.io.IOException;

import persistence.UserPersistence;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Classe che si occupa dell'estrazione degli utenti da Twitter
 * e della creazione di basic users a partire dalle loro informazioni anagrafiche
 *  
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class BasicUserExtractor {

	/**
	 * Estrae gli utenti da Twitter (a intervalli temporali tali da evitare 
	 * di superare il rate limit di Twitter),
	 * ne valuta la validità
	 * e in caso ne effettua la memorizzazione ocme basic user in database. 
	 * Si ferma dopo aver memorizzato 100 basic users.
	 * 
	 * @exception TwitterException
	 * @exception IOException
	 */
	public static void extractUsers() throws TwitterException, IOException {
		Twitter twitter = TwitterConnection.connect();

		/*assumo che gli utenti che seguono TwitterMusic siano appassionati di musica*/
		User twitterMusic = twitter.showUser("@TwitterMusic");
		System.out.println(twitterMusic.getName());
		System.out.println(twitterMusic.getDescription());

		/*Ottengo da twitter la lista degli ID dei followers di TwitterMusic;
		 * Sono organizzati in pagine di 5000 da sfogliare con un meccanismo di cursori */
		long nextCursor = -1;
		IDs ids = twitter.getFollowersIDs("@TwitterMusic", nextCursor);
		long[] IDsArray = ids.getIDs();
		int validExtractedUsers = 0;	//non ho ancora estratto alcun utente
		int visitedUsers = 0;			//non ho ancora visitato alcun utente

		/*Sfoglio l'array di ID valutando per ogni utente se è valido o no;
		 * se un utente è valido ne registro le informazioni nel database.
		 * Procedo fino ad ottenere 100 utenti validi;*/
		while(validExtractedUsers < 100) {
			long id = IDsArray[visitedUsers];
			User currentUser = twitter.showUser(id);
			if (isValid(currentUser)) {
				UserPersistence.storeBasicUser(currentUser);
				validExtractedUsers ++;
			}
			visitedUsers++;

			System.out.println(visitedUsers);
			System.out.println(validExtractedUsers);
			System.out.println(" ");

			/*se la pagina da 5000 utenti è finita,
			 * passo alla pagina successiva*/
			if(visitedUsers==IDsArray.length) {
				nextCursor = ids.getNextCursor();	
				ids = twitter.getFollowersIDs("@TwitterMusic", nextCursor);
				IDsArray = ids.getIDs();
				visitedUsers = 0;
			}

			/*Per evitare di mandare in flooding Twitter,	
			 * faccio 150 estrazioni ogni 15 minuti (il massimo sarebbe 180)
			 * vale a dire un'estrazione ogni 6 secondi */
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	

	/**
	 * metodo ausiliario che valuta se un utente Twitter è valido.
	 * <p>
	 * Un utente Twitter è valido se:
	 * <br> - consente la lettura dei suoi status e della sua descrizione 
	 * <br> - è rilevante (sulla base del numero di tweets che ha scritto)
	 * <br> - è un individuo e non un'organizzazione (confrontando il numero di follower con quello di following) 
	 * <br>	- è di lingua inglese (sulla base della sua descrizione
	 * Se rispetta questi criteri, allora l'utente si considera valido
	 * <p>
	 *@param user utente Twitter di cui valutare la validità
	 *@return true se user è valido, false altrimenti
	 */
	private static boolean isValid(User user) {
		boolean isValid = true;

		if(user.getStatus() == null || user.getDescription() == null)
			isValid = false;

		/*Se l'utente ha scritto troppi pochi tweets, non andrebbe considerato*/
		if (user.getStatusesCount() < 20)
			isValid = false;

		/*Se l'utente è seguito da più persone (followers) 
		 * rispetto alle persone che segue (following/friends), 
		 * probabilmente è un'organizzazione o un VIP, quindi non andrebbe considerato*/
		if (user.getFollowersCount() > user.getFriendsCount())
			isValid = false;

		/*Se l'utente non è di lingua inglese, non andrebbe considerato*/
		if (!user.getLang().equals("en")) {
			isValid = false;
		}
		return isValid;
	}
}
