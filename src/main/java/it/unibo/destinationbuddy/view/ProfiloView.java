package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.Prenotazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import it.unibo.destinationbuddy.data.Abbonamento;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import java.util.List;

/**
 * ProfiloView — pagina profilo utente con certificazioni, abbonamento e prenotazioni.
 * Totalmente basata su classi CSS per la UI (Light Theme).
 *
 * UTILIZZO DAL CONTROLLER:
 * ProfiloView view = new ProfiloView();
 * view.setUtente(persona);
 * view.setCertificazioni(lista);
 * view.setAbbonamento(abbonamentoOptional);
 * view.setPrenotazioni(lista);
 * view.setOnAggiungiCertificazione(c -> controller.aggiungi(c));
 * view.setOnCreaEscursione(() -> controller.apriCreaEscursione()); // solo se guida
 * root.setCenter(view.getRoot());
 */
public class ProfiloView {

    // ── VARIABILI LIGHT THEME (Per fallback in Java) ──────────────────────────
    private static final String APP_BG     = "#F4EFE6"; // Sabbia
    private static final String ACCENT     = "#B85D38"; // Terracotta
    private static final String TEXT_DARK  = "#2C2A26"; // Testo scuro
    private static final String TEXT_MUTED = "#807B73"; // Testo secondario
    // ──────────────────────────────────────────────────────────────────────────

    private Runnable onCreaEscursione = () -> {};
    private Runnable onAggiungiCert   = () -> {};
    private Runnable onVaiPremium     = () -> {};

    private final ScrollPane root;
    private final VBox        contentBox;

    // Label aggiornabili dinamicamente
    private final Label nomeLabel       = new Label();
    private final Label subLabel        = new Label();
    private final Label inizialeLabel   = new Label();
    private final VBox  certsContainer  = new VBox(10);
    private final VBox  abbonamentoBox  = new VBox(10);
    private final VBox  prenotazioniContainer = new VBox(10);
    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProfiloView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));
        contentBox.setStyle("-fx-background-color: " + APP_BG + ";");
        contentBox.getChildren().addAll(
                buildProfileHeader(),
                buildAbbonamentoSection(),
                buildPrenotazioniSection(),
                buildCertSection()
        );
        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + APP_BG + "; -fx-background-color: " + APP_BG + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    public void setUtente(Persona p) {
        nomeLabel.setText(p.nome + " " + p.cognome);
        subLabel.setText("Membro dal " + (p.dataIscrizione != null ? p.dataIscrizione.toString() : "—")
                + "  ·  " + p.escursioniEffettuate + " escursioni effettuate");
        inizialeLabel.setText(p.nome.isEmpty() ? "?" : String.valueOf(p.nome.charAt(0)).toUpperCase());

        // Mostra pulsante "Crea escursione" solo se è una guida
        boolean isGuida = !p.tipoUtente || (p.statoAccount != null && !p.statoAccount.isEmpty());
        contentBox.getChildren().stream()
                .filter(n -> "btn-crea".equals(n.getId()))
                .forEach(n -> n.setVisible(isGuida));
    }

    public void setCertificazioni(List<Certificazione> lista) {
        certsContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Nessuna certificazione ancora registrata.");
            empty.setFont(Font.font("System", 13));
            empty.setTextFill(Color.web(TEXT_MUTED));
            certsContainer.getChildren().add(empty);
            return;
        }
        for (Certificazione c : lista) {
            certsContainer.getChildren().add(buildCertCard(c));
        }
    }

    /** Popola la lista delle prenotazioni effettuate dall'utente. */
    public void setPrenotazioni(List<Prenotazione> lista) {
        prenotazioniContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Nessuna prenotazione effettuata.");
            empty.setFont(Font.font("System", 13));
            empty.setTextFill(Color.web(TEXT_MUTED));
            prenotazioniContainer.getChildren().add(empty);
            return;
        }
        for (Prenotazione p : lista) {
            prenotazioniContainer.getChildren().add(buildPrenotazioneCard(p));
        }
    }

    public void setOnCreaEscursione(Runnable handler)   { this.onCreaEscursione = handler; }
    public void setOnAggiungiCert(Runnable handler)     { this.onAggiungiCert   = handler; }
    public void setOnVaiPremium(Runnable handler)       { this.onVaiPremium     = handler; }
    public ScrollPane getRoot()                          { return root; }

    // ── Costruzione UI ────────────────────────────────────────────────────────

    private HBox buildProfileHeader() {
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.getStyleClass().add("profile-header"); // Usa il CSS

        // Avatar
        StackPane avatar = new StackPane();
        Circle circle = new Circle(32, Color.web(ACCENT));
        inizialeLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        inizialeLabel.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(circle, inizialeLabel);
        avatar.setMinSize(64, 64);
        avatar.setMaxSize(64, 64);

        // Testo
        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        nomeLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        nomeLabel.setTextFill(Color.web(TEXT_DARK));
        subLabel.setFont(Font.font("System", 13));
        subLabel.setTextFill(Color.web(TEXT_MUTED));
        textBox.getChildren().addAll(nomeLabel, subLabel);

        // Pulsante crea escursione (visibile solo per guide)
        Button creaBtn = new Button("+ Nuova escursione");
        creaBtn.setId("btn-crea");
        creaBtn.setFont(Font.font("System", FontWeight.BOLD, 13));
        creaBtn.getStyleClass().add("btn-accent"); // Usa il CSS
        creaBtn.setOnAction(e -> onCreaEscursione.run());
        creaBtn.setVisible(false);

        header.getChildren().addAll(avatar, textBox, creaBtn);
        return header;
    }

    private VBox buildCertSection() {
        VBox section = new VBox(14);
        section.setPadding(new Insets(18));
        section.getStyleClass().add("card"); // Usa il CSS

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label titolo = new Label("Le mie certificazioni");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        titolo.setTextFill(Color.web(TEXT_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Aggiungi");
        addBtn.setFont(Font.font("System", 12));
        addBtn.getStyleClass().add("btn-accent"); // Usa il CSS
        addBtn.setOnAction(e -> onAggiungiCert.run());

        header.getChildren().addAll(titolo, spacer, addBtn);

        section.getChildren().addAll(header, certsContainer);
        return section;
    }

    private HBox buildCertCard(Certificazione c) {
        HBox card = new HBox(14);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setAlignment(Pos.CENTER_LEFT);

        card.getStyleClass().add("cert-row");

        Label icon = new Label("🏅");
        icon.setFont(Font.font(20));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        String tipoTesto = c.tipologia != null
                ? c.tipologia.idCertificazione + " — Livello: " + c.tipologia.livello
                : "Certificazione";
        Label tipoLbl = new Label(tipoTesto);
        tipoLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        tipoLbl.setTextFill(Color.web(TEXT_DARK));

        String details = "Codice: " + c.nCertificazione
                + "  ·  Ente: " + c.enteRilasciante
                + "  ·  Valida: " + (c.dataRilascio != null ? c.dataRilascio : "?")
                + " → " + (c.dataScadenza != null ? c.dataScadenza : "?");
        Label detailsLbl = new Label(details);
        detailsLbl.setFont(Font.font("System", 11));
        detailsLbl.setTextFill(Color.web(TEXT_MUTED));

        info.getChildren().addAll(tipoLbl, detailsLbl);

        // Badge stato validazione con classi CSS Native
        boolean valida = "validata".equalsIgnoreCase(c.statoValidazione);
        Label stato = new Label(valida ? "✓ Validata" : "⏳ In attesa");

        // Aggiungiamo le classi ".badge" e il colore corretto
        stato.getStyleClass().add("badge");
        if (valida) {
            stato.getStyleClass().add("badge-green");
        } else {
            stato.getStyleClass().add("badge-amber");
        }

        card.getChildren().addAll(icon, info, stato);
        return card;
    }

    // ── Prenotazioni ─────────────────────────────────────────────────────────

    private VBox buildPrenotazioniSection() {
        VBox section = new VBox(14);
        section.setPadding(new Insets(18));
        section.getStyleClass().add("card");

        Label titolo = new Label("Le mie prenotazioni");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        titolo.setTextFill(Color.web(TEXT_DARK));

        section.getChildren().addAll(titolo, prenotazioniContainer);
        return section;
    }

    private HBox buildPrenotazioneCard(Prenotazione p) {
        HBox card = new HBox(14);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("cert-row");

        Label icon = new Label("🏔");
        icon.setFont(Font.font(20));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label titoloLbl = new Label(p.titoloEscursione);
        titoloLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        titoloLbl.setTextFill(Color.web(TEXT_DARK));

        String details = "Prenotata il " + (p.dataPrenotazione != null ? p.dataPrenotazione.format(DATA_FMT) : "?");
        Label detailsLbl = new Label(details);
        detailsLbl.setFont(Font.font("System", 11));
        detailsLbl.setTextFill(Color.web(TEXT_MUTED));

        info.getChildren().addAll(titoloLbl, detailsLbl);

        // Badge stato prenotazione
        Label stato = new Label(p.stato == null || p.stato.isEmpty() ? "—" : p.stato);
        stato.getStyleClass().add("badge");
        String statoLower = p.stato != null ? p.stato.toLowerCase() : "";
        if (statoLower.contains("saldat") || statoLower.contains("conferm")) {
            stato.getStyleClass().add("badge-green");
        } else if (statoLower.contains("attesa") || statoLower.contains("pending")) {
            stato.getStyleClass().add("badge-amber");
        } else {
            stato.getStyleClass().add("badge-blue");
        }

        card.getChildren().addAll(icon, info, stato);
        return card;
    }

    // ── Abbonamento ──────────────────────────────────────────────────────────

    public void setAbbonamento(Optional<Abbonamento> abbonamentoOpt) {
        abbonamentoBox.getChildren().clear();

        if (abbonamentoOpt == null || abbonamentoOpt.isEmpty()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("cert-row");
            row.setPadding(new Insets(14));

            VBox info = new VBox(3);
            HBox.setHgrow(info, Priority.ALWAYS);
            Label titolo = new Label("Nessun abbonamento attivo");
            titolo.setFont(Font.font("System", FontWeight.BOLD, 13));
            titolo.setTextFill(Color.web(TEXT_DARK));
            Label sub = new Label("Passa a Premium per sconti sul noleggio e prenotazioni prioritarie.");
            sub.setFont(Font.font("System", 11));
            sub.setTextFill(Color.web(TEXT_MUTED));
            info.getChildren().addAll(titolo, sub);

            Button upgradeBtn = new Button("Scopri Premium");
            upgradeBtn.getStyleClass().add("btn-accent");
            upgradeBtn.setOnAction(e -> onVaiPremium.run());

            row.getChildren().addAll(info, upgradeBtn);
            abbonamentoBox.getChildren().add(row);
            return;
        }

        Abbonamento a = abbonamentoOpt.get();
        boolean attivo = a.isAttivo();
        var dataScadenza = a.dataAbbonamento.plusMonths(a.durata);

        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("cert-row");
        row.setPadding(new Insets(14));

        Label icon = new Label("👑");
        icon.setFont(Font.font(20));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label titolo = new Label(String.format("Piano %d mes%s — €%.2f/mese",
                a.durata, a.durata == 1 ? "e" : "i", a.costoMensile));
        titolo.setFont(Font.font("System", FontWeight.BOLD, 13));
        titolo.setTextFill(Color.web(TEXT_DARK));

        String details = "Sottoscritto il " + a.dataAbbonamento.format(DATA_FMT)
                + "  ·  " + (attivo ? "Scade" : "Scaduto") + " il " + dataScadenza.format(DATA_FMT)
                + "  ·  Pagamento: " + a.dataPagamento.format(DATA_FMT);
        Label detailsLbl = new Label(details);
        detailsLbl.setFont(Font.font("System", 11));
        detailsLbl.setTextFill(Color.web(TEXT_MUTED));

        info.getChildren().addAll(titolo, detailsLbl);

        Label stato = new Label(attivo ? "✓ Attivo" : "Scaduto");
        stato.getStyleClass().add("badge");
        stato.getStyleClass().add(attivo ? "badge-green" : "badge-amber");

        row.getChildren().addAll(icon, info, stato);
        abbonamentoBox.getChildren().add(row);
    }

    private VBox buildAbbonamentoSection() {
        VBox section = new VBox(14);
        section.setPadding(new Insets(18));
        section.getStyleClass().add("card");

        Label titolo = new Label("Il mio abbonamento");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        titolo.setTextFill(Color.web(TEXT_DARK));

        section.getChildren().addAll(titolo, abbonamentoBox);
        return section;
    }
}
