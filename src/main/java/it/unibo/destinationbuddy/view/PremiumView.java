package it.unibo.destinationbuddy.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;

public class PremiumView {

    public enum Piano {
        MENSILE(1,  9.90,  "Mensile",   false),
        SEMESTRALE(6, 7.90, "6 mesi",   true),
        ANNUALE(12,  5.90,  "Annuale",  false);

        public final int     mesi;
        public final double  prezzoMensile;
        public final String  etichetta;
        public final boolean consigliato;

        Piano(int mesi, double prezzoMensile, String etichetta, boolean consigliato) {
            this.mesi          = mesi;
            this.prezzoMensile = prezzoMensile;
            this.etichetta     = etichetta;
            this.consigliato   = consigliato;
        }

        public double totale() {
            return mesi * prezzoMensile;
        }
    }

    private Consumer<Piano> onScegliPiano = p -> {};
    private Runnable        onIndietro    = () -> {};

    private final Label errorLabel = new Label();
    private final ScrollPane root;

    public PremiumView() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(20, 24, 24, 24));
        Label back = new Label("← Indietro");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 0, 10, 0));

        Label titolo = new Label("Passa a Premium");
        titolo.getStyleClass().add("auth-title");
        titolo.setStyle("-fx-font-size: 28px;");

        Label sub = new Label("Stessi vantaggi esclusivi su ogni piano: sconto sul noleggio\ne accesso prioritario alle prenotazioni.");
        sub.getStyleClass().add("text-muted");
        sub.setTextAlignment(TextAlignment.CENTER);

        header.getChildren().addAll(titolo, sub);

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        HBox vantaggi = new HBox(24);
        vantaggi.setAlignment(Pos.CENTER);
        vantaggi.getChildren().addAll(
                vantaggioPill("🏷", "Sconto noleggio equipaggiamento"),
                vantaggioPill("⚡", "Accesso prioritario alle prenotazioni")
        );

        HBox pianiRow = new HBox(20);
        pianiRow.setAlignment(Pos.CENTER);
        for (Piano p : Piano.values()) {
            pianiRow.getChildren().add(buildPianoCard(p));
        }

        page.getChildren().addAll(back, header, errorLabel, vantaggi, pianiRow);
        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    public void setOnScegliPiano(Consumer<Piano> handler) { this.onScegliPiano = handler; }
    public void setOnIndietro(Runnable handler)            { this.onIndietro    = handler; }
    public ScrollPane getRoot()                            { return root; }

    public void mostraErrore(String msg) {
        errorLabel.setText("⚠ " + msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private VBox buildPianoCard(Piano piano) {
        VBox card = new VBox(14);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(24, 20, 24, 20));
        card.setPrefWidth(220);
        card.setMaxWidth(220);

        card.getStyleClass().add("card");
        if (piano.consigliato) {
            card.setStyle("-fx-border-color: -db-accent; -fx-border-width: 2; -fx-border-radius: 12;");
        }

        if (piano.consigliato) {
            Label popBadge = new Label("PIÙ POPOLARE");
            popBadge.getStyleClass().addAll("badge", "badge-accent");
            popBadge.setStyle("-fx-background-radius: 20; -fx-padding: 4 12;");
            card.getChildren().add(popBadge);
        }

        Label etichettaLbl = new Label(piano.etichetta);
        etichettaLbl.getStyleClass().add("text-muted");

        HBox prezzoRow = new HBox(2);
        prezzoRow.setAlignment(Pos.BASELINE_CENTER);
        Label prezzoLbl = new Label(String.format("€%.2f", piano.prezzoMensile));
        prezzoLbl.getStyleClass().add("text-accent");
        prezzoLbl.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        Label perMeseLbl = new Label("/mese");
        perMeseLbl.getStyleClass().add("text-muted");
        prezzoRow.getChildren().addAll(prezzoLbl, perMeseLbl);

        Label totaleLbl = new Label(String.format("Totale: €%.2f per %d mes%s",
                piano.totale(), piano.mesi, piano.mesi == 1 ? "e" : "i"));
        totaleLbl.getStyleClass().add("text-muted");

        VBox risparmioBox = new VBox();
        if (piano.mesi > 1) {
            double risparmioPerc = (1 - piano.prezzoMensile / Piano.MENSILE.prezzoMensile) * 100;
            Label risparmioLbl = new Label(String.format("Risparmi il %.0f%%", risparmioPerc));
            risparmioLbl.getStyleClass().addAll("badge", "badge-green");
            risparmioBox.getChildren().add(risparmioLbl);
            risparmioBox.setAlignment(Pos.CENTER);
        }

        Button scegliBtn = new Button("Scegli " + piano.etichetta);
        scegliBtn.setMaxWidth(Double.MAX_VALUE);
        scegliBtn.getStyleClass().add(piano.consigliato ? "btn-accent" : "btn-outline-accent");
        scegliBtn.setOnAction(e -> onScegliPiano.accept(piano));

        card.getChildren().addAll(etichettaLbl, prezzoRow, totaleLbl, risparmioBox, scegliBtn);
        return card;
    }

    private HBox vantaggioPill(String icon, String testo) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(8, 14, 8, 14));
        box.getStyleClass().add("card");
        box.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");

        Label iconLbl = new Label(icon);

        Label testoLbl = new Label(testo);
        testoLbl.getStyleClass().add("sidebar-item-text");

        box.getChildren().addAll(iconLbl, testoLbl);
        return box;
    }
}
