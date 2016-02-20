package persistence;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/**
 * Classe che si occupa dell'interazione diretta con il database e le collezioni MongoDB 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class MongoDBManager  {

	public final String PROVA_DB = "test";
	public final String PROVA_COLLECTION = "testCollection";

	public final String DB_NAME = "TwitterData";
	public final String TWITTER_BASIC_USERS = "TwitterBasicUsers";
	public final String TWITTER_INTERMEDIATE_USERS = "TwitterIntermediateUsers";
	public final String TWITTER_COMPLETE_USERS = "TwitterCompleteUsers";


	private MongoClient mongoClient;


	/**
	 * crea un MongoDBManager con un MongoClient per interagire con un DB locale
	 * 
	 */
	public MongoDBManager () {
		this.mongoClient = new MongoClient( "localhost" );
	}

	/**
	 * apre manualmente un nuovo MongoClient per interagire con un DB locale
	 * 
	 */
	public void openConnection() {
		this.mongoClient = new MongoClient("localhost");
	}

	/**
	 * chiude manualmente il MongoClient corrente
	 * 
	 */
	public void closeConnection() {
		this.mongoClient.close();
	}


	/**
	 *ottiene dal server di Mongo un DB in base al nome, se esiste;
	 * se non esiste nessun DB con quel nome ne crea uno nuovo.
	 * 
	 * @param name nome del DB
	 * @return un oggetto che rappresenta il punto di accesso al database cercato
	 */
	public MongoDatabase getDatabase(String name) {
		MongoDatabase db = this.mongoClient.getDatabase(name);
		return db;
	}


	/**
	 *registra nel database un basic user, nella collezione opportuna
	 *
	 *@param basicUser l'utente da registrare nel database
	 */
	public void storeBasicUser(Document basicUser) {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_BASIC_USERS);		
		coll.insertOne(basicUser);
	}

	/**
	 *recupera dal DB tutti i basic users
	 *
	 *@return la collezione dei Document dei basic users, sotto forma di un oggetto Iterable 
	 */
	public Iterable<Document> getAllBasicUsers() {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_BASIC_USERS);
		return coll.find();
	}

	/**
	 *registra nel database un intermediate user, nella collezione opportuna
	 *
	 *@param intermediateUser l'utente da registrare nel database
	 */
	public void storeIntermediateUser(Document intermediateUser) {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_INTERMEDIATE_USERS);
		coll.insertOne(intermediateUser);
	}

	/**
	 *recupera dal DB tutti gli intermediate users
	 *
	 *@return la collezione dei Document degli intermediate users, sotto forma di un oggetto Iterable */
	public Iterable<Document> getAllIntermediateUsers() {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_INTERMEDIATE_USERS);
		return coll.find();
	}

	/**
	 *registra nel database un complete user, nella collezione opportuna
	 *
	 *@param completeUser l'utente da registrare nel database
	 */
	public void storeCompleteUser(Document completeUser) {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_COMPLETE_USERS);
		coll.insertOne(completeUser);
	}


	/**
	 *recupera dal DB tutti i complete users
	 *
	 *@return la collezione dei Document dei complete users, sotto forma di un oggetto Iterable */
	public Iterable<Document> getAllCompleteUsers() {
		MongoDatabase database = getDatabase(DB_NAME);
		MongoCollection<Document> coll = database.getCollection(TWITTER_COMPLETE_USERS);
		return coll.find();
	}




	/*inserisce un documento nel DB di prova*/
	public void storeTestStuff(Document document) {
		MongoDatabase database = getDatabase(PROVA_DB);
		MongoCollection<Document> coll = database.getCollection(PROVA_COLLECTION);
		coll.insertOne(document);
	}

	/*recupera tutti i documenti dal DB di prova*/
	public FindIterable<Document> retrieveTestStuff() {
		MongoDatabase database = getDatabase(PROVA_DB);
		FindIterable<Document> iterable = database.getCollection(PROVA_COLLECTION).find();
		return iterable;
	}
}
