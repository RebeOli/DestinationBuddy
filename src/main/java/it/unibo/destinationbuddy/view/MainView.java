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
 */
public class MainView {

    // ── VARIABILI AGGIORNATE AL LIGHT THEME ─────────────────────────────
    private static final String APP_BG       = "#F4EFE6"; // Sabbia
    private static final String SIDEBAR_BG   = "#E8EFE8"; // Bianco per barre
    private static final String BORDER_COLOR = "#DCD5C6"; // Bordo chiaro
    private static final String ACCENT       = "#B85D38"; // Terracotta
    private static final String ACCENT_HOVER = "#4A7C59"; // Verde foresta
    private static final String TEXT_DARK    = "#2C2A26"; // Testo scuro
    private static final String TEXT_MUTED   = "#807B73"; // Testo grigino
    private static final String HOVER_BG     = "#EBF5F8"; // Azzurrino per hover

    private Runnable onHome  = () -> {};
    private Runnable onExplore  = () -> {};
    private Runnable onProfilo  = () -> {};
    private Runnable onAdmin    = () -> {};
    private Runnable onLogout   = () -> {};
    private Runnable onPrenotaNuova = () -> {};
    private Runnable onLogin    = () -> {};
    private Runnable onImpostazioni = () -> {};
    private Runnable onCreaEscursione = () -> {};
    private Runnable onAggiungiLuogo = () -> {};
    private Runnable onInserisciResoconto = () -> {};

    private final BorderPane root;
    private final BorderPane body;

    private final Label inizialeAvatar = new Label("?");
    private final Label nomeLabel      = new Label("Utente");
    private final Label ruoloLabel     = new Label("");
    
    private VBox menuStandard;
    private VBox prenotaBlock;
    private VBox creaBlock;
    private VBox resocontoBlock;
    private Button adminButtonSidebar;
    private VBox luogoBlock;

    private HBox topNavLinks; // Variabile globale per nascondere i menu in alto

    private String activeNav = "home";

    public MainView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + APP_BG + ";");
        root.setTop(buildTopBar());

        body = new BorderPane();
        body.setLeft(buildSidebar()); 
        root.setCenter(body);
    }

    public void setContenuto(javafx.scene.Node node) {
        body.setCenter(node);
    }

    public void setUtente(Persona p, boolean isGuida) {
        if (p == null) {
            inizialeAvatar.setText("?");
            nomeLabel.setText("Utente");
            ruoloLabel.setText("");
            
            if (menuStandard != null) {
                adminButtonSidebar.setVisible(false);
                adminButtonSidebar.setManaged(false);
                
                menuStandard.setVisible(true);
                menuStandard.setManaged(true);
                prenotaBlock.setVisible(true);
                prenotaBlock.setManaged(true);
                
                creaBlock.setVisible(false);
                creaBlock.setManaged(false);
                resocontoBlock.setVisible(false);
                resocontoBlock.setManaged(false);
                
                if (luogoBlock != null) {
                    luogoBlock.setVisible(false);
                    luogoBlock.setManaged(false);
                }
            } // ⚡ ECCO LA PARENTESI CHE MANCAVA! Chiude l'if(menuStandard != null)

            // Torna a mostrare i menu in alto quando nessuno è loggato
            if (topNavLinks != null) {
                topNavLinks.setVisible(true);
                topNavLinks.setManaged(true);
            }
            return; 
        }

        // SE UN UTENTE È LOGGATO
        String iniziale = p.nome.isEmpty() ? "?" : String.valueOf(p.nome.charAt(0)).toUpperCase();
        inizialeAvatar.setText(iniziale);
        nomeLabel.setText(p.nome + " " + p.cognome);
        
        // ── NUOVA LOGICA: Gestione dei testi dei ruoli basata sulle entità pulite ──
        if (p.tipoAmministratore) {
            ruoloLabel.setText("Amministratore");
        } else if (isGuida) {
            ruoloLabel.setText("Guida certificata");
        } else {
            ruoloLabel.setText("Utente base");
        }

        if (p.tipoAmministratore) {
            // REGOLE ADMIN: Nascondi la sidebar standard, mostra solo Admin
            if (menuStandard != null) {
                menuStandard.setVisible(false);
                menuStandard.setManaged(false);
                prenotaBlock.setVisible(false);
                prenotaBlock.setManaged(false);
                creaBlock.setVisible(false);
                creaBlock.setManaged(false);
                resocontoBlock.setVisible(false);
                resocontoBlock.setManaged(false);
                
                if (luogoBlock != null) {
                    luogoBlock.setVisible(false);
                    luogoBlock.setManaged(false);
                }
                
                adminButtonSidebar.setVisible(true);
                adminButtonSidebar.setManaged(true);
            }

            // NASCONDIAMO I BOTTONI IN ALTO (Home, Explore, Profilo)
            if (topNavLinks != null) {
                topNavLinks.setVisible(false);
                topNavLinks.setManaged(false);
            } 
        } else {
            // REGOLE UTENTE / GUIDA: Mostra sidebar standard
            if (menuStandard != null) {
                adminButtonSidebar.setVisible(false);
                adminButtonSidebar.setManaged(false);
                
                menuStandard.setVisible(true);
                menuStandard.setManaged(true);
                prenotaBlock.setVisible(true);
                prenotaBlock.setManaged(true);
                
                creaBlock.setVisible(isGuida);
                creaBlock.setManaged(isGuida);
                resocontoBlock.setVisible(isGuida);
                resocontoBlock.setManaged(isGuida);
                if (luogoBlock != null) {
                    luogoBlock.setVisible(isGuida);
                    luogoBlock.setManaged(isGuida);
                }
            }

            // UTENTE NORMALE / GUIDA: Mostriamo di nuovo i bottoni in alto
            if (topNavLinks != null) {
                topNavLinks.setVisible(true);
                topNavLinks.setManaged(true);
            }
        }
    } // ⚡ E qui ho tolto le parentesi di troppo!

    public void setNavAttiva(String nav) {
        this.activeNav = nav;
        if (root.getTop() != null) {
            String[] menuIds = {"home", "explore", "profilo"};
            for (String id : menuIds) {
                javafx.scene.Node node = root.getTop().lookup("#nav-" + id);
                if (node instanceof Label lbl) {
                    boolean active = activeNav.equals(id);
                    lbl.setTextFill(active ? Color.web(TEXT_DARK) : Color.web(TEXT_MUTED));
                    lbl.setStyle(active
                            ? "-fx-border-color: transparent transparent " + ACCENT + " transparent; -fx-border-width: 0 0 2 0;"
                            : "");
                }
            }
        }
    }

    public void setOnHome(Runnable h)          { this.onHome         = h; }
    public void setOnExplore(Runnable h)       { this.onExplore      = h; }
    public void setOnProfilo(Runnable h)       { this.onProfilo      = h; }
    public void setOnAdmin(Runnable h)         { this.onAdmin        = h; }
    public void setOnLogout(Runnable h)        { this.onLogout       = h; }
    public void setOnPrenotaNuova(Runnable h) { this.onPrenotaNuova = h; }
    public void setOnLogin(Runnable h)         { this.onLogin        = h; }
    public void setOnImpostazioni(Runnable h) { this.onImpostazioni = h; }
    public void setOnCreaEscursione(Runnable h) { this.onCreaEscursione = h; }
    public void setOnAggiungiLuogo(Runnable h) { this.onAggiungiLuogo = h; }
    public void setOnInserisciResoconto(Runnable h) { this.onInserisciResoconto = h; }

    public BorderPane getRoot() { return root; }

    // ── TopBar ────────────────────────────────────────────────────────────────

    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 20, 0, 20));
        bar.setPrefHeight(56);
        bar.setStyle("-fx-background-color: " + SIDEBAR_BG
                + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("Destination Buddy");
        logo.setFont(Font.font("System", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(ACCENT));
        logo.setPadding(new Insets(0, 32, 0, 0));

        topNavLinks = new HBox(0,
                navLink("Home",  "home"),
                navLink("Explore",  "explore"),
                navLink("Profilo",  "profilo"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button signOutBtn = new Button("Accedi");
        signOutBtn.setId("btn-auth");
        signOutBtn.setFont(Font.font("System", 13));
        signOutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK 
                + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;");
        signOutBtn.setOnAction(e -> onLogin.run());
        signOutBtn.setOnMouseEntered(e -> signOutBtn.setStyle("-fx-background-color: " + HOVER_BG 
                + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + ACCENT_HOVER + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;"));
        signOutBtn.setOnMouseExited(e -> signOutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_DARK 
                + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;"));

        bar.getChildren().addAll(logo, topNavLinks, spacer, signOutBtn);
        return bar;
    }

    private Label navLink(String text, String id) {
        Label lbl = new Label(text);
        lbl.setId("nav-" + id);
        lbl.setFont(Font.font("System", 13));
        lbl.setPadding(new Insets(0, 14, 0, 14));
        lbl.setPrefHeight(56);
        lbl.setAlignment(Pos.CENTER);
        lbl.setCursor(javafx.scene.Cursor.HAND);
        boolean active = activeNav.equals(id);
        lbl.setTextFill(active ? Color.web(TEXT_DARK) : Color.web(TEXT_MUTED));
        lbl.setStyle(active
                ? "-fx-border-color: transparent transparent " + ACCENT + " transparent; -fx-border-width: 0 0 2 0;"
                : "");
        lbl.setOnMouseClicked(e -> {
            switch (id) {
                case "home" -> onHome.run();
                case "explore" -> onExplore.run();
                case "profilo" -> onProfilo.run();
            }
        });
        lbl.setOnMouseEntered(ev -> { if (!activeNav.equals(id)) lbl.setTextFill(Color.web(ACCENT_HOVER)); });
        lbl.setOnMouseExited(ev  -> { if (!activeNav.equals(id)) lbl.setTextFill(Color.web(TEXT_MUTED)); });
        return lbl;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(190);
        sidebar.setMinWidth(190);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG
                + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 1 0 0;");

        VBox profileBlock = new VBox(4);
        profileBlock.setPadding(new Insets(20, 14, 20, 14));
        profileBlock.setStyle("-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1 0;");

        Circle circle = new Circle(26, Color.web(ACCENT));
        inizialeAvatar.setFont(Font.font("System", FontWeight.BOLD, 18));
        inizialeAvatar.setTextFill(Color.WHITE);
        StackPane avatar = new StackPane(circle, inizialeAvatar);
        avatar.setMaxSize(52, 52);
        avatar.setMinSize(52, 52);
        VBox.setMargin(avatar, new Insets(0, 0, 10, 0));

        nomeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nomeLabel.setTextFill(Color.web(TEXT_DARK));
        ruoloLabel.setFont(Font.font("System", 10));
        ruoloLabel.setTextFill(Color.web(TEXT_MUTED));

        profileBlock.getChildren().addAll(avatar, nomeLabel, ruoloLabel);

        menuStandard = new VBox(2);
        menuStandard.setPadding(new Insets(12, 8, 12, 8));
        menuStandard.getChildren().addAll(
                sidebarItem("🏠", "Home",         () -> onHome.run()),
                sidebarItem("🗓", "Prenotazioni",    () -> onProfilo.run()),
                sidebarItem("🏅", "Certificazioni",  () -> onProfilo.run())
        );

        prenotaBlock = new VBox();
        prenotaBlock.setPadding(new Insets(10, 12, 10, 12));
        Button bookBtn = new Button("Prenota nuova escursione");
        bookBtn.setMaxWidth(Double.MAX_VALUE);
        bookBtn.setFont(Font.font("System", FontWeight.BOLD, 12));
        bookBtn.setWrapText(true);
        styleAccentBtn(bookBtn);
        bookBtn.setOnAction(e -> onPrenotaNuova.run());
        prenotaBlock.getChildren().add(bookBtn);

        Button creaEscursBtn = new Button("+ Crea escursione");
        creaEscursBtn.setOnAction(e -> onCreaEscursione.run());
        creaEscursBtn.setId("btn-crea-esc");
        creaEscursBtn.setMaxWidth(Double.MAX_VALUE);
        creaEscursBtn.setFont(Font.font("System", 12));
        creaEscursBtn.setStyle("-fx-background-color: transparent;"
                + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT + ";"
                + "-fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");
        creaEscursBtn.setOnMouseEntered(e -> creaEscursBtn.setStyle("-fx-background-color: " + HOVER_BG 
                + "; -fx-text-fill: " + ACCENT_HOVER + "; -fx-border-color: " + ACCENT_HOVER + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));
        creaEscursBtn.setOnMouseExited(e -> creaEscursBtn.setStyle("-fx-background-color: transparent;"
                + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));

        creaBlock = new VBox(creaEscursBtn);
        creaBlock.setPadding(new Insets(0, 12, 8, 12));
        creaBlock.setVisible(false);
        creaBlock.setManaged(false);
        creaBlock.setId("btn-crea-esc");

        Button resocontoBtn = new Button("+ Inserisci resoconto");
        resocontoBtn.setOnAction(e -> onInserisciResoconto.run());
        resocontoBtn.setMaxWidth(Double.MAX_VALUE);
        resocontoBtn.setFont(Font.font("System", 12));
        resocontoBtn.setStyle("-fx-background-color: transparent;"
            + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT + ";"
            + "-fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");
        resocontoBtn.setOnMouseEntered(e -> resocontoBtn.setStyle("-fx-background-color: " + HOVER_BG
            + "; -fx-text-fill: " + ACCENT_HOVER + "; -fx-border-color: " + ACCENT_HOVER
            + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));
        resocontoBtn.setOnMouseExited(e -> resocontoBtn.setStyle("-fx-background-color: transparent;"
            + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT
            + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));

        resocontoBlock = new VBox(resocontoBtn);
        resocontoBlock.setPadding(new Insets(0, 12, 8, 12));
        resocontoBlock.setVisible(false);
        resocontoBlock.setManaged(false);

        // Button aggiungiLuogoBtn = new Button("+ Aggiungi Luogo");
        // aggiungiLuogoBtn.setOnAction(e -> onAggiungiLuogo.run()); // Assicurati di avere questo Runnable
        // aggiungiLuogoBtn.setId("btn-luogo");
        // aggiungiLuogoBtn.setMaxWidth(Double.MAX_VALUE);
        // aggiungiLuogoBtn.setFont(Font.font("System", 12));
        // aggiungiLuogoBtn.setStyle("-fx-background-color: transparent;"
        //         + "-fx-text-fill: " + ACCENT + "; -fx-border-color: " + ACCENT + ";"
        //         + "-fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");

        // luogoBlock = new VBox(aggiungiLuogoBtn);
        // luogoBlock.setPadding(new Insets(0, 12, 8, 12));
        // luogoBlock.setVisible(false);
        // luogoBlock.setManaged(false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottom = new VBox(0);
        bottom.setPadding(new Insets(12, 8, 16, 8));
        bottom.setStyle("-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1 0 0 0;");

        adminButtonSidebar = new Button("⚙ Admin");
        adminButtonSidebar.setId("btn-admin");
        adminButtonSidebar.setMaxWidth(Double.MAX_VALUE);
        adminButtonSidebar.setFont(Font.font("System", 12));
        adminButtonSidebar.setStyle("-fx-background-color: transparent;"
                + "-fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;");
        adminButtonSidebar.setOnMouseEntered(e -> adminButtonSidebar.setStyle("-fx-background-color: " + HOVER_BG 
                + "; -fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + ACCENT_HOVER + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));
        adminButtonSidebar.setOnMouseExited(e -> adminButtonSidebar.setStyle("-fx-background-color: transparent;"
                + "-fx-text-fill: " + TEXT_DARK + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 8 0;"));

        adminButtonSidebar.setVisible(false);
        adminButtonSidebar.setManaged(false);
        adminButtonSidebar.setOnAction(e -> onAdmin.run());
        
        VBox adminBlock = new VBox(adminButtonSidebar);
        adminBlock.setPadding(new Insets(0, 0, 8, 0));

        bottom.getChildren().addAll(adminBlock,
                sidebarItem("↪", "Esci",    () -> onLogout.run()));

        sidebar.getChildren().addAll(profileBlock, menuStandard, prenotaBlock, creaBlock, resocontoBlock, spacer, bottom);
        return sidebar;
    }

    private HBox sidebarItem(String icon, String text, Runnable action) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 12, 10, 12));
        item.setCursor(javafx.scene.Cursor.HAND);
        item.setStyle("-fx-background-radius: 8;");
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: " + HOVER_BG + "; -fx-background-radius: 8;"));
        item.setOnMouseExited(e  -> item.setStyle("-fx-background-radius: 8;"));
        item.setOnMouseClicked(e -> action.run());

        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(14));
        Label textLbl = new Label(text);
        textLbl.setFont(Font.font("System", 13));
        textLbl.setTextFill(Color.web(TEXT_DARK));
        item.getChildren().addAll(iconLbl, textLbl);
        return item;
    }

    private void styleAccentBtn(Button btn) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 10 14;";
        String hover = "-fx-background-color: " + ACCENT_HOVER + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 10 14;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
    }

    public void setAutenticato(boolean autenticato) {
        root.getTop().lookupAll(".button").stream()
            .filter(n -> "btn-auth".equals(n.getId()))
            .findFirst()
            .ifPresent(n -> {
                Button btn = (Button) n;
                btn.setText(autenticato ? "Esci" : "Accedi");
                btn.setOnAction(e -> {
                    if (autenticato) {
                        onLogout.run();
                    } else {
                        onLogin.run();
                    }
                });
            });
    }
}