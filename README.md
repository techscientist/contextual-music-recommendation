# contextual-music-recommendation

Questa applicazione è un progetto per il corso "Sistemi Intelligenti su Internet" dell'Università Roma Tre.
Il suo obiettivo è offrire una raccomandazione musicale agli utenti di Twitter sulla base della loro Twitter Description e dei loro Tweets.

Dato l'ID Twitter di un utente, il sistema
- chiede all'utente quali siano i suoi generi musicali preferiti
- ne estrae i Tweet e le informazioni
- per ogni possibile tag di Last.fm calcola il numero di occorrenze di quel tag nei tweet dell'utente
[NOTA: la lista di tutti i tag di Last.fm è acquisita usando un dataset Hetrec presente nel repository]
- per ogni tag quindi calcola il peso TF-IDF nei tweet dell'utente, basandosi su un corpus di 100 utenti "validi" già processati e registrati nel database
- sulla base dei generi musicali preferiti, il sistema estrae una selezione di brani di interesse da Spotify (ci si rifà a playlist pubblicate e aggiornate da Spotify con i brani più ascoltati)
- per ogni brano si fa una richiesta a Last.fm per estrarre i tag associati con relativa pesatura di popolarità
- Si calcola la coseno-similarità tra il vettore dei tag usati dall'utente e il vettore dei tag associati a ciascun brano
- Si restituiscono i 10 brani con coseno-similarità massima



Per l'acquisizione dei 100 utenti memorizzati, si sono scelti utenti:
- follower di Twitter Music
- con almeno 50 tweets
- di lingua inglese
- con più friends che followers (quindi non VIP e non organizzazioni)

Per ogni utente valido trovato, per evitare di fare flooding sulle API di Twitter, e per ragioni di testing, sono state prima estratte solo le informazioni anagrafiche e di ID (basic user); quindi in uno step successivo sono state calcolate le frequenze dei vari tag (intermediate user); solo dopo è stato possibile completare la pesatura con tf-idf (complete user).

Ovviamente è possibile richiedere raccomandazioni su uno qualsiasi tra i 100 utenti memorizzati.


[NOTA: inizialmente si pensava di processare i tweets usando con un modulo di NLP; tale modulo, essendo completo, è stato comunque mantenuto nel sistema ma è attualmente inutilizzato]
