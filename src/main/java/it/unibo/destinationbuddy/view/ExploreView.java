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

public class ExploreView {

    private static final String APP_BG     = "#F4EFE6";
    private static final String ACCENT     = "#B85D38";
    private static final String TEXT_DARK  = "#2C2A26";
    private static final String TEXT_MUTED = "#807B73";

    private Consumer<EscursionePreview> onEscursioneClick = e -> {};
    private Consumer<TipologiaEscursione> onFiltraTipologia = t -> {};
    private Consumer<String> onRicerca = q -> {};
    private Runnable onFiltraReset = () -> {};

    private final ScrollPane root;
    private final FlowPane cardsPane;
    private final FlowPane filtriBar;

    public ExploreView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + APP_BG + ";");

        Label titolo = new Label("Esplora escursioni");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titolo.setTextFill(Color.web(TEXT_DARK));

        TextField searchField = new TextField();
        searchField.setPromptText("Cerca per titolo...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(260);
        searchField.textProperty().addListener((obs, old, val) -> onRicerca.accept(val));

        filtriBar = new FlowPane(8, 8);
        filtriBar.setAlignment(Pos.CENTER_LEFT);

        Button btnTutte = filterBtn("Tutte", null);
        btnTutte.getStyleClass().add("filter-btn-active");
        filtriBar.getChildren().add(btnTutte);

        VBox topBar = new VBox(10, searchField, filtriBar);

        cardsPane = new FlowPane();
        cardsPane.setHgap(14);
        cardsPane.setVgap(14);
        cardsPane.setPrefWrapLength(900);

        page.getChildren().addAll(titolo, topBar, cardsPane);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + APP_BG + "; -fx-background-color: " + APP_BG
                + "; -fx-border-color: transparent;");
    }

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

    public void setTipologie(List<TipologiaEscursione> tipologie) {
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

    private VBox buildCard(EscursionePreview exc) {
        VBox card = new VBox(10);
        card.setPrefWidth(240);
        card.setPadding(new Insets(0, 0, 14, 0));
        card.getStyleClass().add("exc-card");
        card.setCursor(javafx.scene.Cursor.HAND);

        StackPane imgBox = new StackPane();
        imgBox.setPrefHeight(130);
        imgBox.getStyleClass().add("card-image-box"); 
        
        Label icon = new Label("🗺️");
        icon.setStyle("-fx-font-size: 50px;"); 
        icon.getStyleClass().add("card-image-icon"); 
        
        imgBox.getChildren().add(icon);

        Label badge = pill(exc.difficolta);

        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.getStyleClass().add("exc-card-title");
        titoloLbl.setWrapText(true);

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
        detailBtn.getStyleClass().add("btn-accent");
        detailBtn.setOnAction(e -> onEscursioneClick.accept(exc));
        bottom.getChildren().addAll(prezzoLbl, sp, detailBtn);
        VBox body = new VBox(8, badge, titoloLbl, bottom);
        body.setPadding(new Insets(10, 0, 0, 0));
        card.getChildren().addAll(imgBox, body);
        card.setOnMouseClicked(e -> onEscursioneClick.accept(exc));
        return card;
    }

    private Button filterBtn(String label, TipologiaEscursione tipologia) {
        Button btn = new Button(label);
        btn.setFont(Font.font("System", 12));
        btn.getStyleClass().add("filter-btn");
        btn.setMinWidth(Region.USE_PREF_SIZE);
        btn.setMaxWidth(Region.USE_PREF_SIZE);
        
        btn.setOnAction(e -> {
            filtriBar.getChildren().forEach(n -> {
                if (n instanceof Button) {
                    n.getStyleClass().remove("filter-btn-active");
                }
            });

            if (!btn.getStyleClass().contains("filter-btn-active")) {
                btn.getStyleClass().add("filter-btn-active");
            }
            if (tipologia == null) onFiltraReset.run();
            else onFiltraTipologia.accept(tipologia);
        });
        
        return btn;
    }

    private Label pill(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().addAll("badge", "badge-accent");
        l.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(l, new Insets(0, 12, 0, 12));
        return l;
    }
}