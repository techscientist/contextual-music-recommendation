package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe deputata all'interazione con il dataset di tutti i tag utilizzati in Last.fm
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class TagDatasetManager {
	private final static String DATASET_PATH = "datasets/tags.dat";

	/**
	 * Ottiene la lista dei tag dal dataset e li stampa a video
	 * 
	 */
	public static void readAll() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(new File(DATASET_PATH)));
			String available;
			bufferReader.readLine();
			while((available = bufferReader.readLine()) != null) {
				
				String arr[] = available.split("\t", 2);

				System.out.println(Integer.parseInt((String)arr[0].trim()));
				System.out.println(arr[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	
	/**
	 * Ottiene tuttii tag utilizzati in Last.fm dal dataset Hetrec
	 * 
	 * @return una lista contenente i tag
	 */
	public static ArrayList<String> getTagStringList() {
		ArrayList<String> tags = new ArrayList<String>();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(new File(DATASET_PATH)));
			String available;
			bufferReader.readLine();
			while((available = bufferReader.readLine()) != null) {
				
				String arr[] = available.split("\t", 2);
				tags.add(arr[1].trim());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tags;
	}
}
