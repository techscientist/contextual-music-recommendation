package musicData.transformation;

import java.util.ArrayList;
import java.util.HashMap;

import musicData.extraction.LastFMTrackTagsExtractor;
import persistence.TagDatasetManager;
/**
 * Classe che si occupa di ottenere e processare la mappa tag - weight dei brani di LastFM
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class TrackTag2WeightMapGenerator {
	

	/**
	 * Ottiene la mappa tag-popularity relativa al brano passato come input;
	 * quindi la espande, estendendo l'insieme delle chiavi a tutti i possibili tag utilizzati in LastFM.
	 * Per i tag non compresi nell'elenco iniziale, si dà peso di default 0.0.
	 * Per i tag compresi nell'elenco iniziale si mantiene il peso di partenza.
	 * 
	 * @param title titolo del brano
	 * @param artist nome dell'artista che esegue il brano
	 * @return la nuova mappa tag-popularity espansa.
	 */
	public static HashMap<String, Double> generateTag2Popularity(String title, String artist) {
		/*mappa che contiene, per i soli tags associati al brano,
		 * entries del tipo <tag, popolarità di quel tag nel brano> */
		HashMap<String, Integer> trackTagsMap = LastFMTrackTagsExtractor.extractTrackTags(title, artist);
		
		/*mappa che dovrà contenere, per ogni tag dell'intero dataset, 
		 * una entry di tipo <tag, popolarità di quel tag nel brano> */
		HashMap<String, Double> tag2popularity = new HashMap<String, Double>();

		ArrayList<String> tags = TagDatasetManager.getTagStringList();
		
		/*prima riempie tag2popularity di entries <tag, 0>*/
		for(String tag : tags) {
			tag2popularity.put(tag, 0.0);
		}
		/*solo in seguito corregge il valore dei tag associati al brano*/
		for(String tag : trackTagsMap.keySet()) {
			tag2popularity.put(tag, trackTagsMap.get(tag).doubleValue());
		}
		
		return tag2popularity;
	}
}
