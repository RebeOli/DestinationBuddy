package it.unibo.destinationbuddy.controller;

import it.unibo.destinationbuddy.data.Abbonamento;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.model.*;
import it.unibo.destinationbuddy.view.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AppController {

    private final Stage               primaryStage;
    private final EscursioniModel     escursioniModel;
    private final UtentiModel         utentiModel;
    private final CertificazioniModel certsModel;
    private final AdminModel          adminModel;
    private final PrenotazioniModel   prenotModel;

    private final MainView                    mainView      = new MainView();
    private final AuthView                    authView      = new AuthView();
    private final HomeView                    homeView      = new HomeView();
    private final ExploreView                 exploreView   = new ExploreView();
    private final DettaglioEscursioneView     dettaglioView = new DettaglioEscursioneView();
    private final ProfiloView                 profiloView   = new ProfiloView();
    private final AdminView                   adminView     = new AdminView();
    private final BookingView                 bookingView   = new BookingView();
    private final CreaEscursioneView          creaView      = new CreaEscursioneView();
    private final AggiungiCertificazioneView  aggCertView   = new AggiungiCertificazioneView();
    private final PremiumView premiumView = new PremiumView();

    private Persona utenteCorrente = null;

    public AppController(Stage stage,
                         EscursioniModel e,
                         UtentiModel u,
                         CertificazioniModel c,
                         AdminModel a,
                         PrenotazioniModel p) {
        this.primaryStage    = stage;
        this.escursioniModel = e;
        this.utentiModel     = u;
        this.certsModel      = c;
        this.adminModel      = a;
        this.prenotModel     = p;
    }

    public void avvia() {
        collegaAuthView();
        collegaMainView();
        collegaHomeView();
        collegaExploreView();
        collegaDettaglioView();
        collegaBookingView();
        collegaAdminView();
        collegaProfiloView();
        collegaCreaView();
        collegaAggCertView();
        collegaPremiumView();

        homeView.setTop5(escursioniModel.getTop5());
        exploreView.setEscursioni(escursioniModel.getAll());

        mainView.setAutenticato(false);
        mainView.setContenuto(homeView.getRoot());
        mostraScenaMain();
    }

    // ── Navigazione ───────────────────────────────────────────────

    private void mostraScenaAuth() {
        Scene scene = new Scene(authView.getRoot(), 1200, 700);
        applicaCSS(scene);
        primaryStage.setScene(scene);
    }

    private void mostraScenaMain() {
        Scene scene = new Scene(mainView.getRoot(), 1200, 700);
        applicaCSS(scene);
        primaryStage.setTitle("Destination Buddy");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostraHome() {
        mainView.setContenuto(homeView.getRoot());
        mainView.setNavAttiva("home");

        if (utenteCorrente != null) {
            boolean haAbbonamentoAttivo = utentiModel.getUltimoAbbonamento(utenteCorrente)
                    .map(Abbonamento::isAttivo)
                    .orElse(false);
            homeView.setHaAbbonamentoAttivo(haAbbonamentoAttivo);
        } else {
            homeView.setHaAbbonamentoAttivo(false); // utente non loggato → mostra comunque l'invito
        }
    }
    private void mostraExplore() { 
        mainView.setContenuto(exploreView.getRoot()); mainView.setNavAttiva("explore"); 
    }

    private void mostraProfilo() {
        if (utenteCorrente != null) {
            profiloView.setUtente(utenteCorrente);
            profiloView.setCertificazioni(certsModel.getCertificazioniUtente(utenteCorrente.cf));
            profiloView.setAbbonamento(utentiModel.getUltimoAbbonamento(utenteCorrente));

        }
        mainView.setContenuto(profiloView.getRoot());
        mainView.setNavAttiva("profilo");
    }

    private void mostraAdmin() {
        adminView.setCertificazioniInAttesa(certsModel.getCertificazioniInAttesa());
        adminView.setUtentiDaPremiare(adminModel.getUtentiDaPremiare());
        mainView.setContenuto(adminView.getRoot());
    }

    private void eseguiLogout() {
        utenteCorrente = null;
        mainView.setAutenticato(false);
        mostraHome();
    }

    // ── Auth ──────────────────────────────────────────────────────

    private void collegaAuthView() {
        authView.setOnLogin((email, password) ->
            utentiModel.getPersonaAutenticata(email, password).ifPresentOrElse(
                persona -> {
                    utenteCorrente = persona;
                    mainView.setUtente(persona); // Questo aggiornerà la barra laterale!
                    mainView.setAutenticato(true);
                    homeView.setUtente(persona);
                    profiloView.setUtente(persona);

                    try {
                        profiloView.setCertificazioni(
                            certsModel.getCertificazioniUtente(persona.cf));
                    } catch (Exception ex) {
                        System.out.println("⚠️ Errore nel caricare le certificazioni: " + ex.getMessage());
                    }

                    if (persona.isAmministratore()) {
                        mostraAdmin(); // Se è admin, lo butti dritto nel Pannello Amministratore!
                    } else {
                        mostraHome();  // Altrimenti vede le Top 5 escursioni
                    }
                },
                () -> authView.mostraErroreLogin("Email o password errati.")
            )
        );

        authView.setOnRegistra(campi -> {
            var nuovoUtente = new Persona(
                campi[2], campi[0], campi[1],
                true, false, campi[2],
                0, LocalDate.now(), null,
                campi[3], campi[4], ""
            );
            utentiModel.registraUtente(nuovoUtente);
            authView.pulisciCampi();
            authView.mostraErroreLogin("");
            // Dopo registrazione resta sul form (o torna al login)
        });
    }

    // ── Main ──────────────────────────────────────────────────────

    private void collegaMainView() {
        mainView.setOnHome(()         -> mostraHome());
        mainView.setOnExplore(()      -> mostraExplore());
        mainView.setOnProfilo(()      -> {
            if (utenteCorrente == null) mostraLogin();
            else mostraProfilo();
        });
        mainView.setOnAdmin(()        -> mostraAdmin());
        mainView.setOnPrenotaNuova(() -> mostraExplore());
        mainView.setOnImpostazioni(() -> { });

        // Pulsante "Accedi" nella topbar: apre AuthView dentro la stessa scena
        mainView.setOnLogin(() -> mostraLogin());

        // Pulsante "Esci" nella topbar (quando già autenticato)
        mainView.setOnLogout(() -> {
            if (utenteCorrente != null) eseguiLogout();
            else mostraLogin();
        });
    }

    /** Mostra il form di login/registrazione SENZA cambiare Scene (resta dentro MainView). */
    private void mostraLogin() {
        mainView.setContenuto(authView.getRoot());
        mainView.setNavAttiva("");
    }

    // ── Home ──────────────────────────────────────────────────────

    private void collegaHomeView() {
        homeView.setOnEscursioneClick(preview -> apriDettaglio(preview));
        homeView.setOnMeseClick(mese ->
            homeView.setEscursioniMese(escursioniModel.getEscursioniPerMese(mese))
        );
        homeView.setOnExploreClick(() -> mostraExplore());
        homeView.setOnUpgradeClick(() -> {
            if (utenteCorrente == null) {
                mostraLogin();
            } else {
                mainView.setContenuto(premiumView.getRoot());
            }
        });
    }

    // ── Explore ───────────────────────────────────────────────────

    private void collegaExploreView() {
        exploreView.setOnEscursioneClick(preview -> apriDettaglio(preview));
        exploreView.setOnFiltraTipologia(tip ->
            exploreView.setEscursioni(escursioniModel.getEscursionePerTipologia(tip))
        );
        exploreView.setOnRicerca(query -> {
            var filtrati = escursioniModel.getAll().stream()
                .filter(e -> e.titolo.toLowerCase().contains(query.toLowerCase()))
                .toList();
            exploreView.setEscursioni(filtrati);
        });
        exploreView.setOnFiltraReset(() ->
            exploreView.setEscursioni(escursioniModel.getAll())
        );
    }

    private void apriDettaglio(it.unibo.destinationbuddy.data.EscursionePreview preview) {
        escursioniModel.getDettaglio(preview).ifPresent(exc -> {
            dettaglioView.setEscursione(exc);
            mainView.setContenuto(dettaglioView.getRoot());
        });
    }

    // ── Dettaglio ─────────────────────────────────────────────────

    private void collegaDettaglioView() {
        dettaglioView.setOnIndietro(() -> mostraExplore());
        dettaglioView.setOnPrenota(exc -> {
            if (utenteCorrente == null) {
                mostraLogin();
                return;
            }
            int posti     = prenotModel.getPostiRimanenti(exc.idEscursione);
            double sconto = prenotModel.getScontoNoleggio(utenteCorrente.cf);
            bookingView.setEscursione(exc, utenteCorrente, posti, sconto);
            mainView.setContenuto(bookingView.getRoot());
        });
    }

    // ── Booking ───────────────────────────────────────────────────

    private void collegaBookingView() {
        bookingView.setOnIndietro(() -> mainView.setContenuto(dettaglioView.getRoot()));
        bookingView.setOnConferma((idEscursione, equipSel) -> {
            if (utenteCorrente == null) return;

            boolean certOk = prenotModel.verificaCertificazioni(idEscursione, utenteCorrente.cf);
            if (!certOk) {
                bookingView.mostraErrore("Non possiedi le certificazioni richieste per questa escursione.");
                return;
            }

            int posti = prenotModel.getPostiRimanenti(idEscursione);
            if (posti <= 0) {
                bookingView.mostraErrore("Spiacente, non ci sono più posti disponibili.");
                return;
            }

            boolean ok = prenotModel.confermaPrenotazione(utenteCorrente.cf, idEscursione);
            if (!ok) {
                bookingView.mostraErrore("Errore durante la prenotazione. Riprova.");
                return;
            }

            int durata = 1;
            for (var entry : equipSel.entrySet()) {
                if (entry.getValue()) {
                    String idPezzo = prenotModel.trovaPezzoDisponibile(entry.getKey());
                    if (idPezzo != null) {
                        prenotModel.noleggiaPezzo(idPezzo, idEscursione, utenteCorrente.cf, durata);
                    }
                }
            }

            bookingView.mostraConferma();
        });
    }

    // ── Admin ─────────────────────────────────────────────────────

    private void collegaAdminView() {
        adminView.setOnValidaCert(nCert -> certsModel.validaCertificazione(nCert));
        adminView.setOnAttivaGuida(p    -> adminModel.attivaGuida(p));
        adminView.setOnDisattivaGuida(p -> adminModel.disattivaGuida(p));
    }

    // ── Profilo ───────────────────────────────────────────────────

    private void collegaProfiloView() {
        profiloView.setOnCreaEscursione(() -> {
            if (utenteCorrente == null) return;
            creaView.setGuidaCF(utenteCorrente.cf);
            creaView.setTipologieDisponibili(escursioniModel.getTipologie());
            mainView.setContenuto(creaView.getRoot());
        });
        profiloView.setOnAggiungiCert(() -> {
            if (utenteCorrente == null) return;
            aggCertView.setCfUtente(utenteCorrente.cf);
            aggCertView.setTipologie(certsModel.getTipologieDisponibili());
            mainView.setContenuto(aggCertView.getRoot());
        });
        profiloView.setOnVaiPremium(() -> {
            if (utenteCorrente != null) {
                mainView.setContenuto(premiumView.getRoot());
            }
        });
    }

    // ── CreaEscursione ────────────────────────────────────────────

    private void collegaCreaView() {
        creaView.setOnAnnulla(() -> mostraProfilo());
        creaView.setOnCrea(formData -> {
            escursioniModel.creaEscursione(
                formData.escursione,
                formData.descrizione,
                formData.numeroPartecipanti,
                formData.guidaCF,
                formData.tipologie
            );
            creaView.pulisciForm();
            exploreView.setEscursioni(escursioniModel.getAll());
            mostraProfilo();
        });
    }

    // ── AggiungiCertificazione ────────────────────────────────────

    private void collegaAggCertView() {
        aggCertView.setOnAnnulla(() -> mostraProfilo());
        aggCertView.setOnSalva(cert -> {
            certsModel.aggiungiCertificazione(cert);
            aggCertView.pulisciForm();
            if (utenteCorrente != null) {
                profiloView.setCertificazioni(certsModel.getCertificazioniUtente(utenteCorrente.cf));
            }
            mostraProfilo();
        });
    }

    // ── CSS ───────────────────────────────────────────────────────

    private void applicaCSS(Scene scene) {
        var css = getClass().getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("⚠️  CSS non trovato, continuo senza stili.");
        }
    }
    private void collegaPremiumView() {
        premiumView.setOnIndietro(() -> mostraHome());
        premiumView.setOnScegliPiano(piano -> {
            if (utenteCorrente == null) {
                mostraLogin();
                return;
            }
            boolean ok = utentiModel.sottoscriviAbbonamento(
                piano.prezzoMensile, piano.mesi, utenteCorrente.cf
            );
            if (!ok) {
                premiumView.mostraErrore("Hai già sottoscritto un abbonamento oggi.");
                return;
            }
            mostraProfilo();
        });
    }
}
