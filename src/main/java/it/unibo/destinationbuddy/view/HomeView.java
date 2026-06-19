package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * HomeView — schermata principale di Destination Buddy.
 *
 * UTILIZZO DAL CONTROLLER:
 *   HomeView view = new HomeView();
 *   view.setUtente(persona);
 *   view.setTop5(escursioniList);
 *   view.setOnEscursioneClick(exc -> { ... });
 *   view.setOnMeseClick(mese -> { ... });
 *   view.setOnUpgradeClick(() -> { ... });
 *   view.setOnExploreClick(() -> { ... });
 *   root.setCenter(view.getRoot());
 *
 *   // Quando l'utente clicca un mese, il controller carica le escursioni
 *   // e chiama:
 *   view.setEscursioniMese(listaFiltrata);
 */
public class HomeView {

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String BANNER_BG    = "#D4EDE0";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";
    private static final String TEXT_DARK    = "#1A1A1A";

    // ── State ─────────────────────────────────────────────────────────────────
    private Persona utenteCorrente;
    private int meseSelezionato = -1;

    // ── Callbacks verso il Controller ─────────────────────────────────────────
    private Consumer<EscursionePreview> onEscursioneClick = e -> {};
    private IntConsumer                 onMeseClick       = m -> {};
    private Runnable                    onUpgradeClick    = () -> {};
    private Runnable                    onExploreClick    = () -> {};

    // ── UI roots ─────────────────────────────────────────────────────────────
    private final ScrollPane root;
    private final VBox        top5Container;
    private final GridPane    mesiGrid;
    private final VBox        mesiRisultati;
    private final HBox        welcomeBanner;

    // ─────────────────────────────────────────────────────────────────────────
    public HomeView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");

        // Banner benvenuto
        welcomeBanner = buildWelcomeBanner();

        // Sezione Top 5
        VBox top5Section = new VBox(12);
        HBox top5Header = sectionHeader("Top 5 Escursioni", "Vedi tutte →", onExploreClick);
        top5Container = new VBox(10);
        top5Section.getChildren().addAll(top5Header, top5Container);

        // Sezione mesi
        VBox mesiSection = new VBox(12);
        Label mesiTitle = new Label("Le migliori per mese");
        mesiTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        mesiTitle.setTextFill(Color.WHITE);

        // ── MODIFICA QUI ──────────────────────────────────────────────
        mesiGrid = new GridPane();
        mesiGrid.setHgap(12);
        mesiGrid.setVgap(12);

        // 6 colonne responsive che occupano tutta la larghezza
        for (int col = 0; col < 6; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 6);
            cc.setFillWidth(true);
            mesiGrid.getColumnConstraints().add(cc);
        }
        // ─────────────────────────────────────────────────────────────

        buildMesiGrid();

        mesiRisultati = new VBox(8);
        mesiSection.getChildren().addAll(mesiTitle, mesiGrid, mesiRisultati);

        page.getChildren().addAll(welcomeBanner, top5Section, mesiSection);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── Metodi pubblici chiamati dal Controller ───────────────────────────────

    /** Mostra nome e tipo utente nel banner. */
    public void setUtente(Persona p) {
        this.utenteCorrente = p;
        refreshBanner();
    }

    /** Popola la lista Top 5. */
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

    /** Mostra le escursioni filtrate per mese (chiamato dopo onMeseClick). */
    public void setEscursioniMese(List<EscursionePreview> lista) {
        mesiRisultati.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            mesiRisultati.getChildren().add(placeholderLabel("Nessuna escursione in questo mese."));
            return;
        }
        String nomeMese = meseSelezionato > 0
                ? Month.of(meseSelezionato).getDisplayName(TextStyle.FULL, Locale.ITALIAN)
                : "";
        Label header = new Label(lista.size() + " escursion" + (lista.size() == 1 ? "e" : "i") + " in " + nomeMese);
        header.setFont(Font.font("System", 13));
        header.setTextFill(Color.web(TEXT_MUTED));
        mesiRisultati.getChildren().add(header);
        for (EscursionePreview e : lista) {
            mesiRisultati.getChildren().add(buildMiniCard(e));
        }
    }

    /** Callback: clic su una card escursione → il controller apre il dettaglio. */
    public void setOnEscursioneClick(Consumer<EscursionePreview> handler) {
        this.onEscursioneClick = handler;
    }

    /** Callback: clic su un mese → il controller carica le escursioni del mese. */
    public void setOnMeseClick(IntConsumer handler) {
        this.onMeseClick = handler;
    }

    /** Callback: clic su "Upgrade" nel banner. */
    public void setOnUpgradeClick(Runnable handler) {
        this.onUpgradeClick = handler;
    }

    /** Callback: clic su "Vedi tutte →". */
    public void setOnExploreClick(Runnable handler) {
        this.onExploreClick = handler;
    }

    /** Nodo radice da inserire nel BorderPane principale. */
    public ScrollPane getRoot() {
        return root;
    }

    // ── Costruzione UI ────────────────────────────────────────────────────────

    private HBox buildWelcomeBanner() {
        HBox banner = new HBox(14);
        banner.setPadding(new Insets(16, 20, 16, 20));
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setStyle("-fx-background-color: " + BANNER_BG + "; -fx-background-radius: 12;");

        // Avatar
        StackPane avatar = buildAvatar("?");
        banner.getChildren().add(avatar);

        // Testo benvenuto
        VBox textBox = new VBox(3);
        Label benvenuto = new Label("Benvenuto!");
        benvenuto.setFont(Font.font("System", FontWeight.BOLD, 15));
        benvenuto.setTextFill(Color.web(TEXT_DARK));
        benvenuto.setId("lbl-benvenuto");

        Label status = new Label("Account base");
        status.setFont(Font.font("System", 12));
        status.setTextFill(Color.web("#555"));
        status.setId("lbl-status");

        textBox.getChildren().addAll(benvenuto, status);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Upgrade block
        VBox upgradeBox = new VBox(6);
        upgradeBox.setAlignment(Pos.CENTER_RIGHT);
        Label upgradeMsg = new Label("Passa a Premium: sconto del 20%\nsul noleggio e prenotazione prioritaria.");
        upgradeMsg.setFont(Font.font("System", 11));
        upgradeMsg.setTextFill(Color.web("#444"));
        upgradeMsg.setTextAlignment(TextAlignment.RIGHT);

        Button upgradeBtn = new Button("Upgrade ora");
        upgradeBtn.setFont(Font.font("System", FontWeight.BOLD, 12));
        styleAccentBtn(upgradeBtn);
        upgradeBtn.setOnAction(e -> onUpgradeClick.run());

        upgradeBox.getChildren().addAll(upgradeMsg, upgradeBtn);

        banner.getChildren().addAll(textBox, spacer, upgradeBox);
        return banner;
    }

    private void refreshBanner() {
        if (utenteCorrente == null) return;
        welcomeBanner.getChildren().stream()
                .filter(n -> n instanceof StackPane)
                .findFirst()
                .ifPresent(n -> {
                    StackPane sp = (StackPane) n;
                    sp.getChildren().stream()
                            .filter(c -> c instanceof Label)
                            .findFirst()
                            .ifPresent(c -> ((Label) c).setText(
                                    utenteCorrente.nome.isEmpty() ? "?" :
                                    String.valueOf(utenteCorrente.nome.charAt(0)).toUpperCase()));
                });
        // aggiorna label benvenuto
        welcomeBanner.getChildren().stream()
                .filter(n -> n instanceof VBox)
                .findFirst()
                .ifPresent(n -> {
                    VBox vb = (VBox) n;
                    vb.getChildren().forEach(c -> {
                        if (c instanceof Label) {
                            Label l = (Label) c;
                            if ("lbl-benvenuto".equals(l.getId())) {
                                l.setText("Bentornato, " + utenteCorrente.nome + "!");
                            } else if ("lbl-status".equals(l.getId())) {
                                l.setText(utenteCorrente.tipoUtente ? "Account base" : "Account Premium");
                            }
                        }
                    });
                });
    }

    private void buildMesiGrid() {
        String[] nomi = {"Gen","Feb","Mar","Apr","Mag","Giu",
                        "Lug","Ago","Set","Ott","Nov","Dic"};
        for (int i = 0; i < 12; i++) {
            int mese = i + 1;
            VBox card = new VBox(6);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(16, 0, 16, 0));
            card.setMaxWidth(Double.MAX_VALUE);   // occupa tutta la colonna
            card.setCursor(javafx.scene.Cursor.HAND);
            card.setStyle(styleMonthCard(false));
            card.setId("mese-" + mese);

            GridPane.setFillWidth(card, true);    // si allarga con la colonna


            Label nameLabel = new Label(nomi[i].toUpperCase());
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            nameLabel.setTextFill(Color.web(ACCENT));

            card.getChildren().addAll(nameLabel);

            card.setOnMouseClicked(e -> {
                meseSelezionato = mese;
                refreshMesiGrid();
                onMeseClick.accept(mese);
            });
            card.setOnMouseEntered(e -> {
                if (meseSelezionato != mese)
                    card.setStyle(styleMonthCard(true));
            });
            card.setOnMouseExited(e -> {
                if (meseSelezionato != mese)
                    card.setStyle(styleMonthCard(false));
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
                    .ifPresent(n -> n.setStyle(styleMonthCard(mese == meseSelezionato)));
        }
    }

    private HBox buildTop5Row(EscursionePreview exc, String medal) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 10;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 10; -fx-border-width: 0.5;");
        row.setCursor(javafx.scene.Cursor.HAND);

        Label medalLbl = new Label(medal);
        medalLbl.setFont(Font.font(20));
        medalLbl.setMinWidth(32);

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        titoloLbl.setTextFill(Color.WHITE);

        Label subLbl = new Label("Difficoltà: " + exc.difficolta);
        subLbl.setFont(Font.font("System", 12));
        subLbl.setTextFill(Color.web(TEXT_MUTED));

        info.getChildren().addAll(titoloLbl, subLbl);

        Label prezzoLbl = new Label(String.format("€ %.2f", exc.costo));
        prezzoLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        prezzoLbl.setTextFill(Color.web(ACCENT));

        row.getChildren().addAll(medalLbl, info, prezzoLbl);

        // Hover
        row.setOnMouseEntered(e -> row.setStyle(
                "-fx-background-color: #1E4030; -fx-background-radius: 10;"
                + "-fx-border-color: " + ACCENT + "; -fx-border-radius: 10; -fx-border-width: 0.5;"));
        row.setOnMouseExited(e -> row.setStyle(
                "-fx-background-color: " + CARD_BG + "; -fx-background-radius: 10;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 10; -fx-border-width: 0.5;"));
        row.setOnMouseClicked(e -> onEscursioneClick.accept(exc));

        return row;
    }

    private HBox buildMiniCard(EscursionePreview exc) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 8;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 8; -fx-border-width: 0.5;");
        row.setCursor(javafx.scene.Cursor.HAND);

        Label icon = new Label("🏔");
        icon.setFont(Font.font(18));

        VBox info = new VBox(2);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        titoloLbl.setTextFill(Color.WHITE);
        Label sub = new Label(exc.difficolta);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(titoloLbl, sub);

        Label prezzo = new Label(String.format("€ %.2f", exc.costo));
        prezzo.setFont(Font.font("System", FontWeight.BOLD, 13));
        prezzo.setTextFill(Color.web(ACCENT));

        row.getChildren().addAll(icon, info, prezzo);

        row.setOnMouseEntered(e -> row.setStyle(
                "-fx-background-color: #1E4030; -fx-background-radius: 8;"
                + "-fx-border-color: " + ACCENT + "; -fx-border-radius: 8; -fx-border-width: 0.5;"));
        row.setOnMouseExited(e -> row.setStyle(
                "-fx-background-color: " + CARD_BG + "; -fx-background-radius: 8;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 8; -fx-border-width: 0.5;"));
        row.setOnMouseClicked(e -> onEscursioneClick.accept(exc));

        return row;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private HBox sectionHeader(String title, String linkText, Runnable linkAction) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLbl.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label link = new Label(linkText);
        link.setFont(Font.font("System", 12));
        link.setTextFill(Color.web(ACCENT));
        link.setCursor(javafx.scene.Cursor.HAND);
        link.setOnMouseClicked(e -> onExploreClick.run());

        header.getChildren().addAll(titleLbl, spacer, link);
        return header;
    }

    private StackPane buildAvatar(String iniziale) {
        Circle circle = new Circle(22, Color.web(ACCENT));
        Label lbl = new Label(iniziale);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbl.setTextFill(Color.WHITE);
        StackPane sp = new StackPane(circle, lbl);
        sp.setMaxSize(44, 44);
        sp.setMinSize(44, 44);
        return sp;
    }

    private Label placeholderLabel(String testo) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        l.setPadding(new Insets(8, 0, 8, 0));
        return l;
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

    private String styleMonthCard(boolean active) {
        if (active) {
            return "-fx-background-color: rgba(212,103,58,0.12); -fx-background-radius: 10;"
                    + "-fx-border-color: " + ACCENT + "; -fx-border-radius: 10; -fx-border-width: 1;";
        }
        return "-fx-background-color: " + CARD_BG + "; -fx-background-radius: 10;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 10; -fx-border-width: 0.5;";
    }
}
