package twitterData.extraction;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Classe che si occupa di gestire la connessione con Twitter
 *  
 * @author Andrea Rossi
 * @version 1.0
 * @since 19 feb 2016
 */
public class TwitterConnection {
	
	private static final String CONSUMER_KEY = "k3I7BQx99Wv7rFm5AdBSsI7FH";
	private static final String CONSUMER_SECRET = "EFxHXsJxZdEbpBLD1kDpbGv5bfLCCVdFveQ2lR3rP9qwc7SnLx";
	private static final String ACCESS_TOKEN = "398214868-SrkHTOtaQzE8UkY6Mxq52tuAKfhw1GCebu0apaJL";
	private static final String ACCESS_TOKEN_SECRET = "r4fMiYl4vB3dRdICAKlgxdiJoCLAGifIVR9Ost1t6ufYu";

	
	/**
	 * 	Effettua la connessione con Twitter usando le credenziali dell'account developer personale 
	 * <br>
	 * NB: da sostituire con le credenziali di un account universitario
	 * <p>
	 * @return un oggetto Twitter a cui è possibile richiedere i servizi di Twitter
	 */
	public static Twitter connect() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(CONSUMER_KEY)
		  .setOAuthConsumerSecret(CONSUMER_SECRET)
		  .setOAuthAccessToken(ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}

}
