
PROGETTO BASI DI DATI: DESTINATION BUDDY
Autori: Chiara Mazzoni, Arianna Mondardini, Rebecca Olivieri

REQUISITI DI SISTEMA
--------------------------------------------------------------------
Per eseguire correttamente l'applicativo sono necessari:
- Java Development Kit (JDK) versione 21 (o superiore)
- Un server MySQL in esecuzione (locale o su macchina virtuale)
- Connessione a Internet attiva (per scaricare le dipendenze di Gradle al primo avvio)


CONFIGURAZIONE DEL DATABASE
--------------------------------------------------------------------
Prima di avviare l'applicazione, è necessario che il server MySQL sia in esecuzione.

a) Avvio del server:
   - Aprire il pannello di controllo di XAMPP (o software equivalente come MAMP/WAMP).
   - Cliccare su "Start" in corrispondenza del modulo "MySQL" per avviare il database (assicurandosi che utilizzi la porta standard 3306).

b) Creazione e popolamento del database: 
   - Aprire il proprio client MySQL (es. DBeaver, MySQL Workbench).
   - Creare un nuovo schema/database vuoto "Destinationbuddy".
   - Eseguire per intero lo script SQL fornito nella cartella resources (struttura.sql e popolamento.sql). Questo script creerà tutte le tabelle, 
     inserirà i dati di popolamento iniziali e configurerà automaticamente l'utente dedicato "app_java" con i relativi permessi.

c) Configurare le credenziali nel codice Java:
   - Nel codice sorgente, aprire il file contenente la configurazione del database: src/main/java/it/unibo/destinationbuddy/data/DAOUtils.java.
   - È sufficiente decommentare la variabile URL corretta a seconda del proprio ambiente:
       * Per Windows/Locale: decommentare `URL = "jdbc:mysql://localhost:3306/DestinationBuddy"`
       * Per Mac con VM: decommentare `URL = "jdbc:mysql://10.211.55.3:3306/DestinationBuddy"`


ISTRUZIONI PER L'AVVIO
--------------------------------------------------------------------
L'applicativo utilizza Gradle come sistema di build. È possibile avviarlo in due modi:

MODO A - Tramite Terminale (Consigliato):
    1. Aprire il terminale e navigare all'interno della cartella principale del progetto.
    2. Su Mac/Linux, eseguire il comando:
                                            ./gradlew run
    3. Su Windows, eseguire il comando:
                                            gradlew.bat run

MODO B - Tramite IDE (VS Code, IntelliJ, Eclipse):
    1. Importare la cartella come progetto Gradle/Java.
    2. Individuare la classe App.java.
    3. Eseguire la classe tramite il tasto "Run" o "Play" dell'ambiente di sviluppo.


CREDENZIALI DEMO
--------------------------------------------------------------------
Di seguito sono forniti gli account pre-configurati nel database per testare i diversi ruoli e funzionalità della piattaforma:

• AMMINISTRATORE
    - Email: admin@escursioni.it
    - Password: pass123

• GUIDA CERTIFICATA (Può creare escursioni e inserire resoconti)
    - Utente: Anna Neri
    - Email: anna.guida@gmail.com
    - Password: pass123

• UTENTE BASE
    - Utente: Arianna Mondardini
    - Email: arianna@gmail.com
    - Password: pass123