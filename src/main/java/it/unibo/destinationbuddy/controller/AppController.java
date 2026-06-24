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
    private final PostEscursioneModel postEscursioneModel;

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
    private final PremiumView                 premiumView   = new PremiumView();
    private final GestioneGeograficaAdminView gestioneGeoView = new GestioneGeograficaAdminView();
    private final InserisciResocontoView resocontoView = new InserisciResocontoView();

    private Persona utenteCorrente = null;

    public AppController(Stage stage, EscursioniModel e, UtentiModel u, CertificazioniModel c, AdminModel a, PrenotazioniModel p, PostEscursioneModel post) {
        this.primaryStage    = stage;
        this.escursioniModel = e;
        this.utentiModel     = u;
        this.certsModel      = c;
        this.adminModel      = a;
        this.prenotModel     = p;
        this.postEscursioneModel  = post;
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
        collegaGestioneGeoView();
        collegaResocontoView();

        homeView.setTop5(escursioniModel.getTop5());
        exploreView.setEscursioni(escursioniModel.getAll());
        exploreView.setTipologie(escursioniModel.getTipologie());
        mainView.setAutenticato(false);
        mostraHome();
        mostraScenaMain();
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
            try {
                boolean haAbbonamentoAttivo = utentiModel.getUltimoAbbonamento(utenteCorrente).map(Abbonamento::isAttivo).orElse(false);
                homeView.setHaAbbonamentoAttivo(haAbbonamentoAttivo);
            } catch (Exception e) {
                homeView.setHaAbbonamentoAttivo(false);
            }
        } else {
            homeView.setHaAbbonamentoAttivo(false);
        }
    }

    private void mostraExplore() {
        mainView.setContenuto(exploreView.getRoot());
        mainView.setNavAttiva("explore");
    }

    private void mostraProfilo() {
        mainView.setContenuto(profiloView.getRoot());
        mainView.setNavAttiva("profilo");
        if (utenteCorrente != null) {
            boolean isGuida = utentiModel.verificaSeGuida(utenteCorrente.cf);
            profiloView.setUtente(utenteCorrente, isGuida);
            try {
                profiloView.setCertificazioni(certsModel.getCertificazioniUtente(utenteCorrente.cf));
                profiloView.setAbbonamento(utentiModel.getUltimoAbbonamento(utenteCorrente));
                profiloView.setPrenotazioni(prenotModel.getPrenotazioniUtente(utenteCorrente.cf));
                if (isGuida) {
                    profiloView.setResoconti(postEscursioneModel.getResocontiGuida(utenteCorrente.cf));
                }
            } catch (Exception ignored) {}
        }
    }

    private void mostraAdmin() {
        mainView.setModalitaUtente(false);
        mainView.setContenuto(adminView.getRoot());
        mainView.setNavAttiva("");

        try { adminView.setCertificazioniInAttesa(certsModel.getCertificazioniInAttesa()); } catch (Exception e) { adminView.setCertificazioniInAttesa(null); }
        try { adminView.setGuide(adminModel.getTutteLeGuide(), adminModel.getGuideSospendibili());} catch (Exception e) { adminView.setGuide(null, null); }
        try { adminView.setUtentiDaPremiare(adminModel.getUtentiDaPremiare()); } catch (Exception e) { adminView.setUtentiDaPremiare(null); }
        try { adminView.setRecensioniDaModerare(adminModel.getTutteLeRecensioni()); } catch (Exception e) { adminView.setRecensioniDaModerare(null); }
    }

    private void eseguiLogout() {
        utenteCorrente = null;
        mainView.setAutenticato(false);
        mainView.setUtente(null, false); 
        mainView.setModalitaUtente(false);
        homeView.setUtente(null); 
        mostraHome();
    }

    private void collegaAuthView() {
        authView.setOnLogin((email, password) ->
            utentiModel.getPersonaAutenticata(email, password).ifPresentOrElse(
                persona -> {
                    utenteCorrente = persona;
                    boolean isGuida = utentiModel.verificaSeGuida(persona.cf);
                    mainView.setUtente(persona, isGuida);
                    mainView.setAutenticato(true);
                    homeView.setUtente(persona);
                    profiloView.setUtente(persona, isGuida);

                    if (persona.tipoAmministratore) {
                        mostraAdmin();
                    } else {
                        mostraHome();
                    }
                },
                () -> authView.mostraErroreLogin("Email o password errati.")
            )
        );

        authView.setOnRegistra(campi -> {
            var nuovoUtente = new Persona(campi[2], campi[0], campi[1], true, false, 0, LocalDate.now(), null, campi[3], campi[4], false);
            utentiModel.registraUtente(nuovoUtente);
            authView.pulisciCampi();
            authView.mostraErroreLogin("");
        });
    }

    private void collegaMainView() {
        mainView.setOnHome(() -> mostraHome());
        mainView.setOnExplore(() -> mostraExplore());
        mainView.setOnProfilo(() -> {
            if (utenteCorrente == null) mostraLogin();
            else mostraProfilo();
        });
        mainView.setOnAdmin(() -> mostraAdmin());
        mainView.setOnPrenotaNuova(() -> mostraExplore());
        mainView.setOnLogin(() -> mostraLogin());
        mainView.setOnLogout(() -> {
            if (utenteCorrente != null) eseguiLogout();
            else mostraLogin();
        });
        
        mainView.setOnCreaEscursione(() -> {
            if (utenteCorrente == null) return;
            creaView.setGuidaCF(utenteCorrente.cf);
            creaView.setTipologieDisponibili(escursioniModel.getTipologie());
            creaView.setCertificazioniDisponibili(certsModel.getTipologieDisponibili());
            mainView.setContenuto(creaView.getRoot());
        });

        mainView.setOnInserisciResoconto(() -> {
            if (utenteCorrente == null) return;
            resocontoView.setCfGuida(utenteCorrente.cf);
            resocontoView.setEscursioni(escursioniModel.getEscursioniGuida(utenteCorrente.cf));
            resocontoView.pulisciForm();
            mainView.setContenuto(resocontoView.getRoot());
        });

        mainView.setOnCreaEscursione(() -> {
            if (utenteCorrente == null) return;
            creaView.setGuidaCF(utenteCorrente.cf);
            creaView.setTipologieDisponibili(escursioniModel.getTipologie());
            creaView.setCertificazioniDisponibili(certsModel.getTipologieDisponibili());
            
            creaView.setProviderPaesi(() -> escursioniModel.getPaesi());
            creaView.setProviderZone(paese -> escursioniModel.getZonePerPaese(paese));
            creaView.setProviderLuoghi((paese, zona) -> escursioniModel.getLuoghiPerZona(paese, zona));
            
            mainView.setContenuto(creaView.getRoot());
        });
    }

    private void mostraLogin() {
        mainView.setContenuto(authView.getRoot());
        mainView.setNavAttiva("");
    }

    private void collegaHomeView() {
        homeView.setOnEscursioneClick(preview -> apriDettaglio(preview));
        homeView.setOnMeseClick(mese -> homeView.setEscursioniMese(escursioniModel.getEscursioniPerMese(mese)));
        homeView.setOnExploreClick(() -> mostraExplore());
        homeView.setOnUpgradeClick(() -> {
            if (utenteCorrente == null) mostraLogin();
            else mainView.setContenuto(premiumView.getRoot());
        });
    }

    private void collegaExploreView() {
        exploreView.setOnEscursioneClick(preview -> apriDettaglio(preview));
        exploreView.setOnFiltraTipologia(tip -> exploreView.setEscursioni(escursioniModel.getEscursionePerTipologia(tip)));
        exploreView.setOnRicerca(query -> {
            var filtrati = escursioniModel.getAll().stream().filter(e -> e.titolo.toLowerCase().contains(query.toLowerCase())).toList();
            exploreView.setEscursioni(filtrati);
        });
        exploreView.setOnFiltraReset(() -> exploreView.setEscursioni(escursioniModel.getAll()));
    }

    private void apriDettaglio(it.unibo.destinationbuddy.data.EscursionePreview preview) {
        escursioniModel.getDettaglio(preview).ifPresent(exc -> {
            dettaglioView.setEscursione(exc);
            java.util.List<it.unibo.destinationbuddy.data.Recensione> listaRecensioni = 
                prenotModel.getRecensioniPerEscursione(exc.idEscursione);
            boolean puoRecensire = false;
            String cfUtenteLoggato = "";

            if (utenteCorrente != null && !utenteCorrente.tipoAmministratore) { 
                cfUtenteLoggato = utenteCorrente.cf;
                boolean haPrenotato = prenotModel.getPrenotazioniUtente(utenteCorrente.cf).stream()
                        .anyMatch(p -> p.idEscursione.equals(exc.idEscursione) && 
                                ("Confermata".equalsIgnoreCase(p.stato) || "Completata".equalsIgnoreCase(p.stato)));
                boolean escursioneTerminata = false;
                if (exc.giornate != null && !exc.giornate.isEmpty()) {
                    LocalDate dataUltimoGiorno = exc.giornate.get(exc.giornate.size() - 1).data;
                    escursioneTerminata = LocalDate.now().isAfter(dataUltimoGiorno);
                }

                puoRecensire = haPrenotato && escursioneTerminata;
            }
            dettaglioView.setRecensioni(listaRecensioni, puoRecensire, exc.idEscursione, cfUtenteLoggato);
            mainView.setContenuto(dettaglioView.getRoot());
        });
    }

    private void collegaDettaglioView() {
        dettaglioView.setOnIndietro(() -> mostraExplore());
        dettaglioView.setOnPrenota(exc -> {
            if (utenteCorrente == null) {
                mostraLogin();
                return;
            }

            LocalDate oggi = LocalDate.now();
            LocalDate aperturaNormale = exc.dataAperturaEscursione;
            LocalDate chiusura = exc.dataChiusuraEscursione;
            
            boolean isPremium = utentiModel.getUltimoAbbonamento(utenteCorrente)
                                           .map(it.unibo.destinationbuddy.data.Abbonamento::isAttivo)
                                           .orElse(false);

            LocalDate aperturaEffettiva = isPremium ? aperturaNormale.minusDays(15) : aperturaNormale;

            if (oggi.isAfter(chiusura)) {
                mostraAvviso("Iscrizioni Chiuse", "Spiacenti, le iscrizioni per questa escursione sono terminate il " + chiusura + ".");
                return;
            }

            if (oggi.isBefore(aperturaEffettiva)) {
                if (isPremium) {
                    mostraAvviso("Accesso Anticipato", "Hai il Premium, ma è troppo presto!\nLe iscrizioni in accesso anticipato apriranno il " + aperturaEffettiva + ".");
                } else {
                    mostraAvviso("Iscrizioni Chiuse", "Le iscrizioni standard apriranno il " + aperturaNormale + ".\n\n👑 Passa a Premium per sbloccare l'accesso anticipato dal " + aperturaNormale.minusDays(15) + "!");
                }
                return;
            }

            int posti = prenotModel.getPostiRimanenti(exc.idEscursione);
            double sconto = prenotModel.getScontoNoleggio(utenteCorrente.cf);
            bookingView.setEscursione(exc, utenteCorrente, posti, sconto);
            mainView.setContenuto(bookingView.getRoot());
        });

        dettaglioView.setOnInviaRecensione(nuovaRecensione -> {
            boolean salvata = prenotModel.inserisciRecensione(nuovaRecensione);
            if (salvata) {
                var previewFinta = new it.unibo.destinationbuddy.data.EscursionePreview(
                    nuovaRecensione.idEscursione, "", "", 0.0
                );
                apriDettaglio(previewFinta);
            }
        });
    }

    private void collegaBookingView() {
        bookingView.setOnIndietro(() -> mainView.setContenuto(dettaglioView.getRoot()));
        bookingView.setOnTornaEsplora(() -> mostraExplore());
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
            for (var entry : equipSel.entrySet()) {
                if (entry.getValue()) {
                    String idPezzo = prenotModel.trovaPezzoDisponibile(entry.getKey());
                    if (idPezzo != null) prenotModel.noleggiaPezzo(idPezzo, idEscursione, utenteCorrente.cf, 1);
                }
            }
            bookingView.mostraConferma();
        });
    }

    private void collegaAdminView() {
        adminView.setOnValidaCert((idCert, nCert) -> {
            try { certsModel.validaCertificazione(idCert, nCert); } catch (Exception ignored) {}
            mostraAdmin();
        });
        
        adminView.setOnAttivaGuida(p -> {
            try { adminModel.attivaGuida(p); } catch (Exception ignored) {}
            mostraAdmin(); 
        });
        
        adminView.setOnDisattivaGuida(p -> {
            try { adminModel.disattivaGuida(p); } catch (Exception ignored) {}
            mostraAdmin(); 
        });

        adminView.setOnEliminaRecensione(recensione -> {
            try { adminModel.eliminaRecensione(recensione.cf, recensione.idEscursione); } catch (Exception ignored) {}
            mostraAdmin();
        });

        adminView.setOnApriGestioneGeo(() -> {
            gestioneGeoView.pulisciForm();
            gestioneGeoView.setPaesi(escursioniModel.getPaesi());
            gestioneGeoView.setCategorie(escursioniModel.getCategorieLuoghi());
            mainView.setContenuto(gestioneGeoView.getRoot());
        });
        adminView.setOnVaiComeUtente(() -> {
            mainView.setModalitaUtente(true);
            mostraHome();
        });
    }

    private void collegaProfiloView() {
        profiloView.setOnAggiungiCert(() -> {
            if (utenteCorrente == null) return;
            aggCertView.setCfUtente(utenteCorrente.cf);
            aggCertView.setTipologie(certsModel.getTipologieDisponibili());
            mainView.setContenuto(aggCertView.getRoot());
        });
        profiloView.setOnVaiPremium(() -> mainView.setContenuto(premiumView.getRoot()));
    }

    private void collegaCreaView() {
        creaView.setOnAnnulla(() -> {
            creaView.pulisciForm();
            mostraProfilo();
        });

        creaView.setOnCrea(formData -> {
            try {
                escursioniModel.creaEscursione(
                    formData.escursione, 
                    formData.descrizione, 
                    formData.numeroPartecipanti, 
                    formData.guidaCF, 
                    formData.tipologie,
                    formData.certificazioniSelezionate,
                    formData.nuovaCertificazione
                );
                
                exploreView.setEscursioni(escursioniModel.getAll());
                
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Escursione Pubblicata");
                alert.setHeaderText(null);
                alert.setContentText("L'escursione \"" + formData.escursione.titolo + "\" è stata pubblicata con successo ed è ora visibile nel catalogo!");
                alert.showAndWait();

                creaView.pulisciForm();
                mostraProfilo();

            } catch (Exception e) {
                javafx.scene.control.Alert alertErrore = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alertErrore.setTitle("Errore di Salvataggio");
                alertErrore.setHeaderText(null);
                alertErrore.setContentText("Non è stato possibile pubblicare l'escursione. Controlla che i dati siano corretti.");
                alertErrore.showAndWait();
                e.printStackTrace();
            }
        });
    }

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

    private void collegaPremiumView() {
        premiumView.setOnIndietro(() -> mostraHome());
        premiumView.setOnScegliPiano(piano -> {
            if (utenteCorrente == null) {
                mostraLogin();
                return;
            }
            boolean ok = utentiModel.sottoscriviAbbonamento(piano.prezzoMensile, piano.mesi, utenteCorrente.cf);
            if (!ok) {
                premiumView.mostraErrore("Hai già sottoscritto un abbonamento oggi.");
                return;
            }
            mostraProfilo();
        });
    }

private void collegaGestioneGeoView() {
        gestioneGeoView.setOnAnnulla(() -> mostraAdmin());
        
        gestioneGeoView.setOnPaeseSelezionato(paese -> {
            gestioneGeoView.setZone(escursioniModel.getZonePerPaese(paese));
        });

        gestioneGeoView.setOnSalvaLuogo(luogo -> {
            try {
                escursioniModel.aggiungiLuogoEsplorabile(luogo);
                mostraAvviso("Successo", "Luogo salvato nel database!");
                gestioneGeoView.pulisciForm();
                mostraAdmin(); 
            } catch (Exception e) { mostraAvviso("Errore", "Impossibile salvare il Luogo."); }
        });

        gestioneGeoView.setOnSalvaPaese(paese -> {
            try {
                escursioniModel.aggiungiPaese(paese);
                mostraAvviso("Successo", "Paese '" + paese + "' salvato nel database!");
                gestioneGeoView.pulisciForm();
                gestioneGeoView.setPaesi(escursioniModel.getPaesi());
            } catch (Exception e) { mostraAvviso("Errore", "Impossibile salvare il Paese (forse esiste già?)."); }
        });

        gestioneGeoView.setOnSalvaZona(dati -> {
            try {
                escursioniModel.aggiungiZona(dati[0], dati[1], dati[2]);
                mostraAvviso("Successo", "Zona '" + dati[1] + "' salvata nel database!");
                gestioneGeoView.pulisciForm();
            } catch (Exception e) { mostraAvviso("Errore", "Impossibile salvare la Zona."); }
        });

        gestioneGeoView.setOnSalvaCategoria(categoria -> {
            try {
                escursioniModel.aggiungiCategoria(categoria);
                mostraAvviso("Successo", "Categoria '" + categoria + "' salvata nel database!");
                gestioneGeoView.pulisciForm();
                gestioneGeoView.setCategorie(escursioniModel.getCategorieLuoghi());
            } catch (Exception e) { mostraAvviso("Errore", "Impossibile salvare la Categoria (forse esiste già?)."); }
        });
    }

    private void collegaResocontoView() {
        resocontoView.setOnAnnulla(() -> mostraProfilo());
        resocontoView.setOnSalva(r -> {
            boolean ok = postEscursioneModel.inserisciResoconto(r);
            if (ok) {
                resocontoView.mostraConferma();
            } else {
                resocontoView.mostraErrore("Errore durante il salvataggio. Riprova.");
            }
        });
    }

    private void applicaCSS(Scene scene) {
        var css = getClass().getResource("/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    private void mostraAvviso(String titolo, String messaggio) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}