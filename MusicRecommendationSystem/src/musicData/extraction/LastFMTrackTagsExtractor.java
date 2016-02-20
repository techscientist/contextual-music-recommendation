package musicData.extraction;

import java.util.Collection;
import java.util.HashMap;

import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;

/**
 * Classe che si occupa dell'estrazione dei tag associati ad un brano 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class LastFMTrackTagsExtractor {
	
	/**
	 * Ottiene i tag associati ad un brano in LastFM ed i loro valori di popolarità
	 * @param title il titolo del brano
	 * @param artist il nome dell'artista come oggetto String.
	 * @return hashMap <Tag, Integer> contenente tutte coppie tag - weight associate al brano in LastFM
	 */
	public static HashMap<String, Integer> extractTrackTags(String title, String artist) {
		String apiKey = LastFMConnection.getApiKey();
		
		Collection<Tag> tags = Track.getTopTags(artist, title, apiKey);
		HashMap<String, Integer> tag2weight = new HashMap<String, Integer>();
		for(Tag tag : tags) {
			tag2weight.put(tag.getName().toLowerCase(), tag.getCount());
		}
		
		return tag2weight;
	}
}
