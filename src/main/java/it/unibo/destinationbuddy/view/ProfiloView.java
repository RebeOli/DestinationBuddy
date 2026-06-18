package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * ProfiloView — pagina profilo utente con certificazioni e info account.
 *
 * UTILIZZO DAL CONTROLLER:
 *   ProfiloView view = new ProfiloView();
 *   view.setUtente(persona);
 *   view.setCertificazioni(lista);
 *   view.setOnAggiungiCertificazione(c -> controller.aggiungi(c));
 *   view.setOnCreaEscursione(() -> controller.apriCreaEscursione()); // solo se guida
 *   root.setCenter(view.getRoot());
 */
public class ProfiloView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

    private Runnable                  onCreaEscursione        = () -> {};
    private Runnable                  onAggiungiCert          = () -> {};

    private final ScrollPane root;
    private final VBox        contentBox;

    // Label aggiornabili dinamicamente
    private final Label nomeLabel       = new Label();
    private final Label subLabel        = new Label();
    private final Label inizialeLabel   = new Label();
    private final VBox  certsContainer  = new VBox(10);

    public ProfiloView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));
        contentBox.setStyle("-fx-background-color: " + DARK_BG + ";");
        contentBox.getChildren().addAll(buildProfileHeader(), buildCertSection());

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    public void setUtente(Persona p) {
        nomeLabel.setText(p.nome + " " + p.cognome);
        subLabel.setText("Membro dal " + (p.dataIscrizione != null ? p.dataIscrizione.toString() : "—")
                + "  ·  " + p.escursioniEffettuate + " escursioni effettuate");
        inizialeLabel.setText(p.nome.isEmpty() ? "?" : String.valueOf(p.nome.charAt(0)).toUpperCase());

        // Mostra pulsante "Crea escursione" solo se è una guida (tipoUtente = false = guida nel vostro schema)
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

    public void setOnCreaEscursione(Runnable handler)   { this.onCreaEscursione = handler; }
    public void setOnAggiungiCert(Runnable handler)     { this.onAggiungiCert   = handler; }
    public ScrollPane getRoot()                          { return root; }

    // ── Costruzione UI ────────────────────────────────────────────────────────

    private HBox buildProfileHeader() {
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 14; -fx-border-width: 0.5;");

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
        nomeLabel.setTextFill(Color.WHITE);
        subLabel.setFont(Font.font("System", 13));
        subLabel.setTextFill(Color.web(TEXT_MUTED));
        textBox.getChildren().addAll(nomeLabel, subLabel);

        // Pulsante crea escursione (visibile solo per guide)
        Button creaBtn = new Button("+ Nuova escursione");
        creaBtn.setId("btn-crea");
        creaBtn.setFont(Font.font("System", FontWeight.BOLD, 13));
        styleAccentBtn(creaBtn);
        creaBtn.setOnAction(e -> onCreaEscursione.run());
        creaBtn.setVisible(false); // nascosto di default, setUtente lo mostra se guida

        header.getChildren().addAll(avatar, textBox, creaBtn);
        return header;
    }

    private VBox buildCertSection() {
        VBox section = new VBox(14);
        section.setPadding(new Insets(18));
        section.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 12; -fx-border-width: 0.5;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label titolo = new Label("Le mie certificazioni");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        titolo.setTextFill(Color.WHITE);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addBtn = new Button("+ Aggiungi");
        addBtn.setFont(Font.font("System", 12));
        styleAccentBtn(addBtn);
        addBtn.setOnAction(e -> onAggiungiCert.run());
        header.getChildren().addAll(titolo, spacer, addBtn);

        section.getChildren().addAll(header, certsContainer);
        return section;
    }

    private HBox buildCertCard(Certificazione c) {
        HBox card = new HBox(14);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #0E2A1A; -fx-background-radius: 10;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 10; -fx-border-width: 0.5;");

        Label icon = new Label("🏅");
        icon.setFont(Font.font(20));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        String tipoTesto = c.tipologia != null
                ? c.tipologia.idCertificazione + " — Livello: " + c.tipologia.livello
                : "Certificazione";
        Label tipoLbl = new Label(tipoTesto);
        tipoLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        tipoLbl.setTextFill(Color.WHITE);

        String details = "Codice: " + c.nCertificazione
                + "  ·  Ente: " + c.enteRilasciante
                + "  ·  Valida: " + (c.dataRilascio != null ? c.dataRilascio : "?")
                + " → " + (c.dataScadenza != null ? c.dataScadenza : "?");
        Label detailsLbl = new Label(details);
        detailsLbl.setFont(Font.font("System", 11));
        detailsLbl.setTextFill(Color.web(TEXT_MUTED));

        info.getChildren().addAll(tipoLbl, detailsLbl);

        // Badge stato validazione
        boolean valida = "validata".equalsIgnoreCase(c.statoValidazione);
        Label stato = new Label(valida ? "✓ Validata" : "⏳ In attesa");
        stato.setFont(Font.font("System", FontWeight.BOLD, 11));
        stato.setTextFill(Color.web(valida ? "#4AC582" : "#F59E0B"));
        stato.setStyle("-fx-background-color: " + (valida ? "rgba(74,197,130,0.15)" : "rgba(245,158,11,0.15)")
                + "; -fx-background-radius: 4; -fx-padding: 3 8;");

        card.getChildren().addAll(icon, info, stato);
        return card;
    }

    private void styleAccentBtn(Button btn) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 8 16;";
        String hover = "-fx-background-color: " + ACCENT_HOVER + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 8 16;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }
}
