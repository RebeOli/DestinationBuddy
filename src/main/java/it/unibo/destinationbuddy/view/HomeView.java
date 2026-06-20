package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class HomeView {

    private Persona utenteCorrente;
    private int meseSelezionato = -1;
    private boolean haAbbonamentoAttivo = false;

    private Consumer<EscursionePreview> onEscursioneClick = e -> {};
    private IntConsumer                 onMeseClick       = m -> {};
    private Runnable                    onExploreClick    = () -> {};
    private Runnable                    onUpgradeClick    = () -> {};

    private final ScrollPane root;
    private final VBox        top5Container;
    private final GridPane    mesiGrid;
    private final VBox        mesiRisultati;
    private final HBox        welcomeBanner;
    
    // Riferimenti diretti alle Label per poterle cambiare all'istante
    private final Label inizialeAvatar = new Label("?");
    private final Label benvenutoLabel = new Label("Benvenuto!");
    private final Label statusLabel    = new Label("Accedi o registrati per pianificare le tue escursioni.");
    private VBox upgradeBox;

    public HomeView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));

        welcomeBanner = buildWelcomeBanner();
        refreshBanner(); // Si assicura che all'avvio sia tutto in stato "Sloggato"

        // Top 5
        VBox top5Section = new VBox(12);
        HBox top5Header = sectionHeader("Top 5 Escursioni", "Vedi tutte →");
        top5Container = new VBox(10);
        top5Section.getChildren().addAll(top5Header, top5Container);

        // Mesi
        VBox mesiSection = new VBox(12);
        Label mesiTitle = new Label("Le migliori per mese");
        mesiTitle.getStyleClass().add("section-title");

        mesiGrid = new GridPane();
        mesiGrid.setHgap(10);
        mesiGrid.setVgap(10);

        for (int col = 0; col < 6; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 6);
            cc.setFillWidth(true);
            mesiGrid.getColumnConstraints().add(cc);
        }
        buildMesiGrid();

        mesiRisultati = new VBox(10);
        VBox.setVgrow(mesiRisultati, Priority.ALWAYS);

        mesiSection.getChildren().addAll(mesiTitle, mesiGrid, mesiRisultati);

        page.getChildren().addAll(welcomeBanner, top5Section, mesiSection);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    // ── API pubblica ──────────────────────────────────────────────

    public void setUtente(Persona p) {
        this.utenteCorrente = p;
        refreshBanner(); // Aggiorna i testi e i bottoni
    }

    public void setHaAbbonamentoAttivo(boolean attivo) {
        this.haAbbonamentoAttivo = attivo;
        refreshBanner(); // Aggiorna i testi e i bottoni
    }

    public void setTop5(List<EscursionePreview> lista) {
        top5Container.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            top5Container.getChildren().add(placeholderLabel("Nessuna escursione disponibile."));
            return;
        }
        String[] medals = {"🥇", "🥈", "🥉", "4°", "5°"};
        for (int i = 0; i < Math.min(lista.size(), 5); i++) {
            top5Container.getChildren().add(buildTop5Row(lista.get(i), medals[i]));
        }
    }

    public void setEscursioniMese(List<EscursionePreview> lista) {
        mesiRisultati.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            mesiRisultati.getChildren().add(placeholderLabel("Nessuna escursione in questo mese."));
            return;
        }
        String nomeMese = meseSelezionato > 0
                ? Month.of(meseSelezionato).getDisplayName(TextStyle.FULL, Locale.ITALIAN)
                : "";

        Label header = new Label(lista.size() + " escursion"
                + (lista.size() == 1 ? "e" : "i") + " in " + nomeMese);
        header.getStyleClass().add("text-muted");
        mesiRisultati.getChildren().add(header);

        for (EscursionePreview e : lista) {
            mesiRisultati.getChildren().add(buildMiniCard(e));
        }
    }

    public void setOnEscursioneClick(Consumer<EscursionePreview> h) { this.onEscursioneClick = h; }
    public void setOnMeseClick(IntConsumer h)                        { this.onMeseClick       = h; }
    public void setOnExploreClick(Runnable h)                        { this.onExploreClick    = h; }
    public void setOnUpgradeClick(Runnable h)                        { this.onUpgradeClick    = h; }
    public ScrollPane getRoot()                                       { return root; }

    // ── UI ────────────────────────────────────────────────────────

    private HBox buildWelcomeBanner() {
        HBox banner = new HBox(14);
        banner.setPadding(new Insets(16, 20, 16, 20));
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.getStyleClass().add("welcome-banner");

        // Avatar
        Circle circle = new Circle(22);
        circle.setStyle("-fx-fill: -db-accent;");
        inizialeAvatar.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        StackPane avatar = new StackPane(circle, inizialeAvatar);
        avatar.setMaxSize(44, 44);
        avatar.setMinSize(44, 44);

        banner.getChildren().add(avatar);

        // Testi
        VBox textBox = new VBox(3);
        benvenutoLabel.getStyleClass().add("auth-title");
        benvenutoLabel.setStyle("-fx-font-size: 15px;");
        statusLabel.getStyleClass().add("text-muted");

        textBox.getChildren().addAll(benvenutoLabel, statusLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Box Upgrade
        upgradeBox = new VBox(6);
        upgradeBox.setAlignment(Pos.CENTER_RIGHT);
        Label upgradeMsg = new Label("Passa a Premium: sconto del 20%\nsul noleggio e prenotazione prioritaria.");
        upgradeMsg.getStyleClass().add("text-muted");
        upgradeMsg.setTextAlignment(TextAlignment.RIGHT);

        Button upgradeBtn = new Button("Upgrade ora");
        upgradeBtn.getStyleClass().add("btn-accent");
        upgradeBtn.setOnAction(e -> onUpgradeClick.run());

        upgradeBox.getChildren().addAll(upgradeMsg, upgradeBtn);

        banner.getChildren().addAll(textBox, spacer, upgradeBox);
        return banner;
    }

    // IL METODO DI AGGIORNAMENTO CORRETTO
    private void refreshBanner() {
        if (utenteCorrente == null) {
            // REGOLE NON LOGGATO: Scritte standard e NASCONDE il box Upgrade
            inizialeAvatar.setText("?");
            benvenutoLabel.setText("Benvenuto!");
            statusLabel.setText("Accedi o registrati per pianificare le tue escursioni.");
            
            if (upgradeBox != null) {
                upgradeBox.setVisible(false);
                upgradeBox.setManaged(false);
            }
        } else {
            // REGOLE LOGGATO: Nome, Ruolo e MOSTRA l'Upgrade solo se necessario
            String iniziale = utenteCorrente.nome.isEmpty() ? "?" : String.valueOf(utenteCorrente.nome.charAt(0)).toUpperCase();
            inizialeAvatar.setText(iniziale);
            benvenutoLabel.setText("Bentornato, " + utenteCorrente.nome + "!");

            if (utenteCorrente.tipoAmministratore) {
                statusLabel.setText("Sessione Amministratore attiva.");
            } else if (haAbbonamentoAttivo) {
                statusLabel.setText("Account Premium attivo — Goditi i tuoi vantaggi!");
            } else if (!utenteCorrente.tipoUtente) { // Se è una guida
                statusLabel.setText("Guida certificata Destination Buddy.");
            } else {
                statusLabel.setText("Account base");
            }

            // A destra mostra l'upgrade SOLO se è un utente base e non è già premium
            if (upgradeBox != null) {
                boolean mostraUpgrade = !utenteCorrente.tipoAmministratore && !haAbbonamentoAttivo;
                upgradeBox.setVisible(mostraUpgrade);
                upgradeBox.setManaged(mostraUpgrade);
            }
        }
    }

    private void buildMesiGrid() {
        String[] nomi = {"Gen","Feb","Mar","Apr","Mag","Giu","Lug","Ago","Set","Ott","Nov","Dic"};
        for (int i = 0; i < 12; i++) {
            int mese = i + 1;
            VBox card = new VBox(6);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(16, 0, 16, 0));
            card.setMaxWidth(Double.MAX_VALUE);
            card.setCursor(javafx.scene.Cursor.HAND);
            card.getStyleClass().add("month-card");
            card.setId("mese-" + mese);
            GridPane.setFillWidth(card, true);

            Label nameLabel = new Label(nomi[i].toUpperCase());
            nameLabel.getStyleClass().add("month-num");

            card.getChildren().addAll(nameLabel);

            card.setOnMouseClicked(e -> {
                meseSelezionato = mese;
                refreshMesiGrid();
                onMeseClick.accept(mese);
            });

            mesiGrid.add(card, i % 6, i / 6);
        }
    }

    private void refreshMesiGrid() {
        for (int i = 1; i <= 12; i++) {
            int mese = i;
            mesiGrid.getChildren().stream()
                    .filter(n -> ("mese-" + mese).equals(n.getId()))
                    .findFirst()
                    .ifPresent(n -> n.getStyleClass().setAll(mese == meseSelezionato
                            ? "month-card-active" : "month-card"));
        }
    }

    private HBox buildTop5Row(EscursionePreview exc, String medal) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("top5-row");
        row.setCursor(javafx.scene.Cursor.HAND);

        Label medalLbl = new Label(medal);
        medalLbl.setStyle("-fx-font-size: 20px; -fx-min-width: 32px;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.getStyleClass().add("top5-title");
        Label subLbl = new Label("Difficoltà: " + exc.difficolta);
        subLbl.getStyleClass().add("top5-sub");
        info.getChildren().addAll(titoloLbl, subLbl);

        Label prezzoLbl = new Label(String.format("€ %.2f", exc.costo));
        prezzoLbl.getStyleClass().add("text-price");

        row.getChildren().addAll(medalLbl, info, prezzoLbl);
        row.setOnMouseClicked(e -> onEscursioneClick.accept(exc));
        return row;
    }

    private HBox buildMiniCard(EscursionePreview exc) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("top5-row");
        row.setCursor(javafx.scene.Cursor.HAND);

        Label icon = new Label("🏔");

        VBox info = new VBox(2);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.getStyleClass().addAll("top5-title");
        Label sub = new Label(exc.difficolta);
        sub.getStyleClass().add("text-muted");
        info.getChildren().addAll(titoloLbl, sub);

        Label prezzo = new Label(String.format("€ %.2f", exc.costo));
        prezzo.getStyleClass().add("text-price");

        row.getChildren().addAll(icon, info, prezzo);
        row.setOnMouseClicked(e -> onEscursioneClick.accept(exc));
        return row;
    }

    // ── Helpers ───────────────────────────────────────────────────

    private HBox sectionHeader(String title, String linkText) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("section-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label link = new Label(linkText);
        link.getStyleClass().add("switch-link");
        link.setCursor(javafx.scene.Cursor.HAND);
        link.setOnMouseClicked(e -> onExploreClick.run());
        header.getChildren().addAll(titleLbl, spacer, link);
        return header;
    }

    private StackPane buildAvatar(String iniziale) {
        Circle circle = new Circle(22);
        circle.setStyle("-fx-fill: -db-accent;");
        Label lbl = new Label(iniziale);
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        StackPane sp = new StackPane(circle, lbl);
        sp.setMaxSize(44, 44);
        sp.setMinSize(44, 44);
        return sp;
    }

    private Label placeholderLabel(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().add("text-muted");
        l.setPadding(new Insets(8, 0, 8, 0));
        return l;
    }
}