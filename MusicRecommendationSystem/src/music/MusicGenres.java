package music;

/**
 * Classe che conserva e espone informazioni generali sul dominio della musica 
 * (per ora solo i principali generi musicali).
 * La classe offre metodi statici per ottenere le informazioni.
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class MusicGenres {
	/**
	 * Array dei generi musicali presi in considerazione dal sistema di raccomandazione
	 */
	static final String[] MUSIC_GENRES = {"pop", "rock", "indie", "hiphop", "r&b", "soul", "metal", "country", "jazz", "classical"};
	
	
	/**
	 * Ottiene i generi musicali considerati dal sistema di raccomandazione, 
	 * rappresentati ciascuno come una stringa
	 * @return i generi musicali in un array di oggetti String.
	 */
	public static String[] getGenresArray() {
		return MUSIC_GENRES;
	}
}
