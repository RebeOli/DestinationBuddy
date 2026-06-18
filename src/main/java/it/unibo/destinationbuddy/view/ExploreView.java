package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.TipologiaEscursione;
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
 * ExploreView — griglia di tutte le escursioni con ricerca e filtri per tipologia.
 *
 * UTILIZZO DAL CONTROLLER:
 *   ExploreView view = new ExploreView();
 *   view.setEscursioni(lista);
 *   view.setTipologie(tipologieList);
 *   view.setOnEscursioneClick(exc -> controller.apriDettaglio(exc));
 *   view.setOnFiltraPerTipologia(tip -> controller.filtra(tip));
 *   view.setOnRicerca(query -> controller.ricerca(query));
 *   root.setCenter(view.getRoot());
 */
public class ExploreView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

    private Consumer<EscursionePreview>  onEscursioneClick   = e -> {};
    private Consumer<TipologiaEscursione> onFiltraTipologia  = t -> {};
    private Consumer<String>             onRicerca           = q -> {};
    private Runnable                     onFiltraReset       = () -> {};

    private final ScrollPane root;
    private final FlowPane   cardsPane;
    private final HBox       filtriBar;
    private TipologiaEscursione tipologiaAttiva = null;

    public ExploreView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");

        // Titolo
        Label titolo = new Label("Esplora escursioni");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 22));
        titolo.setTextFill(Color.WHITE);

        // Barra ricerca
        TextField searchField = new TextField();
        searchField.setPromptText("Cerca per titolo o difficoltà...");
        searchField.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-text-fill: white;"
                + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 8 14;");
        searchField.setPrefWidth(260);
        searchField.textProperty().addListener((obs, old, val) -> onRicerca.accept(val));

        // Filtri tipologia
        filtriBar = new HBox(8);
        filtriBar.setAlignment(Pos.CENTER_LEFT);

        Button btnTutte = filterBtn("Tutte", null);
        filtriBar.getChildren().add(btnTutte);

        HBox topBar = new HBox(12, searchField, filtriBar);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Griglia cards
        cardsPane = new FlowPane();
        cardsPane.setHgap(14);
        cardsPane.setVgap(14);
        cardsPane.setPrefWrapLength(900);

        page.getChildren().addAll(titolo, topBar, cardsPane);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    /** Popola la griglia con le escursioni (tutte o filtrate). */
    public void setEscursioni(List<EscursionePreview> lista) {
        cardsPane.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            Label empty = new Label("Nessuna escursione trovata.");
            empty.setFont(Font.font("System", 13));
            empty.setTextFill(Color.web(TEXT_MUTED));
            cardsPane.getChildren().add(empty);
            return;
        }
        for (EscursionePreview exc : lista) {
            cardsPane.getChildren().add(buildCard(exc));
        }
    }

    /** Aggiunge i bottoni filtro per ogni tipologia disponibile nel DB. */
    public void setTipologie(List<TipologiaEscursione> tipologie) {
        // Mantieni il pulsante "Tutte" già esistente
        if (filtriBar.getChildren().size() > 1) {
            filtriBar.getChildren().subList(1, filtriBar.getChildren().size()).clear();
        }
        if (tipologie == null) return;
        for (TipologiaEscursione t : tipologie) {
            filtriBar.getChildren().add(filterBtn(t.idTipologia, t));
        }
    }

    public void setOnEscursioneClick(Consumer<EscursionePreview> handler)    { this.onEscursioneClick  = handler; }
    public void setOnFiltraTipologia(Consumer<TipologiaEscursione> handler)   { this.onFiltraTipologia  = handler; }
    public void setOnRicerca(Consumer<String> handler)                         { this.onRicerca          = handler; }
    public void setOnFiltraReset(Runnable handler)                             { this.onFiltraReset      = handler; }

    public ScrollPane getRoot() { return root; }

    // ── Costruzione card ──────────────────────────────────────────────────────

    private VBox buildCard(EscursionePreview exc) {
        VBox card = new VBox(10);
        card.setPrefWidth(240);
        card.setPadding(new Insets(0, 0, 14, 0));
        card.setStyle(styleCard(false));
        card.setCursor(javafx.scene.Cursor.HAND);

        // Immagine placeholder con gradiente
        StackPane imgBox = new StackPane();
        imgBox.setPrefHeight(130);
        javafx.scene.shape.Rectangle bg = new javafx.scene.shape.Rectangle(240, 130);
        bg.setFill(new javafx.scene.paint.LinearGradient(0, 0, 0, 1, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#2D4A35")),
                new javafx.scene.paint.Stop(1, Color.web("#0E200E"))));
        Label icon = new Label("⛰");
        icon.setFont(Font.font(40));
        icon.setOpacity(0.5);
        imgBox.getChildren().addAll(bg, icon);

        // Badge difficoltà
        Label badge = pill(exc.difficolta);

        // Titolo
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        titoloLbl.setTextFill(Color.WHITE);
        titoloLbl.setWrapText(true);
        titoloLbl.setPadding(new Insets(0, 12, 0, 12));

        // Prezzo + pulsante
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(0, 12, 0, 12));
        Label prezzoLbl = new Label(String.format("€ %.2f", exc.costo));
        prezzoLbl.setFont(Font.font("System", FontWeight.BOLD, 15));
        prezzoLbl.setTextFill(Color.web(ACCENT));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Button detailBtn = new Button("Dettagli");
        detailBtn.setFont(Font.font("System", 12));
        styleAccentBtn(detailBtn);
        detailBtn.setOnAction(e -> onEscursioneClick.accept(exc));
        bottom.getChildren().addAll(prezzoLbl, sp, detailBtn);

        VBox body = new VBox(8, badge, titoloLbl, bottom);
        body.setPadding(new Insets(10, 0, 0, 0));

        card.getChildren().addAll(imgBox, body);

        card.setOnMouseEntered(e -> card.setStyle(styleCard(true)));
        card.setOnMouseExited(e -> card.setStyle(styleCard(false)));
        card.setOnMouseClicked(e -> onEscursioneClick.accept(exc));

        return card;
    }

    private Button filterBtn(String label, TipologiaEscursione tipologia) {
        Button btn = new Button(label);
        btn.setFont(Font.font("System", 12));
        btn.setStyle(styleFilterBtn(false));
        btn.setOnAction(e -> {
            // Reset stile tutti i filtri
            filtriBar.getChildren().forEach(n -> {
                if (n instanceof Button) ((Button) n).setStyle(styleFilterBtn(false));
            });
            btn.setStyle(styleFilterBtn(true));
            tipologiaAttiva = tipologia;
            if (tipologia == null) onFiltraReset.run();
            else onFiltraTipologia.accept(tipologia);
        });
        btn.setOnMouseEntered(ev -> {
            if (tipologiaAttiva != tipologia) btn.setStyle(styleFilterBtn(true));
        });
        btn.setOnMouseExited(ev -> {
            if (tipologiaAttiva != tipologia) btn.setStyle(styleFilterBtn(false));
        });
        return btn;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Label pill(String testo) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", FontWeight.BOLD, 10));
        l.setTextFill(Color.web(ACCENT));
        l.setStyle("-fx-background-color: rgba(212,103,58,0.18); -fx-background-radius: 4;"
                + "-fx-padding: 3 8;");
        l.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(l, new Insets(0, 12, 0, 12));
        return l;
    }

    private String styleCard(boolean hover) {
        return "-fx-background-color: " + (hover ? "#1E4030" : CARD_BG) + ";"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: " + (hover ? ACCENT : "#1E4030") + ";"
                + "-fx-border-radius: 12; -fx-border-width: 0.5;";
    }

    private String styleFilterBtn(boolean active) {
        if (active)
            return "-fx-background-color: rgba(212,103,58,0.18); -fx-text-fill: " + ACCENT + ";"
                    + "-fx-border-color: " + ACCENT + "; -fx-border-radius: 20;"
                    + "-fx-background-radius: 20; -fx-border-width: 1; -fx-padding: 5 14; -fx-cursor: hand;";
        return "-fx-background-color: rgba(255,255,255,0.06); -fx-text-fill: rgba(255,255,255,0.6);"
                + "-fx-border-color: rgba(255,255,255,0.15); -fx-border-radius: 20;"
                + "-fx-background-radius: 20; -fx-border-width: 0.5; -fx-padding: 5 14; -fx-cursor: hand;";
    }

    private void styleAccentBtn(Button btn) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 6 14;";
        String hover = "-fx-background-color: " + ACCENT_HOVER + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 6 14;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }
}
