import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bson.Document;

import music.MusicGenres;
import musicData.extraction.SpotifyTracksExtractor;
import musicData.extraction.TrackData;
import persistence.UserPersistence;
import similarity.UserTrackSimilarity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterData.extraction.TwitterConnection;
import twitterData.transformation.UserTerm2FrequencyMapGenerator;
import twitterData.transformation.UserTerm2TfIdfMapGenerator;

/**
 * Classe che gestisce dell'esecuzione complessiva del Recommendation System* 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class RecommendationSystem {

	/**
	 * Genera una raccomandazione per un utente random estratto dal database dei completeuser,
	 * e stampa a video le informazioni sui brani consigliati (titolo, artista, coseno-similarità) 
	 */
	public static void randomUserRecommendation() {

		System.out.println("Benvenuto nel sistema di raccomandazione musicale contestuale.");
		int userIndex = new Random().nextInt(100);
		Document completeUser = UserPersistence.getAllCompleteUsers().get(userIndex);

		Document info = (Document) completeUser.get("info");
		ArrayList<String> favouriteGenres = (ArrayList<String>)completeUser.get("favourite music genres");

		System.out.println("Raccomandazione per l'utente " + (String)info.get("name"));
		System.out.println("Generi musicali preferiti: " + favouriteGenres);
		System.out.println();

		recommendTracks(favouriteGenres, completeUser);

	}

	/**
	 * Genera una raccomandazione per uno specifico utente Twitter 
	 * (non necessariamente facente parte del campione di 100 utenti in database),
	 * e stampa a video le informazioni sui brani consigliati (titolo, artista, coseno-similarità).
	 *
	 *@param screenName screen name dell'accout twitter dell'utente a cui consigliare brani  
	 */
	public static void specificUserRecommendation(String screenName) throws TwitterException, IOException {
		System.out.println("Benvenuto nel sistema di raccomandazione musicale contestuale.");
		ArrayList<String> favouriteGenres = askFavouriteMusicGenres();
		Document completeUser = extractUserData(screenName);
		ArrayList<Entry<TrackData, Double>> track2similarityList = recommendTracks(favouriteGenres, completeUser);

		System.out.println("Brani consigliati:");

		for(int i = 0; i<10; i++) {
			Map.Entry<TrackData, Double> entry = track2similarityList.get(i);
			String artist = entry.getKey().getArtistName();
			String title = entry.getKey().getTitle();
			Double value = (Double)entry.getValue();

			System.out.println(title);
			System.out.println(artist);
			System.out.println(value);
			System.out.println();
		}		

	}



	/**
	 * Metodo ausiliario che, si occupa di chiedere all'utente che interagisce con il sistema
	 * quali siano i suoi generi musicali preferiti e di registrarne le risposte
	 *@return i generi musicali preferiti dall'utente, in un ArrayList di String
	 */
	private static ArrayList<String> askFavouriteMusicGenres() {

		/*acquisizione del primo genere musicale: obbligatoria*/
		System.out.println("Inserisci il tuo genere musicale preferito, per favore.");
		System.out.println("Puoi scegliere tra pop, rock, indie, hiphop, r&b, soul, metal, country, jazz, classical");

		ArrayList<String> favouriteGenres =  new ArrayList<String>();
		String[] musicGenres = MusicGenres.getGenresArray();

		try{
			boolean availableGenre = false;
			while (!availableGenre) {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine();

				for(int i = 0; i < musicGenres.length && !availableGenre; i++) {
					if (s.equals(musicGenres[i])) {
						availableGenre = true;
						favouriteGenres.add(s);
					}
				}
				if (availableGenre == false) {
					System.out.println("Genere non corretto");
					System.out.println("Per favore, inserisci un genere musicale corretto");
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		/*acquisizione di altri generi musicali: opzionale*/
		boolean terminato = false;
		while(!terminato) {
			System.out.println("Vuoi inserire un altro genere musicale preferito? (y/n)");
			try{
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine();
				while(!s.equals("y") && !s.equals("n")) {
					System.out.println("Input non riconosciuto");
					System.out.println("Per favore, digita y se vuoi indicare dei generi musicali, o n se non vuoi inserirne altri");
					bufferRead = new BufferedReader(new InputStreamReader(System.in));
					s = bufferRead.readLine();
				} 
				if(s.equals("n")) {
					terminato = true;
				}
				else {
					System.out.println("Inserisci il nuovo genere musicale, prego");

					bufferRead = new BufferedReader(new InputStreamReader(System.in));
					s = bufferRead.readLine();

					if(favouriteGenres.contains(s)) {
						System.out.println("Hai già inserito questo genere musicale");
					} 
					else {
						boolean availableGenre = false;
						for(int i = 0; i < musicGenres.length && !availableGenre; i++) {
							if (s.equals(musicGenres[i])) {
								availableGenre = true;
								favouriteGenres.add(s);
							}
						}
						if (availableGenre == false) {
							System.out.println("Genere non corretto");
						}
					}

				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		System.out.print("Ricapitolando, i tuoi generi musicali preferiti sono:" );
		for (String s : favouriteGenres) {
			System.out.print(" " + s + " ");
		}
		System.out.println("");
		return favouriteGenres;
	}



	/**
	 * Metodo ausiliario che, dato l'id dell'utente, ne estrae le informazioni anagrafiche e i tweet, 
	 * le processa e costruisce un profilo analogo a un completeUser
	 *@param id Twitter id dell'utente target
	 *@exception TwitterException problema nella comunicazione con twitter
	 *@exception IOException
	 *@return un oggetto Document (di fatto un BSON) contenente il profilo nuovo dell'utente
	 */
	private static Document extractUserData(long id) throws TwitterException, IOException {
		Twitter twitter = TwitterConnection.connect();
		User user = twitter.showUser(id);
		Document basicUser = new Document().append("info", new Document()
				.append("name", user.getName())
				.append("screen name", user.getScreenName())
				.append("id", user.getId()));

		HashMap<String, Integer> term2frequency = UserTerm2FrequencyMapGenerator.generateTerm2FrequencyMap(id);

		Document intermediateUser = basicUser.append("term2frequency", term2frequency);
		ArrayList<Document> allIntermediateUsers = UserPersistence.getAllIntermediateUsers();
		HashMap<String, Double> term2tfIdf =  UserTerm2TfIdfMapGenerator.generateUserTerm2TfIdfMap(allIntermediateUsers, intermediateUser);

		intermediateUser.remove("term2frequency");
		Document completeUser = intermediateUser.append("term2TfIdf", term2tfIdf);
		return completeUser;
	}

	/**
	 * Metodo ausiliario che, dato lo screenName dell'utente, ne ricava l'id e
	 * invoca extractUserData(id) e ne restituisce il risultato
	 *@see java.RecommendationSystem.extractUserData(long id)
	 *@param screenName screen name Twitter dell'utente target
	 *@exception TwitterException problema nella comunicazione con twitter
	 *@exception IOException
	 *@return un oggetto Document (di fatto un BSON) contenente il profilo completo dell'utente
	 */
	private static Document extractUserData(String screenName) throws TwitterException, IOException {
		Twitter twitter = TwitterConnection.connect();
		User user = twitter.showUser(screenName);
		long id = user.getId();
		return extractUserData(id);
	}


	/**
	 * Metodo ausiliario che, dati i generi preferiti dell'utente e il suo profilo completo,
	 * calcola e restituisce la lista dei brani ordinati per similarità rispetto all'utente
	 * 
	 *@param favouriteGenres lista dei generi musicali preferiti dall'utente
	 *@param completeUser il profilo dell'utente [con tutte le informazioni di un completeUser]
	 *@return la lista dei trackData ordinati per similarità rispetto all'utente [NOTA: si tratterebbe di una mappa <TrackData, Double>, ma per poterla ordinare è necessario gestirla come List di Map.Entry<TrackData, Double>]
	 */
	private static ArrayList<Entry<TrackData, Double>> recommendTracks(ArrayList<String> favouriteGenres, Document completeUser) {
		ArrayList<TrackData> tracks = new SpotifyTracksExtractor().extractTracksData(favouriteGenres);

		ArrayList< Map.Entry<TrackData, Double> > track2similarityList = new ArrayList< Map.Entry<TrackData, Double> >();

		for(TrackData track : tracks) {
			Double similarity = UserTrackSimilarity.calculate(completeUser, track);
			track2similarityList.add(new AbstractMap.SimpleEntry(track, similarity));
		}
		
		track2similarityList.sort(new UserTrackSimilarityComparator());
		return track2similarityList;
	}
}