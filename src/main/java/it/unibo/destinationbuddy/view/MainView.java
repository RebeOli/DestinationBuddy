package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * MainView — contenitore principale con TopBar e Sidebar.
 * Gestisce la navigazione tra le pagine sostituendo il centro del BorderPane.
 *
 * UTILIZZO DAL CONTROLLER (AppController):
 *   MainView main = new MainView();
 *   main.setOnCatalog(()  -> controller.mostraHome());
 *   main.setOnExplore(()  -> controller.mostraExplore());
 *   main.setOnProfilo(()  -> controller.mostraProfilo());
 *   main.setOnAdmin(()    -> controller.mostraAdmin());
 *   main.setOnLogout(()   -> controller.logout());
 *   main.setUtente(persona);
 *   main.setContenuto(homeView.getRoot());
 *   primaryStage.setScene(new Scene(main.getRoot(), 1200, 700));
 */
public class MainView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String TOPBAR_BG    = "#0B2316";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";
    private static final String SIDEBAR_ACT  = "#C95E2E";

    private Runnable onCatalog  = () -> {};
    private Runnable onExplore  = () -> {};
    private Runnable onProfilo  = () -> {};
    private Runnable onAdmin    = () -> {};
    private Runnable onLogout   = () -> {};
    private Runnable onPrenotaNuova = () -> {};

    private final BorderPane root;
    private final BorderPane body;

    // Elementi aggiornabili
    private final Label inizialeAvatar = new Label("?");
    private final Label nomeLabel      = new Label("Utente");
    private final Label ruoloLabel     = new Label("");
    private final Button adminBtn;
    private final Button creaEscBtn;

    private String activeNav = "catalog";

    public MainView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + DARK_BG + ";");
        root.setTop(buildTopBar());

        body = new BorderPane();
        body.setLeft(buildSidebar());
        root.setCenter(body);

        // Pulsanti speciali costruiti con riferimento per poterli mostrare/nascondere
        adminBtn   = findBtn("btn-admin");
        creaEscBtn = findBtn("btn-crea-esc");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    /** Imposta il nodo centrale (la pagina attiva). */
    public void setContenuto(javafx.scene.Node node) {
        body.setCenter(node);
    }

    /** Aggiorna le info nella sidebar quando l'utente si autentica. */
    public void setUtente(Persona p) {
        String iniziale = p.nome.isEmpty() ? "?" : String.valueOf(p.nome.charAt(0)).toUpperCase();
        inizialeAvatar.setText(iniziale);
        nomeLabel.setText(p.nome + " " + p.cognome);
        ruoloLabel.setText(p.tipoAmministratore ? "Amministratore"
                : (!p.tipoUtente ? "Guida certificata" : "Utente base"));

        // Mostra pulsante admin solo agli amministratori
        if (adminBtn != null) {
            adminBtn.setVisible(p.tipoAmministratore);
            adminBtn.setManaged(p.tipoAmministratore);
        }
        // Mostra "Nuova escursione" solo alle guide
        boolean isGuida = !p.tipoUtente && !p.tipoAmministratore;
        if (creaEscBtn != null) {
            creaEscBtn.setVisible(isGuida);
            creaEscBtn.setManaged(isGuida);
        }
    }

    /** Evidenzia la voce di navigazione attiva. */
    public void setNavAttiva(String nav) {
        this.activeNav = nav;
        // Il rebuild della sidebar non è necessario: usiamo ID sui pulsanti
    }

    public void setOnCatalog(Runnable h)      { this.onCatalog      = h; }
    public void setOnExplore(Runnable h)      { this.onExplore      = h; }
    public void setOnProfilo(Runnable h)      { this.onProfilo      = h; }
    public void setOnAdmin(Runnable h)        { this.onAdmin        = h; }
    public void setOnLogout(Runnable h)       { this.onLogout       = h; }
    public void setOnPrenotaNuova(Runnable h) { this.onPrenotaNuova = h; }

    public BorderPane getRoot() { return root; }

    // ── TopBar ────────────────────────────────────────────────────────────────

    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 20, 0, 20));
        bar.setPrefHeight(56);
        bar.setStyle("-fx-background-color: " + TOPBAR_BG
                + "; -fx-border-color: #1E4030; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("Destination Buddy");
        logo.setFont(Font.font("System", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(ACCENT));
        logo.setPadding(new Insets(0, 32, 0, 0));

        HBox navLinks = new HBox(0,
                navLink("Catalog",  "catalog"),
                navLink("Explore",  "explore"),
                navLink("Profilo",  "profilo"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button signOutBtn = new Button("Accedi");
        signOutBtn.setId("btn-auth");
        signOutBtn.setOnAction(e -> onLogout.run());

        bar.getChildren().addAll(logo, navLinks, spacer, signOutBtn);
        return bar;
    }

    private Label navLink(String text, String id) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("System", 13));
        lbl.setPadding(new Insets(0, 14, 0, 14));
        lbl.setPrefHeight(56);
        lbl.setAlignment(Pos.CENTER);
        lbl.setCursor(javafx.scene.Cursor.HAND);
        boolean active = activeNav.equals(id);
        lbl.setTextFill(active ? Color.WHITE : Color.web(TEXT_MUTED));
        lbl.setStyle(active
                ? "-fx-border-color: transparent transparent " + ACCENT + " transparent; -fx-border-width: 0 0 2 0;"
                : "");
        lbl.setOnMouseClicked(e -> {
            switch (id) {
                case "catalog" -> onCatalog.run();
                case "explore" -> onExplore.run();
                case "profilo" -> onProfilo.run();
            }
        });
        lbl.setOnMouseEntered(ev -> { if (!activeNav.equals(id)) lbl.setTextFill(Color.WHITE); });
        lbl.setOnMouseExited(ev  -> { if (!activeNav.equals(id)) lbl.setTextFill(Color.web(TEXT_MUTED)); });
        return lbl;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(190);
        sidebar.setMinWidth(190);
        sidebar.setStyle("-fx-background-color: " + DARK_BG
                + "; -fx-border-color: #1E4030; -fx-border-width: 0 1 0 0;");

        // Profilo
        VBox profileBlock = new VBox(4);
        profileBlock.setPadding(new Insets(20, 14, 20, 14));
        profileBlock.setStyle("-fx-border-color: #1E4030; -fx-border-width: 0 0 1 0;");

        Circle circle = new Circle(26, Color.web(ACCENT));
        inizialeAvatar.setFont(Font.font("System", FontWeight.BOLD, 18));
        inizialeAvatar.setTextFill(Color.WHITE);
        StackPane avatar = new StackPane(circle, inizialeAvatar);
        avatar.setMaxSize(52, 52);
        avatar.setMinSize(52, 52);
        VBox.setMargin(avatar, new Insets(0, 0, 10, 0));

        nomeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nomeLabel.setTextFill(Color.WHITE);
        ruoloLabel.setFont(Font.font("System", 10));
        ruoloLabel.setTextFill(Color.web(TEXT_MUTED));

        profileBlock.getChildren().addAll(avatar, nomeLabel, ruoloLabel);

        // Menu
        VBox menu = new VBox(2);
        menu.setPadding(new Insets(12, 8, 12, 8));
        menu.getChildren().addAll(
                sidebarItem("📋", "Catalog",         () -> onCatalog.run()),
                sidebarItem("🗓", "Prenotazioni",    () -> onProfilo.run()),
                sidebarItem("📊", "Dashboard",       () -> onProfilo.run()),
                sidebarItem("🏅", "Certificazioni",  () -> onProfilo.run())
        );

        // Pulsante prenota (sempre visibile)
        VBox bookBlock = new VBox();
        bookBlock.setPadding(new Insets(10, 12, 10, 12));
        Button bookBtn = new Button("Prenota nuova escursione");
        bookBtn.setMaxWidth(Double.MAX_VALUE);
        bookBtn.setFont(Font.font("System", FontWeight.BOLD, 12));
        bookBtn.setWrapText(true);
        styleAccentBtn(bookBtn);
        bookBtn.setOnAction(e -> onPrenotaNuova.run());
        bookBlock.getChildren().add(bookBtn);

        // Pulsante crea escursione (solo guide) — nascosto di default
        Button creaEscursBtn = new Button("+ Crea escursione");
        creaEscursBtn.setId("btn-crea-esc");
        creaEscursBtn.setMaxWidth(Double.MAX_VALUE);
        creaEscursBtn.setFont(Font.font("System", 12));
        creaEscursBtn.setStyle("-fx-background-color: rgba(212,103,58,0.15);"
                + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT + ";"
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");
        VBox creaBlock = new VBox(creaEscursBtn);
        creaBlock.setPadding(new Insets(0, 12, 8, 12));
        creaBlock.setVisible(false);
        creaBlock.setManaged(false);
        creaBlock.setId("btn-crea-esc");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Bottom
        VBox bottom = new VBox(0);
        bottom.setPadding(new Insets(12, 8, 16, 8));
        bottom.setStyle("-fx-border-color: #1E4030; -fx-border-width: 1 0 0 0;");

        // Pulsante admin (nascosto di default)
        Button adminButton = new Button("⚙ Admin");
        adminButton.setId("btn-admin");
        adminButton.setMaxWidth(Double.MAX_VALUE);
        adminButton.setFont(Font.font("System", 12));
        adminButton.setStyle("-fx-background-color: rgba(167,139,250,0.12);"
                + "-fx-text-fill: #C4B5FD; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");
        adminButton.setVisible(false);
        adminButton.setManaged(false);
        adminButton.setOnAction(e -> onAdmin.run());
        VBox adminBlock = new VBox(adminButton);
        adminBlock.setPadding(new Insets(0, 0, 8, 0));

        bottom.getChildren().addAll(adminBlock,
                sidebarItem("❓", "Aiuto",   () -> {}),
                sidebarItem("↪", "Esci",    () -> onLogout.run()));

        sidebar.getChildren().addAll(profileBlock, menu, bookBlock, creaBlock, spacer, bottom);
        return sidebar;
    }

    private HBox sidebarItem(String icon, String text, Runnable action) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 12, 10, 12));
        item.setCursor(javafx.scene.Cursor.HAND);
        item.setStyle("-fx-background-radius: 8;");
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #1E4030; -fx-background-radius: 8;"));
        item.setOnMouseExited(e  -> item.setStyle("-fx-background-radius: 8;"));
        item.setOnMouseClicked(e -> action.run());

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        Label textLbl = new Label(text);
        textLbl.setFont(Font.font("System", 13));
        textLbl.setTextFill(Color.web(TEXT_MUTED));
        item.getChildren().addAll(iconLbl, textLbl);
        return item;
    }

    private Button findBtn(String id) {
        // Restituiamo null qui — i pulsanti vengono trovati al momento del set
        return null;
    }

    private void styleAccentBtn(Button btn) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 10 14;";
        String hover = base.replace(ACCENT, ACCENT_HOVER);
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
    }

    public void setAutenticato(boolean autenticato) {
        // Trova il pulsante e cambia testo
        root.getTop().lookupAll(".button").stream()
            .filter(n -> "btn-auth".equals(n.getId()))
            .findFirst()
            .ifPresent(n -> ((Button) n).setText(autenticato ? "Esci" : "Accedi"));
    }
}
