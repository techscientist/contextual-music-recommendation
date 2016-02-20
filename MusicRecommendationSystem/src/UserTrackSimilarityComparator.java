import java.util.Comparator;
import java.util.Map;

import musicData.extraction.TrackData;

/**
 * Classe che implementa Comparator per consentire il confronto di oggetti Map.Entry<TrackData, Double>
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class UserTrackSimilarityComparator implements Comparator<Map.Entry<TrackData, Double>> {
	    @Override
	    public int compare(Map.Entry<TrackData, Double> entry1, Map.Entry<TrackData, Double> entry2) {
	        Double value1 = entry1.getValue();
	        Double value2 = entry2.getValue();
	        
	        // ordine decrescente (ordine crescente sarebbe value1.compareTo(value2)
	        return value2.compareTo(value1);
	    }
	}
