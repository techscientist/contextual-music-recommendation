package musicData.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.Track;

import music.MusicGenres;

/**
 * Un oggetto SpotifyTracksExtractor si occupa dell'estrazione di brani da Spotify 
 * a partire dai generi musicali di interesse
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class SpotifyTracksExtractor {
	
	/**
	 * Mappa che associa ad ogni genere musicale il codice della playlist di Spotify
	 * incenrata su quel genere scelta per il sistema di raccomandazione
	 */
	private HashMap<String, String> genre2playlist;

	
	
	/**
	 * Costruisce un oggetto SpotifyTracksExtractor dotato di tutte le informazioni necessarie
	 * per estrarre brani di qualsiasi genere musicale tra quelli usati dal sistema
	 * 
	 * <p>
	 * Nello specifico, compila la mappa genre2Playlist che associa ad ogni genere musicale la
	 * playlist di Spotify da usare come riferimento.
	 * Le varie playlist sono state individuata manualmente tra quelle pubblicate e
	 * aggiornate dinamicamente dallo staff di Spotify sulla base dei maggiori ascolti.
	 * Ogni playlist è identificata da un codice univoco.
	 * <p>
	 */
	public SpotifyTracksExtractor() {
		this.genre2playlist = new HashMap<String, String> (MusicGenres.getGenresArray().length);
		this.genre2playlist.put("pop", "3ZgmfR6lsnCwdffZUan8EA");
		this.genre2playlist.put("rock", "3qu74M0PqlkSV76f98aqTd");
		this.genre2playlist.put("indie", "4dJHrPYVdKgaCE3Lxrv1MZ");
		this.genre2playlist.put("hiphop", "06KmJWiQhL0XiV6QQAHsmw");
		this.genre2playlist.put("r&b", "76h0bH2KJhiBuLZqfvPp3K");		
		this.genre2playlist.put("metal", "6yIGwQ7pz2lHxGW6tTcnpL");
		this.genre2playlist.put("country", "5tA2x3J6yAaJpa7mHGvhmB");
		this.genre2playlist.put("jazz","64xDgnmM6W7vG1x6bDVDmJ");
		this.genre2playlist.put("soul","0UUovM2yGwRThZSy9BvADQ");
		this.genre2playlist.put("classical","3J3mTk0N0NzDOFgnp67Z75");
	}


	/**
	 * Si connette a Spotify ed estrae i brani musicali più importanti legati ai generi musicali passati
	 *<p>
	 * Per ogni genere musicale, si ottengono i brani contenuti nella più importante playlist 
	 * pubblicata e aggiornata da Spotify incentrata su quel genere.
	 *<p>
	 *
	 * @param favouriteGenres lista dei generi musicali di cui si vogliono estrarre brani
	 * @return lista dei brani contenuti nelle playlist di Spotify relative a qi generi musicali ricevuti in input;
	 * ogni brano è restituito sotto forma di oggetti TrackData, contenenti informazioni quali nome del brano e dell'artista
	 */
	/*estrae da Spotify e restituisce i dati dei brani dei generi specificati*/
	public ArrayList<TrackData> extractTracksData(ArrayList<String> favouriteGenres) {
		ArrayList<TrackData> allTracksData = new ArrayList<TrackData>();
		Api api = SpotifyConnection.connect();
		
		/*per ciascuno dei generi musicali preferiti dall'utente, 
		 * estraggo i brani più popolari del momento e ne restituisco le informazioni primarie
		 * in una arrayList di (con cui interagire con LastFM)*/
		for(String genre : favouriteGenres) {
			ArrayList<TrackData> genreTracksData = this.extractPlaylistTracksData(api, genre);
			allTracksData.addAll(allTracksData.size(), genreTracksData);
		}
		return allTracksData;
	}


	/**
	 * Metodo ausiliario che dato un genere musicale estrae da Spotify i brani musicali più importanti di quel genere;
	 * Per individuare i brani si usa la playlist pubblicata e aggiornata da Spotify incentrata sul genere musicale passato.
	 *
	 * @param genre genere musicale di cui si vogliono estrarre i brani più importanti
	 * @param spotify oggetto Api che consente di effettuare richieste a Spotify
	 * @return lista dei brani contenuti nella playlist Spotify relativa a quel genere
	 */
	/*estrae da Spotify i brani del genere specificato
	 * utilizzando la connessione api che viene passata (ottenuta con SpotifyConnection.connect())
	 * e ne restituisce i dati*/
	private ArrayList<TrackData> extractPlaylistTracksData(Api spotify, String genre) {
		ArrayList<TrackData> playlistTracksData = new ArrayList<TrackData>();

		/*prima verifico se per il genere passato è possibile estrarre i brani da spotify*/
		if(this.genre2playlist.containsKey(genre)) {
			/*costruisco una richiesta per la playlist di Spotify relativa a quel genere*/
			String playListID = this.genre2playlist.get(genre);
			PlaylistRequest playlistRequest = spotify.getPlaylist("spotify", playListID).build();

			try {
				/*provo a ottenere la playlist da spotify*/
				final Playlist playlist = playlistRequest.get();
				/*ne estraggo i brani (come oggetti PlaylistTracks)*/
				List<PlaylistTrack> tracks = playlist.getTracks().getItems();
				
				/*per i primi 100 brani della playlist, creo degli oggetti 
				 * LastFMTrackData e li inserisco in playlistTracksData*/
				for(int i = 0; i < 100; i++) {
					PlaylistTrack playlistTrack = tracks.get(i);
					Track currentTrack = playlistTrack.getTrack();
					TrackData currentTrackData = new TrackData(currentTrack.getName(), currentTrack.getArtists().get(0).getName());
					playlistTracksData.add(currentTrackData);
				}
			} catch (Exception e) {
				System.out.println("Something went wrong!" + e.getMessage());
			}
		}
		return playlistTracksData;
	}
}