package persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Classe che si occupa di mediare l'interazione con il database 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class UserPersistence {
	
	/**
	 * Estrae le informazioni di base da un user di Twitter e le usa per creare un basic user;
	 * quindi apre una connessione con il DB di Mongo e registra il basic user nella collezione opportuna:
	 * infine chiude la connessione.
	 * 
	 * @exception TwitterException
	 * @exception IOException
	 * @param currentUser utente di Twitter da cui formare un basic user
	 */
	public static void storeBasicUser(User currentUser) throws TwitterException, IOException {
		
		MongoDBManager mongo = new MongoDBManager();
		Document basicUser = new Document();
		basicUser.append("info", new BasicDBObject("name", currentUser.getName())
											.append("screen name", currentUser.getScreenName())
											.append("id", currentUser.getId()));
		mongo.storeBasicUser(basicUser);
		mongo.closeConnection();
	}
	
	/**
	 * Apre una connessione con mongoDB,
	 * recupera tutti i basic user dal database, 
	 * li trascrive in una lista,
	 * quindi chiude la connessione.
	 * 
	 * @return l'insieme dei basic user sotto forma di ArrayList
	 */
	public static ArrayList<Document> getAllBasicUsers() {
		MongoDBManager mongo = new MongoDBManager();
		Iterable<Document> iterable = mongo.getAllBasicUsers();
		Iterator<Document> iterator = iterable.iterator();
		
		ArrayList<Document> allBasicUsers = new ArrayList<Document>();
		while(iterator.hasNext()) {
			allBasicUsers.add(iterator.next());
		}
		mongo.closeConnection();
		return allBasicUsers;
	}
	
	/**
	 * Apre una connessione con MongoDB,
	 * registra un intermediate user nella collezione opportuna
	 * e infine chiude la connessione..
	 * 
	 * @param l'intermediate user da registrare nel database
	 */
	public static void storeIntermediateUser(Document intermediateUser) throws TwitterException, IOException {
		MongoDBManager mongo = new MongoDBManager();
		mongo.storeIntermediateUser(intermediateUser);	
		mongo.closeConnection();
	}
	
	/**
	 * Apre una connessione con mongoDB,
	 * recupera tutti gli intermediate user dal database, 
	 * li trascrive in una lista,
	 * quindi chiude la connessione.
	 * 
	 * @return l'insieme degli intermediate user sotto forma di ArrayList
	 */
	public static ArrayList<Document> getAllIntermediateUsers() {
		MongoDBManager mongo = new MongoDBManager();
		
		Iterable<Document> iterable = mongo.getAllIntermediateUsers();
		Iterator<Document> iterator = iterable.iterator();
		
		ArrayList<Document> allIntermediateUsers = new ArrayList<Document>();
		while(iterator.hasNext()) {
			Document intermediateUser = iterator.next();
			allIntermediateUsers.add(intermediateUser);
		}
		mongo.closeConnection();
		return allIntermediateUsers;
	}

	/**
	 * Apre una connessione con MongoDB,
	 * registra un complete user nella collezione opportuna
	 * e infine chiude la connessione..
	 * 
	 * @param il complete user da registrare nel database
	 */

	public static void storeCompleteUser(Document completeUser) throws TwitterException, IOException {
		MongoDBManager mongo = new MongoDBManager();
		mongo.storeCompleteUser(completeUser);	
		mongo.closeConnection();
	}

	/**
	 * Apre una connessione con mongoDB,
	 * recupera tutti i complete user dal database, 
	 * li trascrive in una lista,
	 * quindi chiude la connessione.
	 * 
	 * @return l'insieme dei complete user sotto forma di ArrayList
	 */
	public static ArrayList<Document> getAllCompleteUsers() {
		MongoDBManager mongo = new MongoDBManager();
		
		Iterable<Document> iterable = mongo.getAllCompleteUsers();
		Iterator<Document> iterator = iterable.iterator();
		
		ArrayList<Document> allCompleteUsers = new ArrayList<Document>();
		while(iterator.hasNext()) {
			Document completeUser = iterator.next();
			allCompleteUsers.add(completeUser);
		}
		mongo.closeConnection();
		return allCompleteUsers;
	}

}