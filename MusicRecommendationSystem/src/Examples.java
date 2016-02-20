import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bson.Document;

import musicData.extraction.TrackData;
import musicData.extraction.LastFMTrackTagsExtractor;
import musicData.extraction.SpotifyTracksExtractor;
import musicData.transformation.TrackTag2WeightMapGenerator;
import naturalLanguageProcessing.UserStem2FrequencyMapGenerator;
import persistence.MongoDBManager;
import persistence.UserPersistence;
import similarity.CosineSimilarityCalculator;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterData.extraction.TwitterConnection;
import twitterData.transformation.MusicPreferencesGenerator;
import twitterData.transformation.UserTerm2FrequencyMapGenerator;
import twitterData.transformation.UserTerm2TfIdfMapGenerator;

/**
 * Classe che conserva una serie di esempi/test effettuati in fase di programmazione.
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class Examples {

	public static void getUserTweets () throws TwitterException, IOException {
		Twitter twitter = TwitterConnection.connect();
		try {
			User user = twitter.showUser("TwitterMusic");
			long id = user.getId();
			UserStem2FrequencyMapGenerator t = new UserStem2FrequencyMapGenerator(twitter);
			ArrayList<String> stems;
			System.out.println("inizio");
			System.out.println(" ");

			System.out.println(user.getDescription());
			stems = t.getDescriptionStems(id);
			for(String s:stems) {
				System.out.println(s);
			}
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");

			stems = t.getAllTweetsStems(id);
			for(String s:stems) {
				System.out.println(s);
			}
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");

			HashMap<String, Integer> map = t.generateStem2FrequencyMap(id);
			for(String s: map.keySet()) {
				System.out.print(s + " ");
				System.out.println(map.get(s));
			}
		} finally {}
	}

	public static void getTags() {
		LastFMTrackTagsExtractor tracksExtractor = new LastFMTrackTagsExtractor();
		HashMap<String, Integer> tag2weight = tracksExtractor.extractTrackTags("Bohemian Rapsody", "Queen");
		 System.out.println(tag2weight);
	}

	
	public static void getTag2popularityMap() {
		
		HashMap<String, Double> mappa = TrackTag2WeightMapGenerator.generateTag2Popularity("Bohemian Rapsody", "Queen");
		for(String s : mappa.keySet()) {
			if (mappa.get(s) > 0)
				System.out.println(s + " " + mappa.get(s));
		}
	}

	public static void getTracksByGenres() {
		SpotifyTracksExtractor ste = new SpotifyTracksExtractor();
		ArrayList<String> genres = new ArrayList<String>();
		genres.add("pop");
		genres.add("rock");
		genres.add("jazz");

		ArrayList<TrackData> data = ste.extractTracksData(genres);

		for(TrackData td : data) {
			System.out.println(td.getArtistName());
			System.out.println(td.getTitle());
			System.out.println();

		}
	}

	public static void storeInDB() throws TwitterException {

		Twitter twitter = TwitterConnection.connect();
		User currentUser = twitter.showUser("TwitterMusic");

		Document basicUser = new Document();
		basicUser.append("info", new Document("name", currentUser.getName())
				.append("screen name", currentUser.getScreenName())
				.append("id", currentUser.getId()));
		
		MongoDBManager mongo = new MongoDBManager();
		mongo.storeTestStuff(basicUser);
	}

	public static void retrieveFromDB() {
		MongoDBManager mongo = new MongoDBManager();
		Iterable<Document> iterable = mongo.retrieveTestStuff();
		Iterator<Document> iterator = iterable.iterator();

		Document object1 = iterator.next();
		Document object2 = iterator.next();

		String nome = (String)object1.get("nome");
		System.out.println(nome);
		Document info = (Document) object2.get("info");
		String user = (String) info.get("name");
		System.out.println(user);
	}


	public static void readTagsFromDataset() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(new File("datasets/tags.dat")));
			String available;
			bufferReader.readLine();
			while((available = bufferReader.readLine()) != null) {

				String arr[] = available.split("\t", 2);

				System.out.println(Integer.parseInt((String)arr[0].trim()));
				System.out.println(arr[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void extractUserTweets() throws TwitterException{

		Twitter twitter = TwitterConnection.connect();
		User user = twitter.showUser("joe_rinna");
		long currentUserId = user.getId();
		/*utente di cui considerare i tweet*/
		User currentUser = twitter.showUser(currentUserId);

		/*lista dei Tweet scritti dall'utente*/
		ArrayList<String> lastTweets = new ArrayList<String>();

		/*se l'utente ha scritto più di 100 tweets, comunque considero solo gli ultimi 100*/
		int validTweetsNumber = Math.min(100, currentUser.getStatusesCount());

		/*timeline degli status dell'utente, in una pagina da 100 status massimi*/
		Paging paging = new Paging(1, 100);
		ResponseList<Status> userStatusResponseList = twitter.getUserTimeline(currentUserId, paging);
		/*inserisce il testo di ogni tweet nella lista */
		for(Status s : userStatusResponseList) {
			lastTweets.add(s.getText());
		}

		for(String s : lastTweets)
			System.out.println(s);
	}

	public static void generateTerm2FrequencyMap() throws TwitterException {
		Twitter twitter = TwitterConnection.connect();
		User user = twitter.showUser("TwitterMusic");
		long currentUserId = user.getId();

		try {
			HashMap<String, Integer> mappa = UserTerm2FrequencyMapGenerator.generateTerm2FrequencyMap(currentUserId);
			for(String s : mappa.keySet()) {
				System.out.print(s+ " ");
				System.out.println(mappa.get(s));
			}
		} catch (TwitterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void generateIntermediateUserFromBasic() throws TwitterException, IOException {
		ArrayList<Document> basicUsers = UserPersistence.getAllBasicUsers();
		Document user = basicUsers.get(0);

		System.out.println("lavoro all'utente " + user);

		user.append("favourite music genres", MusicPreferencesGenerator.generateRandomMusicGenres());

		Document info = (Document)user.get("info");

		long currentUserId = (long)info.get("id");
		HashMap<String, Integer> term2frequency = UserTerm2FrequencyMapGenerator.generateTerm2FrequencyMap(currentUserId);
		user.append("term2frequency", term2frequency);

		System.out.println(user);
		System.out.println(term2frequency.containsValue(1));
	}





	private static void generateAllIntermediateUsersFromBasicUsers() throws TwitterException, IOException {
		ArrayList<Document> basicUsers = UserPersistence.getAllBasicUsers();

		for (Document user : basicUsers) {
			System.out.println("lavoro all'utente " + user);

			user.append("favourite music genres", MusicPreferencesGenerator.generateRandomMusicGenres());

			Document info = (Document)user.get("info");

			long currentUserId = (long)info.get("id");
			HashMap<String, Integer> term2frequency = UserTerm2FrequencyMapGenerator.generateTerm2FrequencyMap(currentUserId);
			user.append("term2frequency", term2frequency);

			System.out.println(user);
		}
	}

	private static void generateCompleteUserFromIntermediateUser() throws TwitterException, IOException {
		ArrayList<Document> intermediateUsers = UserPersistence.getAllBasicUsers();
		Document user = intermediateUsers.get(0);

		System.out.println("lavoro all'utente " + user);
		HashMap<String, Double> term2TfIdf = UserTerm2TfIdfMapGenerator.generateUserTerm2TfIdfMap(intermediateUsers, user);

		user.remove("term2frequency");
		user.append("term2TfIdf", term2TfIdf);

		System.out.println(user);
	}


	public static void generateTfIdfMap() throws TwitterException, IOException {
		Document intermediateUser = UserPersistence.getAllIntermediateUsers().get(0);
		HashMap<String, Double> hashmap = UserTerm2TfIdfMapGenerator.generateUserTerm2TfIdfMap(UserPersistence.getAllIntermediateUsers(), intermediateUser);

		for(String s : hashmap.keySet()) {
			if(hashmap.get(s)>0)
				System.out.println(s + " " + hashmap.get(s));
		}
	}

	public static void generateCompleteUser() {
		Document intermediateUser = UserPersistence.getAllIntermediateUsers().get(0);
		HashMap<String, Double> userTerm2TfIdfMap = UserTerm2TfIdfMapGenerator.generateUserTerm2TfIdfMap(UserPersistence.getAllIntermediateUsers(), intermediateUser);
		intermediateUser.remove("term2frequency");
		Document completeUser = (Document) intermediateUser.append("term2TfIdf", userTerm2TfIdfMap);
		System.out.println(completeUser);
	}
	
	
	
	public static void weird() {
		Document intermediateUser = UserPersistence.getAllIntermediateUsers().get(55);
		HashMap<String, Double> userTerm2TfIdfMap = UserTerm2TfIdfMapGenerator.generateUserTerm2TfIdfMap(UserPersistence.getAllIntermediateUsers(), intermediateUser);
		intermediateUser.remove("term2frequency");
		Document completeUser = (Document) intermediateUser.append("term2TfIdf", userTerm2TfIdfMap);
		System.out.println(completeUser);
	}
	
	
	public static Double cosineSimilarity() {
		Double[] vector1 = {3.0, 0.0, 0.0, 4.0, 5.0};
		Double[] vector2 = {0.0, 0.0, 1.0, 0.0, 0.0};
		
		System.out.println(CosineSimilarityCalculator.compute(vector1, vector2));
		return CosineSimilarityCalculator.compute(vector1, vector2);

	}

	public static void userTrackSimilarity() {
		Document user = UserPersistence.getAllCompleteUsers().get(4);
		Document term2TfIdf = (Document) user.get("term2TfIdf");
		Double[] userVector = term2TfIdf.values().toArray(new Double[term2TfIdf.size()]);
		
		HashMap<String, Double> tag2popularity = TrackTag2WeightMapGenerator.generateTag2Popularity("Bohemian Rapsody", "Queen");
		Double[] trackVector = tag2popularity.values().toArray(new Double[tag2popularity.size()]);
		System.out.println(CosineSimilarityCalculator.compute(userVector, trackVector));
	}


}

