package it.unibo.destinationbuddy;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.view.AuthView;
import it.unibo.destinationbuddy.view.MainView;
import it.unibo.destinationbuddy.view.HomeView;
import it.unibo.destinationbuddy.view.ExploreView;
import it.unibo.destinationbuddy.view.ProfiloView;
import it.unibo.destinationbuddy.view.AdminView;
import it.unibo.destinationbuddy.view.DettaglioEscursioneView;
import it.unibo.destinationbuddy.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class App extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Avvio di DestinationBuddy...");
            connection = DAOUtils.connetti();
            System.out.println("✅ Connessione stabilita.");

            // ── 1. Crea i Model ───────────────────────────────────────────
            EscursioniModel escursioniModel   = new DBEscursioniModel(connection);
            UtentiModel     utentiModel       = new DBUtentiModel(connection);
            CertificazioniModel certsModel    = new DBCertificazioniModel(connection);
            AdminModel      adminModel        = new DBAdminModel(connection);

            // ── 2. Crea le View ───────────────────────────────────────────
            MainView               mainView     = new MainView();
            AuthView               authView     = new AuthView();
            HomeView               homeView     = new HomeView();
            ExploreView            exploreView  = new ExploreView();
            DettaglioEscursioneView dettaglioView = new DettaglioEscursioneView();
            ProfiloView            profiloView  = new ProfiloView();
            AdminView              adminView    = new AdminView();

            // ── 3. Collega i callback della MainView ──────────────────────
            mainView.setOnCatalog(()  -> mainView.setContenuto(homeView.getRoot()));
            mainView.setOnExplore(()  -> mainView.setContenuto(exploreView.getRoot()));
            mainView.setOnProfilo(()  -> mainView.setContenuto(profiloView.getRoot()));
            mainView.setOnAdmin(()    -> {
                adminView.setCertificazioniInAttesa(certsModel.getCertificazioniInAttesa());
                adminView.setUtentiDaPremiare(adminModel.getUtentiDaPremiare());
                mainView.setContenuto(adminView.getRoot());
            });
            mainView.setOnLogout(() -> {
                // Se autenticato → esci; se non autenticato → vai al login
                mainView.setAutenticato(false);
                mainView.setContenuto(homeView.getRoot());
            });
            mainView.setOnPrenotaNuova(() -> mainView.setContenuto(exploreView.getRoot()));

            // ── 4. Collega HomeView ───────────────────────────────────────
            homeView.setTop5(escursioniModel.getTop5());
            homeView.setOnEscursioneClick(preview -> {
                escursioniModel.getDettaglio(preview).ifPresent(exc -> {
                    dettaglioView.setEscursione(exc);
                    mainView.setContenuto(dettaglioView.getRoot());
                });
            });
            homeView.setOnMeseClick(mese ->
                homeView.setEscursioniMese(escursioniModel.getEscursioniPerMese(mese))
            );
            homeView.setOnExploreClick(() -> mainView.setContenuto(exploreView.getRoot()));

            // ── 5. Collega ExploreView ────────────────────────────────────
            exploreView.setEscursioni(escursioniModel.getAll());
            exploreView.setOnEscursioneClick(preview -> {
                escursioniModel.getDettaglio(preview).ifPresent(exc -> {
                    dettaglioView.setEscursione(exc);
                    mainView.setContenuto(dettaglioView.getRoot());
                });
            });
            exploreView.setOnFiltraTipologia(tip ->
                exploreView.setEscursioni(escursioniModel.getEscursionePerTipologia(tip))
            );
            exploreView.setOnRicerca(query -> {
                // Filtra lato client sul titolo — oppure aggiungi un metodo al model
                var tutti = escursioniModel.getAll();
                var filtrati = tutti.stream()
                    .filter(e -> e.titolo.toLowerCase().contains(query.toLowerCase()))
                    .toList();
                exploreView.setEscursioni(filtrati);
            });
            exploreView.setOnFiltraReset(() ->
                exploreView.setEscursioni(escursioniModel.getAll())
            );

            // ── 6. Collega DettaglioView ──────────────────────────────────
            dettaglioView.setOnIndietro(() -> mainView.setContenuto(exploreView.getRoot()));
            dettaglioView.setOnPrenota(exc -> {
                // TODO: quando avrai BookingView, sostituisci con:
                // bookingView.setEscursione(exc, utenteCorrente);
                // mainView.setContenuto(bookingView.getRoot());
                System.out.println("Prenotazione avviata per: " + exc.titolo);
            });

            // ── 7. Collega AdminView ──────────────────────────────────────
            adminView.setOnValidaCert(nCert -> certsModel.validaCertificazione(nCert));
            adminView.setOnAttivaGuida(p    -> adminModel.attivaGuida(p));
            adminView.setOnDisattivaGuida(p -> adminModel.disattivaGuida(p));

            // ── 8. Collega AuthView ───────────────────────────────────────
            authView.setOnLogin((email, password) -> {
            utentiModel.getPersonaAutenticata(email, password).ifPresentOrElse(
                persona -> {
                    mainView.setUtente(persona);
                    mainView.setAutenticato(true);   // ← cambia il pulsante in "Esci"
                    homeView.setUtente(persona);
                    profiloView.setUtente(persona);
                    profiloView.setCertificazioni(
                        certsModel.getCertificazioniUtente(persona.cf)
                    );
                    // Torna alla home dopo il login
                    mainView.setContenuto(homeView.getRoot());
                },
                () -> authView.mostraErroreLogin("Email o password errati.")
            );
        });
            authView.setOnRegistra(campi -> {
                // campi = [nome, cognome, cf, email, password]
                var nuovoUtente = new it.unibo.destinationbuddy.data.Persona(
                    campi[2], campi[0], campi[1],
                    true, false, campi[2],
                    0, java.time.LocalDate.now(), null,
                    campi[3], campi[4], ""
                );
                utentiModel.registraUtente(nuovoUtente);
                authView.pulisciCampi();
                authView.mostraErroreLogin(""); // nessun errore
                // Dopo registrazione → vai al login
            });

            // ── 9. Mostra subito la Home (senza login) ────────────────────────
            homeView.setTop5(escursioniModel.getTop5());
            exploreView.setEscursioni(escursioniModel.getAll());
            mainView.setContenuto(homeView.getRoot());

            Scene scene = new Scene(mainView.getRoot(), 1200, 700);
            primaryStage.setTitle("Destination Buddy");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                System.out.println("🛑 Chiusura app. Disconnessione dal database...");
                connection.close();
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}