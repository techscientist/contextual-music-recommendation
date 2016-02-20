package musicData.extraction;

import java.io.IOException;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

/**
 * Classe che si occupa di instaurare una connessione con Spotify per poter usufruire delle sue APIs.
 * La classe conserva le credenziali di accesso ai servizi di Spotify e le utilizza per
 * creare richieste di comunicazione.
 * 
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class SpotifyConnection {

	/**
	 * ID legato all'account personale Spotify
	 * NB: da sostituire con un ID associato a un account universitario 
	 */
	final static String CLIENT_ID = "1fa352b29c414e9db55d2ab1925ba957"; 
	/**
	 * Codice segreto legato all'account personale Spotify
	 * NB: da sostituire con un ID associato a un account universitario 
	 */
	final static String CLIENT_SECRET = "84ca3e7fb49e42569d4f232917b71102";

	
	/**
	 * Instaura una connessione con Spotify.
	 * 
	 * <p>
	 * Nello specifico, usa le credenziali per creare un oggetto ClientCredentialsGrantRequest
	 * in grado di formulare una richiesta di comunicazione con Spotify.
	 * La richiesta viene gestita in modo sincrono.
	 * <p>
	 * 
	 * @return un oggetto di tipo Api che funge da punto di accesso per i servizi di Spotify.
	 */
	public static Api connect() {

		Api.Builder b = Api.builder();
		b.clientId(CLIENT_ID);
		b.clientSecret(CLIENT_SECRET);
		Api api = b.build();
		
		/* Create a request object. */
		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

		/* Use the request object to make the request synchronously (get) */
		ClientCredentials responseFuture;
		try {
			responseFuture = request.get();
			if(responseFuture != null) {
				api.setAccessToken(responseFuture.getAccessToken());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return api;
	}
}