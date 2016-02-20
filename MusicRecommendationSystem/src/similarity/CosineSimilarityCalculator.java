package similarity;

/**
 * Classe che si occupa di gestire i calcoli di coseno-similarità 
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class CosineSimilarityCalculator {

	/**
	 * Effettua il calcolo della cosenosimilarità tra due vettori
	 * 
	 * @param vector1 primo dei due vettori di cui calcolare la cossim
	 * @param vector2 secondo dei due vettori di cui calcolare la cossim
	 * @return il valore di cosenosimilarità calcolato; 
	 *  		se uno dei due vettori è composto da soli zeri, restituisce 0 a prescindere
	 */
	public static double compute(Double[] vector1, Double[] vector2) {
		
		double cosineSimilarity = 0.0;
		double numeratore = numeratore(vector1, vector2);
		double denominatore = denominatore (vector1, vector2);
		
		/* se il denominatore è uguale a 0, vuol dire che uno dei due vettori è composto da soli zeri
		 * in questo caso assumo che non sia possibile fare un confronto accettabile, e
		 * considero la similarità come 0 a prescindere*/
		if(denominatore != 0.0) {
			cosineSimilarity = numeratore/denominatore;
		}
		return cosineSimilarity;
	}
	
	/**
	 * metodo ausiliario per il calcolo del numeratore della cosenosimilarità 
	 * il numeratore è calcolato come prodotto scalare tra i due vettori
	 * 
	 * @param vector1 primo dei due vettori di cui si vuole calcolare la cossim
	 * @param vector2 secondo dei due vettori di cui si vuole calcolare la cossim
	 * @return il numeratore del calcolo della cosenosimilarità
	 */
	private static double numeratore(Double[] vector1, Double[] vector2) {
		double numeratore = 0.0;
		for(int i = 0; i < vector1.length; i++) {
			numeratore += vector1[i]*vector2[i];
		}
		return numeratore;
	}
	
	/**
	 * metodo ausiliario per il denominatore della cosenosimilarità  
	 * il denominatore è calcolato come prodotto delle radici della somma dei quadrati
	 *  dei termini dei due vettori
	 * (cioè, il prodotto delle somme quadratiche dei termini dei due vettori)
	 * 
	 * @param vector1 primo dei due vettori di cui si vuole calcolare la cossim
	 * @param vector2 secondo dei due vettori di cui si vuole calcolare la cossim
	 * @return il denominatore del calcolo della cosenosimilarità
	 */
	private static double denominatore(Double[] vector1, Double[] vector2) {
		return sommaQuadratica(vector1)*sommaQuadratica(vector2);
	}
	
	/*dato un vettore, ne calcola la somma quadratica
	 * (radice della somma dei quadrati dei termini del vettore)*/
	private static double sommaQuadratica(Double[] vector) {
		double sommaQuadratica = 0.0;
		for(int i = 0; i < vector.length; i++) {
			sommaQuadratica += vector[i]*vector[i];
		}
		return Math.sqrt(sommaQuadratica);
	}
}
