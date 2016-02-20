package twitterData.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BasicBSONObject;
import org.bson.Document;

import persistence.UserPersistence;
import twitter4j.TwitterException;


/**
 * Classe che si occupa di generare gli Intermediate Users a partire dai Basic Users
 * <p>
 * Un intermediate user è un utente al quale 
 * <br>- è già stato associato un insieme di generi musicali preferiti
 * <br>- è già stata associata la mappa termine - frequenza di quel termine nei suoi tweet,
 * <br>ma non è stata ancora calcolata la mappa termine - peso TfIdf
 * <p> 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class IntermediateUsersGenerator {
	/**
	 * lancia l'espansione di tutti i basic users a intermediate users.
	 * I nuovi intermediate users saranno registrati in una collezione diversa
	 * da quella di partenza.
	 * 
	 * @exception TwitterException 
	 * @exception IOException
	 */
	public static void updateBasicUsersToIntermediateUsers() throws TwitterException, IOException {
		ArrayList<Document> basicUsers = UserPersistence.getAllBasicUsers();
		for (Document d : basicUsers) {
			updateToIntermediateAndStore(d);
		}
	}

	/**
	 * esegue l'aggiornamento di un basic user a intermediate user, 
	 * e registra il nuovo intermediate user nel database.
	 * 
	 * @param user basic user da aggiornare a intermediate
	 * @exception TwitterException 
	 * @exception IOException
	 */
	public static void updateToIntermediateAndStore(Document user) throws TwitterException, IOException {
		user.append("favourite music genres", MusicPreferencesGenerator.generateRandomMusicGenres());

		Document info = (Document)user.get("info");
		long currentUserId = (long)info.get("id");
		HashMap<String, Integer> term2frequency = UserTerm2FrequencyMapGenerator.generateTerm2FrequencyMap(currentUserId);			
		user.append("term2frequency", term2frequency);

		UserPersistence.storeIntermediateUser(user);
		System.out.println((String)info.get("name"));
	}
}