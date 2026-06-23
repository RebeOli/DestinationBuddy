package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * AdminView — pannello amministratore: certificazioni in attesa, guide, premi.
 * Aggiornata al Light Theme.
 *
 * UTILIZZO DAL CONTROLLER:
 * AdminView view = new AdminView();
 * view.setCertificazioniInAttesa(lista);
 * view.setGuide(lista);
 * view.setUtentiDaPremiare(lista);
 * // ⚡ MODIFICATO: Ora accetta e passa due parametri (idCert, nCert)
 * view.setOnValidaCert((idCert, nCert) -> controller.valida(idCert, nCert));
 * view.setOnAttivaGuida(p -> controller.attiva(p));
 * view.setOnDisattivaGuida(p -> controller.disattiva(p));
 * root.setCenter(view.getRoot());
 */
public class AdminView {

    // ── VARIABILI LIGHT THEME (Per fallback in Java) ──────────────────────────
    private static final String APP_BG     = "#F4EFE6"; // Sabbia
    private static final String ACCENT     = "#B85D38"; // Terracotta
    private static final String TEXT_DARK  = "#2C2A26"; // Testo scuro
    private static final String TEXT_MUTED = "#807B73"; // Testo secondario
    // ──────────────────────────────────────────────────────────────────────────

    private BiConsumer<String, String> onValidaCert = (id, n) -> {};    
    private Consumer<Persona> onAttivaGuida   = p -> {};
    private Consumer<Persona> onDisattivaGuida = p -> {};

    private final ScrollPane root;
    private final VBox certsContainer  = new VBox(10);
    private final VBox guideContainer  = new VBox(10);
    private final VBox premiContainer  = new VBox(10);

    public AdminView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + APP_BG + ";");

        Label titolo = new Label("Pannello amministratore");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titolo.setTextFill(Color.web(TEXT_DARK)); // Titolo scuro

        // Tab-like: tre sezioni con titolo espandibile
        page.getChildren().addAll(
                titolo,
                buildSection("📋 Certificazioni in attesa di verifica", certsContainer),
                buildSection("👤 Gestione guide", guideContainer),
                buildSection("🏆 Utenti da premiare (tutti i paesi)", premiContainer)
        );

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + APP_BG + "; -fx-background-color: " + APP_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    public void setCertificazioniInAttesa(List<Certificazione> lista) {
        certsContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            certsContainer.getChildren().add(muted("Nessuna certificazione in attesa."));
            return;
        }
        for (Certificazione c : lista) {
            certsContainer.getChildren().add(buildCertRow(c));
        }
    }

    public void setGuide(List<Persona> lista, List<String> cfSospendibili) {
        guideContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            guideContainer.getChildren().add(muted("Nessuna guida registrata."));
            return;
        }
        for (Persona p : lista) {
            // Controlliamo se la guida è nella "lista nera"
            boolean puoSospendere = cfSospendibili != null && cfSospendibili.contains(p.cf);
            
            guideContainer.getChildren().add(buildGuidaRow(p, puoSospendere));
        }
    }

    private HBox buildGuidaRow(Persona p, boolean puoSospendere) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.getStyleClass().add("cert-row"); 
        
        boolean attiva = p.statoAccount;
        String statoVisivo = attiva ? "Attivo" : "Sospeso";

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label("CF: " + p.cf + "  ·  Stato: " + statoVisivo);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(nomeLbl, sub);

        Button toggleBtn = smallBtn(
                attiva ? "Disattiva" : "Attiva",
                ACCENT,
                "#F9EAE1");
        
        if (attiva && !puoSospendere) {
            toggleBtn.setDisable(true); // Spegne il bottone
            toggleBtn.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #A0A0A0; -fx-background-radius: 6; -fx-padding: 5 12;");
            toggleBtn.setTooltip(new Tooltip("Servono >5 recensioni negative per sospendere questa guida."));
        }

        toggleBtn.setOnAction(e -> {
            if (attiva) onDisattivaGuida.accept(p);
            else onAttivaGuida.accept(p);
        });

        row.getChildren().addAll(info, toggleBtn);
        return row;
    }

    public void setUtentiDaPremiare(List<Persona> lista) {
        premiContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            premiContainer.getChildren().add(muted("Nessun utente ha completato tutti i paesi."));
            return;
        }
        for (Persona p : lista) {
            premiContainer.getChildren().add(buildPremioRow(p));
        }
    }

    public void setOnValidaCert(BiConsumer<String, String> handler) { this.onValidaCert = handler; }
    public void setOnAttivaGuida(Consumer<Persona> handler) { this.onAttivaGuida = handler; }
    public void setOnDisattivaGuida(Consumer<Persona> handler){ this.onDisattivaGuida = handler; }
    public ScrollPane getRoot() { return root; }

    // ── Righe ─────────────────────────────────────────────────────────────────

    private HBox buildCertRow(Certificazione c) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.getStyleClass().add("cert-row"); // Usa il CSS!

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        String tipo = c.tipologia != null
                ? c.tipologia.idCertificazione + " — " + c.tipologia.livello : "—";
        Label tipoLbl = new Label(tipo);
        tipoLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        tipoLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label("Codice: " + c.nCertificazione
                + "  ·  Ente: " + c.enteRilasciante
                + "  ·  CF: " + c.cf);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(tipoLbl, sub);

        // Bottone color verde pastello per validare
        Button approvaBtn = smallBtn("✓ Valida", "#155724", "#D4EDDA");
        approvaBtn.setOnAction(e -> {
            
            // ⚡ MODIFICA QUI: Estraiamo l'ID e passiamo ENTRAMBI i valori alla lambda function!
            String idCert = (c.tipologia != null) ? c.tipologia.idCertificazione : "";
            onValidaCert.accept(idCert, c.nCertificazione);
            
            approvaBtn.setText("✓ Validata");
            approvaBtn.setDisable(true);
        });

        row.getChildren().addAll(info, approvaBtn);
        return row;
    }

    private HBox buildPremioRow(Persona p) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        // Sfondo giallino tenue per i premiati
        row.setStyle("-fx-background-color: #FFF9E6; -fx-background-radius: 8;"
                + "-fx-border-color: #FDE68A; -fx-border-radius: 8; -fx-border-width: 1;");

        Label icon = new Label("🏅");
        icon.setFont(Font.font(22));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label(p.escursioniEffettuate + " escursioni · CF: " + p.cf);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(nomeLbl, sub);

        row.getChildren().addAll(icon, info);
        return row;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private VBox buildSection(String titolo, VBox container) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(16));
        section.getStyleClass().add("card"); // Usa il CSS per le ombre e lo sfondo bianco!
        
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbl.setTextFill(Color.web(TEXT_DARK));
        
        section.getChildren().addAll(lbl, container);
        return section;
    }

    private Button smallBtn(String text, String textColor, String bgColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.BOLD, 11));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor
                + "; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12;");
        return btn;
    }

    private Label muted(String testo) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        return l;
    }
}