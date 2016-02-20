package musicData.extraction;

/**
 * Un oggetto TrackData conserva le informazioni più importanti relative ad un brano musicale
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class TrackData {
	/**
	 * Titolo del brano musicale
	 */
	private String title;
	/**
	 * Nome dell'artista che ha eseguito il brano musicale
	 */
	private String artistName;
	
	/**
	 * Crea un nuovo oggetto che rappresenta le informazioni di un brano musicale
	 * @param title titolo del brano
	 * @param artistName nome dell'artista che esegue il brano
	 */
	public TrackData(String title, String artistName) {
		this.title = title;
		this.artistName = artistName;
	}
	
	/**
	 * metodo per ottenere il titolo del brano
	 * @return il titolo del brano
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * metodo per sovrascrivere il titolo del brano
	 * @param title il nuovo titolo 
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * metodo per ottenere il nome dell'artista che esegue il brano
	 * @return il nome dell'artista
	 */
	public String getArtistName() {
		return artistName;
	}
	
	/**
	 * metodo per sovrascrivere l'artista del brano
	 * @param artistName il nuovo nome dell'artista 
	 */
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
}
