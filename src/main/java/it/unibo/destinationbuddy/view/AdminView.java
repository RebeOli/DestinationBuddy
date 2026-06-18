package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.function.Consumer;

/**
 * AdminView — pannello amministratore: certificazioni in attesa, guide, premi.
 *
 * UTILIZZO DAL CONTROLLER:
 *   AdminView view = new AdminView();
 *   view.setCertificazioniInAttesa(lista);
 *   view.setGuide(lista);
 *   view.setUtentiDaPremiare(lista);
 *   view.setOnValidaCert(nCert -> controller.valida(nCert));
 *   view.setOnAttivaGuida(p -> controller.attiva(p));
 *   view.setOnDisattivaGuida(p -> controller.disattiva(p));
 *   root.setCenter(view.getRoot());
 */
public class AdminView {

    private static final String DARK_BG   = "#0E2A1A";
    private static final String CARD_BG   = "#152E1C";
    private static final String ACCENT    = "#D4673A";
    private static final String TEXT_MUTED = "#A0B8AA";

    private Consumer<String>  onValidaCert    = s -> {};
    private Consumer<Persona> onAttivaGuida   = p -> {};
    private Consumer<Persona> onDisattivaGuida = p -> {};

    private final ScrollPane root;
    private final VBox certsContainer  = new VBox(10);
    private final VBox guideContainer  = new VBox(10);
    private final VBox premiContainer  = new VBox(10);

    public AdminView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");

        Label titolo = new Label("Pannello amministratore");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 22));
        titolo.setTextFill(Color.WHITE);

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
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
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

    public void setGuide(List<Persona> lista) {
        guideContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            guideContainer.getChildren().add(muted("Nessuna guida registrata."));
            return;
        }
        for (Persona p : lista) {
            guideContainer.getChildren().add(buildGuidaRow(p));
        }
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

    public void setOnValidaCert(Consumer<String> handler)     { this.onValidaCert     = handler; }
    public void setOnAttivaGuida(Consumer<Persona> handler)   { this.onAttivaGuida    = handler; }
    public void setOnDisattivaGuida(Consumer<Persona> handler){ this.onDisattivaGuida = handler; }
    public ScrollPane getRoot()                                { return root; }

    // ── Righe ─────────────────────────────────────────────────────────────────

    private HBox buildCertRow(Certificazione c) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setStyle(styleRow());

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        String tipo = c.tipologia != null
                ? c.tipologia.idCertificazione + " — " + c.tipologia.livello : "—";
        Label tipoLbl = new Label(tipo);
        tipoLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        tipoLbl.setTextFill(Color.WHITE);
        Label sub = new Label("Codice: " + c.nCertificazione
                + "  ·  Ente: " + c.enteRilasciante
                + "  ·  CF: " + c.cf);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(tipoLbl, sub);

        Button approvaBtn = smallBtn("✓ Valida", "#4AC582", "rgba(74,197,130,0.2)");
        approvaBtn.setOnAction(e -> {
            onValidaCert.accept(c.nCertificazione);
            approvaBtn.setText("✓ Validata");
            approvaBtn.setDisable(true);
        });

        row.getChildren().addAll(info, approvaBtn);
        return row;
    }

    private HBox buildGuidaRow(Persona p) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setStyle(styleRow());

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.WHITE);
        Label sub = new Label("CF: " + p.cf + "  ·  Stato: " +
                (p.statoAccount == null || p.statoAccount.isEmpty() ? "—" : p.statoAccount));
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(nomeLbl, sub);

        boolean attiva = "attivo".equalsIgnoreCase(p.statoAccount);
        Button toggleBtn = smallBtn(
                attiva ? "Disattiva" : "Attiva",
                ACCENT,
                "rgba(212,103,58,0.2)");
        toggleBtn.setOnAction(e -> {
            if (attiva) onDisattivaGuida.accept(p);
            else onAttivaGuida.accept(p);
        });

        row.getChildren().addAll(info, toggleBtn);
        return row;
    }

    private HBox buildPremioRow(Persona p) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setStyle("-fx-background-color: rgba(255,215,0,0.06); -fx-background-radius: 10;"
                + "-fx-border-color: rgba(255,215,0,0.2); -fx-border-radius: 10; -fx-border-width: 0.5;");

        Label icon = new Label("🏅");
        icon.setFont(Font.font(22));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.WHITE);
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
        section.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 12; -fx-border-width: 0.5;");
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 15));
        lbl.setTextFill(Color.WHITE);
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

    private String styleRow() {
        return "-fx-background-color: #0E2A1A; -fx-background-radius: 10;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 10; -fx-border-width: 0.5;";
    }
}
