package musicData.extraction;

/**
 * Classe che conserva le informazioni necessarie per usufruire dei servizi di LastFM 
 * Nello specifico, conserva il codice apiKey da utilizzare per usare le API di LastFM
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class LastFMConnection {
	/**
	 * Chiave legata all'account personale LastFM e necessaria per richiedere la maggior parte dei servizi di LastFM
	 * NB: da sostituire con una chiave associato a un account universitario 
	 */
	private static String apiKey = "5ddc9b3c5524ccee7d829614aee9de92";
	
	/**
	 * Ottiene la chiave apiKey da passare nelle chiamate all'API LastFM 
	 * @return l'apiKey come oggetto String.
	 */
	public static String getApiKey() {
		return apiKey;
	}
}
