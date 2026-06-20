package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Equipaggiamento;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.Giornata;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;

/**
 * DettaglioEscursioneView — mostra tutti i dati di un'Escursione completa.
 * Light Theme tramite classi CSS.
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

    private Consumer<Escursione> onPrenota  = e -> {};
    private Runnable             onIndietro = () -> {};

    private final ScrollPane root;
    private final VBox        contentBox;

    public DettaglioEscursioneView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));

        Label placeholder = new Label("Seleziona un'escursione dalla lista.");
        placeholder.getStyleClass().add("text-muted");
        contentBox.getChildren().add(placeholder);

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    // ── API pubblica ─────────────────────────────────────────────

    /** Riempie la view con tutti i dati dell'escursione. */
    public void setEscursione(Escursione exc) {
        contentBox.getChildren().clear();

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Esplora");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        Label sep = new Label("›");
        sep.getStyleClass().add("text-muted");
        Label current = new Label(exc.titolo);
        current.getStyleClass().add("text-muted");
        breadcrumb.getChildren().addAll(back, sep, current);

        // Header
        VBox header = new VBox(8);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.getStyleClass().add("auth-title");
        titoloLbl.setStyle("-fx-font-size: 26px;");
        titoloLbl.setWrapText(true);

        HBox badges = new HBox(8);
        badges.getChildren().add(pill(exc.difficolta, "badge-accent"));
        for (String tip : exc.tipologie) {
            badges.getChildren().add(pill(tip, "badge-blue"));
        }
        header.getChildren().addAll(titoloLbl, badges);

        // Info grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(14));
        infoGrid.getStyleClass().add("card");

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
                certBadges.getChildren().add(pill(c.idCertificazione + " – " + c.livello, "badge-purple"));
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
                equipBadges.getChildren().add(pill(eq.toString(), "badge-green"));
            }
            equipBox.getChildren().add(equipBadges);
        }

        // Pulsanti azione
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);
        Button prenotaBtn = new Button("Prenota questa escursione");
        prenotaBtn.getStyleClass().add("btn-accent");
        prenotaBtn.setOnAction(e -> onPrenota.accept(exc));

        Button indietroBtn = new Button("← Torna all'elenco");
        indietroBtn.getStyleClass().add("btn-ghost");
        indietroBtn.setOnAction(e -> onIndietro.run());

        actions.getChildren().addAll(prenotaBtn, indietroBtn);

        contentBox.getChildren().addAll(breadcrumb, header, infoGrid,
                certBox, giornateBox, equipBox, actions);
    }

    /** Aggiorna il numero di posti rimanenti (chiamato dopo conferma prenotazione). */
    public void setPostiRimanenti(int posti) {
        // Per semplicità basta richiamare setEscursione con l'escursione aggiornata
    }

    public void setOnPrenota(Consumer<Escursione> handler)  { this.onPrenota  = handler; }
    public void setOnIndietro(Runnable handler)              { this.onIndietro = handler; }
    public ScrollPane getRoot()                              { return root; }

    // ── Helpers ───────────────────────────────────────────────────

    private VBox buildSection(String titolo) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(14));
        box.getStyleClass().add("card");
        Label lbl = new Label(titolo);
        lbl.getStyleClass().add("card-title");
        box.getChildren().add(lbl);
        return box;
    }

    private HBox buildGiornataRow(Giornata g) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(6, 0, 6, 0));
        row.setStyle("-fx-border-color: transparent transparent -db-border transparent;"
                + "-fx-border-width: 0 0 1 0;");
        Label num = new Label("Giorno " + g.data);
        num.getStyleClass().add("text-accent");
        num.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-min-width: 80px;");
        Label desc = new Label(g.programma != null ? g.programma : "");
        desc.getStyleClass().add("sidebar-item-text");
        desc.setWrapText(true);
        row.getChildren().addAll(num, desc);
        return row;
    }

    private void addInfoCell(GridPane grid, String label, String value, int col, int row) {
        VBox cell = new VBox(3);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("text-muted");
        Label val = new Label(value);
        val.getStyleClass().add("cert-title");
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, row);
    }

    private Label pill(String testo, String badgeColorClass) {
        Label l = new Label(testo);
        l.getStyleClass().addAll("badge", badgeColorClass);
        return l;
    }

    private Label muted(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().add("text-muted");
        return l;
    }
}
