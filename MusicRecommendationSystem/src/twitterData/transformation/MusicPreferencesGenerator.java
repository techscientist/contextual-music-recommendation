package twitterData.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import music.MusicGenres;
import twitter4j.TwitterException;

/**
 * Classe che si occupa di generare le preferenze musicali random
 * che simulano una comunicazione esplicita delle proprie preferenze musicali
 * da parte degli utenti estratti da Twitter
 *
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class MusicPreferencesGenerator {
	
	/**
	 * a partire dai 15 generi di musica in analisi, 
	 * genera e restituisce un massimo di tre preferenze randomiche
	 * 
	 *  @return la lista di generi musicali preferiti, espressi come String
	 */
	public static ArrayList<String> generateRandomMusicGenres() {
		
		/*prima scelgo random quanti N generi musicali preferiti avere (da 1 a 3)*/
		int favouriteGenresNumber = Math.abs(new Random().nextInt() % 3) + 1;
		/*poi, sulla base del numero di generi preferiti, estraggo i generi
		 * in modo randomico e facendo attenzione di evitare i doppioni*/
		ArrayList<String> favouriteGenres = new ArrayList<String>(favouriteGenresNumber);
		
		for(int i = 0; i < favouriteGenresNumber; i++) {
			int newFavouriteGenreIndex = Math.abs(new Random().nextInt() % MusicGenres.getGenresArray().length);
			String newFavouriteGenre = MusicGenres.getGenresArray()[newFavouriteGenreIndex];
			while (favouriteGenres.contains(newFavouriteGenre)) {
				newFavouriteGenreIndex = Math.abs(new Random().nextInt() % MusicGenres.getGenresArray().length);
				newFavouriteGenre = MusicGenres.getGenresArray()[newFavouriteGenreIndex];
			}
			favouriteGenres.add(newFavouriteGenre);
		} 
		return favouriteGenres;	
	}
}
