package it.unibo.destinationbuddy.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * AuthView — Login e Registrazione in un'unica view con toggle.
 *
 * UTILIZZO DAL CONTROLLER:
 *   AuthView view = new AuthView();
 *   view.setOnLogin((email, password) -> controller.login(email, password));
 *   view.setOnRegistra((nome, cognome, cf, email, password) -> controller.registra(...));
 *   view.mostraErrore("Email o password errati.");
 *   root.setCenter(view.getRoot());
 */
public class AuthView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

    // Callbacks
    private BiConsumer<String, String>                        onLogin     = (e, p) -> {};
    private Consumer<String[]>                                onRegistra  = arr -> {};

    // Campi login
    private final TextField     loginEmail    = new TextField();
    private final PasswordField loginPassword = new PasswordField();
    private final Label         loginError    = new Label();

    // Campi registrazione
    private final TextField     regNome       = new TextField();
    private final TextField     regCognome    = new TextField();
    private final TextField     regCF         = new TextField();
    private final TextField     regEmail      = new TextField();
    private final PasswordField regPassword   = new PasswordField();
    private final Label         regError      = new Label();

    private final StackPane root;
    private final VBox loginCard;
    private final VBox registerCard;

    public AuthView() {
        loginCard    = buildLoginCard();
        registerCard = buildRegisterCard();
        registerCard.setVisible(false);
        registerCard.setManaged(false);

        StackPane center = new StackPane(loginCard, registerCard);
        center.setMaxWidth(440);

        VBox page = new VBox(center);
        page.setAlignment(Pos.CENTER);
        page.setPadding(new Insets(40, 24, 40, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");
        VBox.setVgrow(center, Priority.ALWAYS);

        root = new StackPane(page);
        root.setStyle("-fx-background-color: " + DARK_BG + ";");
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    public void setOnLogin(BiConsumer<String, String> handler)   { this.onLogin    = handler; }
    public void setOnRegistra(Consumer<String[]> handler)        { this.onRegistra = handler; }

    public void mostraErroreLogin(String msg) {
        loginError.setText(msg);
        loginError.setVisible(true);
    }

    public void mostraErroreRegistra(String msg) {
        regError.setText(msg);
        regError.setVisible(true);
    }

    public void pulisciCampi() {
        loginEmail.clear(); loginPassword.clear(); loginError.setText("");
        regNome.clear(); regCognome.clear(); regCF.clear();
        regEmail.clear(); regPassword.clear(); regError.setText("");
    }

    public StackPane getRoot() { return root; }

    // ── Login card ────────────────────────────────────────────────────────────

    private VBox buildLoginCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(28, 28, 28, 28));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 16;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 16; -fx-border-width: 0.5;");
        card.setMaxWidth(440);

        Label logo = new Label("Destination Buddy");
        logo.setFont(Font.font("System", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(ACCENT));

        Label title = new Label("Accedi");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label sub = new Label("Bentornato su Destination Buddy");
        sub.setFont(Font.font("System", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));

        styleField(loginEmail, "Email");
        styleField(loginPassword, "Password");

        loginError.setFont(Font.font("System", 12));
        loginError.setTextFill(Color.web("#EF4444"));
        loginError.setVisible(false);

        Button loginBtn = new Button("Accedi");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(loginBtn, 12, 0);
        loginBtn.setOnAction(e -> {
            loginError.setVisible(false);
            onLogin.accept(loginEmail.getText().trim(), loginPassword.getText());
        });

        Label switchLbl = new Label("Non hai un account? ");
        switchLbl.setFont(Font.font("System", 13));
        switchLbl.setTextFill(Color.web(TEXT_MUTED));

        Label switchLink = new Label("Registrati");
        switchLink.setFont(Font.font("System", 13));
        switchLink.setTextFill(Color.web(ACCENT));
        switchLink.setCursor(javafx.scene.Cursor.HAND);
        switchLink.setOnMouseClicked(e -> showPanel(false));

        HBox switchRow = new HBox(4, switchLbl, switchLink);
        switchRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(logo, title, sub, loginEmail, loginPassword,
                loginError, loginBtn, switchRow);
        return card;
    }

    // ── Register card ─────────────────────────────────────────────────────────

    private VBox buildRegisterCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(28, 28, 28, 28));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 16;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 16; -fx-border-width: 0.5;");
        card.setMaxWidth(440);

        Label title = new Label("Crea account");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label sub = new Label("Iscriviti gratuitamente");
        sub.setFont(Font.font("System", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));

        HBox nomeRow = new HBox(10);
        styleField(regNome, "Nome");
        styleField(regCognome, "Cognome");
        HBox.setHgrow(regNome, Priority.ALWAYS);
        HBox.setHgrow(regCognome, Priority.ALWAYS);
        nomeRow.getChildren().addAll(regNome, regCognome);

        styleField(regCF, "Codice fiscale");
        styleField(regEmail, "Email");
        styleField(regPassword, "Password (min. 8 caratteri)");

        regError.setFont(Font.font("System", 12));
        regError.setTextFill(Color.web("#EF4444"));
        regError.setVisible(false);

        Button regBtn = new Button("Crea account gratuito");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(regBtn, 12, 0);
        regBtn.setOnAction(e -> {
            regError.setVisible(false);
            onRegistra.accept(new String[]{
                regNome.getText().trim(),
                regCognome.getText().trim(),
                regCF.getText().trim(),
                regEmail.getText().trim(),
                regPassword.getText()
            });
        });

        Label switchLbl = new Label("Hai già un account? ");
        switchLbl.setFont(Font.font("System", 13));
        switchLbl.setTextFill(Color.web(TEXT_MUTED));
        Label switchLink = new Label("Accedi");
        switchLink.setFont(Font.font("System", 13));
        switchLink.setTextFill(Color.web(ACCENT));
        switchLink.setCursor(javafx.scene.Cursor.HAND);
        switchLink.setOnMouseClicked(e -> showPanel(true));

        HBox switchRow = new HBox(4, switchLbl, switchLink);
        switchRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(title, sub, nomeRow, regCF, regEmail,
                regPassword, regError, regBtn, switchRow);
        return card;
    }

    private void showPanel(boolean login) {
        loginCard.setVisible(login);
        loginCard.setManaged(login);
        registerCard.setVisible(!login);
        registerCard.setManaged(!login);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void styleField(TextInputControl field, String prompt) {
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-text-fill: white;"
                + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 10 14;");
    }

    private void styleAccentBtn(Button btn, int padV, int padH) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8;"
                + "-fx-padding: " + padV + (padH > 0 ? " " + padH : "") + ";";
        String hover = base.replace(ACCENT, ACCENT_HOVER);
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }
}
