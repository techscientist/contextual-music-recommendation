package twitterData.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.Document;

import persistence.UserPersistence;
import twitter4j.TwitterException;

/**
 * Classe che si occupa di generare i Complete Users a partire dagli Intermediate Users
 * <p>
 * Un complete user è un utente nella sua versione definitiva:
 * gli è stata calcolata e associata anche la mappa termine - peso TfIdf
 * <p> 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class CompleteUsersGenerator {

	/**
	 * lancia l'espansione di tutti gli intermediate users a complete users.
	 * I nuovi complete users saranno registrati in una collezione diversa
	 * da quella di partenza.
	 * 
	 * @exception TwitterException 
	 * @exception IOException
	 */
	public static void updateIntermediateUsersToCompleteUsers() throws TwitterException, IOException {
		ArrayList<Document> intermediateUsers = UserPersistence.getAllIntermediateUsers();
		for (Document d : intermediateUsers) {
			Document info = (Document)d.get("info");
			updateToCompleteAndStore(d);
		}
	}

	/**
	 * esegue l'aggiornamento di un intermediate user a copmlete user, 
	 * e registra il nuovo complete user nel database.
	 * 
	 * @param intermediateUser intermediate user da aggiornare a complete
	 * @exception TwitterException 
	 * @exception IOException
	 */
	public static void updateToCompleteAndStore(Document intermediateUser) throws TwitterException, IOException {

		HashMap<String, Double> userTerm2TfIdfMap = UserTerm2TfIdfMapGenerator.
				generateUserTerm2TfIdfMap(	UserPersistence.getAllIntermediateUsers(),
						intermediateUser);
		intermediateUser.remove("term2frequency");
		Document completeUser = (Document) intermediateUser.append("term2TfIdf", userTerm2TfIdfMap);
		UserPersistence.storeCompleteUser(completeUser);
	}
}
