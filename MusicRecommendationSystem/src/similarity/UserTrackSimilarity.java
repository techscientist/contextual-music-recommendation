package similarity;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import musicData.extraction.TrackData;
import musicData.transformation.TrackTag2WeightMapGenerator;

/**
 * Classe che si occupa di gestire il calcolo della vicinanza tra utenti e brani musicali
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class UserTrackSimilarity {

	/**
	 * Effettua il calcolo della similarità tra un utente e un brano
	 * 
	 * @param user utente (di categoria complete user) di cui calcolare la similarità con un brano
	 * @param track informazioni sul brano di cui calcolare la similarità con l'utente
	 * @return il valore di similarità tra utente e brano
	 */
	public static double calculate (Document user, TrackData track) {
		Map<String, Double> term2TfIdf = (Map) user.get("term2TfIdf");
		//Document term2TfIdf = (Document) user.get("term2TfIdf");
		Double[] userVector = term2TfIdf.values().toArray(new Double[term2TfIdf.size()]);
		
		HashMap<String, Double> tag2popularity = TrackTag2WeightMapGenerator.generateTag2Popularity(track.getTitle(), track.getArtistName());
		Double[] trackVector = tag2popularity.values().toArray(new Double[tag2popularity.size()]);

		return CosineSimilarityCalculator.compute(userVector, trackVector);
	}
}