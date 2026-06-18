package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Equipaggiamento;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.Giornata;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

/**
 * DettaglioEscursioneView — mostra tutti i dati di un'Escursione completa.
 *
 * UTILIZZO DAL CONTROLLER:
 *   DettaglioEscursioneView view = new DettaglioEscursioneView();
 *   view.setEscursione(escursione);
 *   view.setPostiRimanenti(n);
 *   view.setOnPrenota(exc -> controller.avviaPrenotazione(exc));
 *   view.setOnIndietro(() -> controller.tornaExplore());
 *   root.setCenter(view.getRoot());
 */
public class DettaglioEscursioneView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";
    private static final String PURPLE_BG    = "rgba(167,139,250,0.12)";
    private static final String PURPLE_TEXT  = "#C4B5FD";

    private Consumer<Escursione> onPrenota  = e -> {};
    private Runnable             onIndietro = () -> {};

    private final ScrollPane root;
    private final VBox        contentBox;

    public DettaglioEscursioneView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));
        contentBox.setStyle("-fx-background-color: " + DARK_BG + ";");

        Label placeholder = new Label("Seleziona un'escursione dalla lista.");
        placeholder.setFont(Font.font("System", 13));
        placeholder.setTextFill(Color.web(TEXT_MUTED));
        contentBox.getChildren().add(placeholder);

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    /** Riempie la view con tutti i dati dell'escursione. */
    public void setEscursione(Escursione exc) {
        contentBox.getChildren().clear();

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Esplora");
        back.setFont(Font.font("System", 12));
        back.setTextFill(Color.web(ACCENT));
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        Label sep = new Label("›");
        sep.setTextFill(Color.web(TEXT_MUTED));
        Label current = new Label(exc.titolo);
        current.setFont(Font.font("System", 12));
        current.setTextFill(Color.web(TEXT_MUTED));
        breadcrumb.getChildren().addAll(back, sep, current);

        // Header
        VBox header = new VBox(8);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.setFont(Font.font("System", FontWeight.BOLD, 26));
        titoloLbl.setTextFill(Color.WHITE);
        titoloLbl.setWrapText(true);

        HBox badges = new HBox(8);
        badges.getChildren().add(pill(exc.difficolta, ACCENT, "rgba(212,103,58,0.18)"));
        for (String tip : exc.tipologie) {
            badges.getChildren().add(pill(tip, "#60A5FA", "rgba(96,165,250,0.15)"));
        }
        header.getChildren().addAll(titoloLbl, badges);

        // Info grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(14));
        infoGrid.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 10;");

        addInfoCell(infoGrid, "Guida", exc.guidaNome + " " + exc.guidaCognome, 0, 0);
        addInfoCell(infoGrid, "Posti disponibili", String.valueOf(exc.postiDisponibili), 1, 0);
        addInfoCell(infoGrid, "Costo iscrizione", String.format("€ %.2f", exc.costo), 2, 0);
        addInfoCell(infoGrid, "Apertura iscrizioni",
                exc.dataAperturaEscursione != null ? exc.dataAperturaEscursione.toString() : "—", 0, 1);
        addInfoCell(infoGrid, "Chiusura iscrizioni",
                exc.dataChiusuraEscursione != null ? exc.dataChiusuraEscursione.toString() : "—", 1, 1);
        addInfoCell(infoGrid, "Difficoltà", exc.difficolta, 2, 1);

        // Certificazioni richieste
        VBox certBox = buildSection("🏅 Certificazioni richieste");
        if (exc.certificazioniRichieste == null || exc.certificazioniRichieste.isEmpty()) {
            certBox.getChildren().add(muted("Nessuna certificazione richiesta."));
        } else {
            FlowPane certBadges = new FlowPane(8, 6);
            certBadges.setPrefWrapLength(700);
            for (TipologiaCertificazione c : exc.certificazioniRichieste) {
                certBadges.getChildren().add(pill(c.idCertificazione + " – " + c.livello,
                        PURPLE_TEXT, PURPLE_BG));
            }
            certBox.getChildren().add(certBadges);
        }

        // Programma giornate
        VBox giornateBox = buildSection("📅 Programma");
        if (exc.giornate == null || exc.giornate.isEmpty()) {
            giornateBox.getChildren().add(muted("Nessuna giornata definita."));
        } else {
            for (Giornata g : exc.giornate) {
                giornateBox.getChildren().add(buildGiornataRow(g));
            }
        }

        // Equipaggiamento minimo
        VBox equipBox = buildSection("🎒 Equipaggiamento minimo");
        if (exc.equipaggiamento == null || exc.equipaggiamento.isEmpty()) {
            equipBox.getChildren().add(muted("Nessun equipaggiamento specificato."));
        } else {
            FlowPane equipBadges = new FlowPane(8, 6);
            equipBadges.setPrefWrapLength(700); 
            for (Equipaggiamento eq : exc.equipaggiamento) {
                equipBadges.getChildren().add(
                        pill(eq.toString(), "#4AC582", "rgba(74,197,130,0.15)"));
            }
            equipBox.getChildren().add(equipBadges);
        }

        // Pulsanti azione
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);
        Button prenotaBtn = new Button("Prenota questa escursione");
        prenotaBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(prenotaBtn, 12, 24);
        prenotaBtn.setOnAction(e -> onPrenota.accept(exc));

        Button indietroBtn = new Button("← Torna all'elenco");
        indietroBtn.setFont(Font.font("System", 13));
        styleGhostBtn(indietroBtn);
        indietroBtn.setOnAction(e -> onIndietro.run());

        actions.getChildren().addAll(prenotaBtn, indietroBtn);

        contentBox.getChildren().addAll(breadcrumb, header, infoGrid,
                certBox, giornateBox, equipBox, actions);
    }

    /** Aggiorna il numero di posti rimanenti (chiamato dopo conferma prenotazione). */
    public void setPostiRimanenti(int posti) {
        // Trovare la cella e aggiornare — in una View reale useremmo un binding
        // Per semplicità basta richiamare setEscursione con l'escursione aggiornata
    }

    public void setOnPrenota(Consumer<Escursione> handler)  { this.onPrenota  = handler; }
    public void setOnIndietro(Runnable handler)              { this.onIndietro = handler; }
    public ScrollPane getRoot()                              { return root; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private VBox buildSection(String titolo) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 10;");
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.WHITE);
        box.getChildren().add(lbl);
        return box;
    }

    private HBox buildGiornataRow(Giornata g) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(6, 0, 6, 0));
        row.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.07) transparent;"
                + "-fx-border-width: 0 0 0.5 0;");
        Label num = new Label("Giorno " + g.data);
        num.setFont(Font.font("System", FontWeight.BOLD, 13));
        num.setTextFill(Color.web(ACCENT));
        num.setMinWidth(80);
        Label desc = new Label(g.programma != null ? g.programma : "");
        desc.setFont(Font.font("System", 13));
        desc.setTextFill(Color.web("#C0C8C4"));
        desc.setWrapText(true);
        row.getChildren().addAll(num, desc);
        return row;
    }

    private void addInfoCell(GridPane grid, String label, String value, int col, int row) {
        VBox cell = new VBox(3);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        Label val = new Label(value);
        val.setFont(Font.font("System", FontWeight.BOLD, 14));
        val.setTextFill(Color.WHITE);
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, row);
    }

    private Label pill(String testo, String textColor, String bgColor) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", FontWeight.BOLD, 11));
        l.setTextFill(Color.web(textColor));
        l.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 4; -fx-padding: 3 8;");
        return l;
    }

    private Label muted(String testo) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        return l;
    }

    private void styleAccentBtn(Button btn, int padV, int padH) {
        String s = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8;"
                + "-fx-padding: " + padV + " " + padH + ";";
        String h = "-fx-background-color: " + ACCENT_HOVER + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8;"
                + "-fx-padding: " + padV + " " + padH + ";";
        btn.setStyle(s);
        btn.setOnMouseEntered(e -> btn.setStyle(h));
        btn.setOnMouseExited(e -> btn.setStyle(s));
    }

    private void styleGhostBtn(Button btn) {
        String s = "-fx-background-color: rgba(255,255,255,0.07); -fx-text-fill: white;"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 18;";
        btn.setStyle(s);
        btn.setOnMouseEntered(e -> btn.setStyle(s.replace("0.07", "0.14")));
        btn.setOnMouseExited(e -> btn.setStyle(s));
    }
}
