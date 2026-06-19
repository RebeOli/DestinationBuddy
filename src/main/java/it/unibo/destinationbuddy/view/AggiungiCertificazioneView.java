package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

/**
 * AggiungiCertificazioneView — form per aggiungere una certificazione dal profilo.
 *
 * UTILIZZO DAL CONTROLLER:
 *   AggiungiCertificazioneView view = new AggiungiCertificazioneView();
 *   view.setTipologie(lista);
 *   view.setOnSalva(cert -> controller.aggiungi(cert));
 *   view.setOnAnnulla(() -> controller.tornaProfilo());
 *   mainView.setContenuto(view.getRoot());
 */
public class AggiungiCertificazioneView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

    private Consumer<Certificazione> onSalva   = c -> {};
    private Runnable                 onAnnulla = () -> {};

    private String cfUtente = "";
    private List<TipologiaCertificazione> tipologie = List.of();

    // Campi form
    private final ComboBox<TipologiaCertificazione> tipologiaBox = new ComboBox<>();
    private final TextField  nCertificazione = new TextField();
    private final TextField  enteRilasciante = new TextField();
    private final DatePicker dataRilascio    = new DatePicker();
    private final DatePicker dataScadenza    = new DatePicker();
    private final Label      errorLabel      = new Label();

    private final ScrollPane root;

    public AggiungiCertificazioneView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");

        // Breadcrumb
        Label back = new Label("← Profilo");
        back.setFont(Font.font("System", 12));
        back.setTextFill(Color.web(ACCENT));
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());

        Label titoloPag = new Label("Aggiungi certificazione");
        titoloPag.setFont(Font.font("System", FontWeight.BOLD, 22));
        titoloPag.setTextFill(Color.WHITE);

        // Card form
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 14; -fx-border-width: 0.5;");

        // Tipologia (ComboBox)
        VBox tipologiaGroup = fieldGroup("Tipologia certificazione");
        tipologiaBox.setMaxWidth(Double.MAX_VALUE);
        tipologiaBox.setPromptText("Seleziona tipologia...");
        tipologiaBox.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8;");
        tipologiaBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TipologiaCertificazione item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.idCertificazione + " — " + item.livello);
            }
        });
        tipologiaBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipologiaCertificazione item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleziona tipologia..." : item.idCertificazione + " — " + item.livello);
                setTextFill(item == null ? Color.web(TEXT_MUTED) : Color.WHITE);
            }
        });
        tipologiaGroup.getChildren().add(tipologiaBox);

        // Numero certificazione
        VBox nCertGroup = fieldGroup("Numero certificazione");
        styleField(nCertificazione, "Es. AG-GD-001");
        nCertGroup.getChildren().add(nCertificazione);

        // Ente rilasciante
        VBox enteGroup = fieldGroup("Ente rilasciante");
        styleField(enteRilasciante, "Es. UIAGM, CAI, Croce Rossa...");
        enteGroup.getChildren().add(enteRilasciante);

        // Date
        HBox dateRow = new HBox(16);
        VBox dataRilascioGroup = fieldGroup("Data rilascio");
        styleDatePicker(dataRilascio);
        dataRilascioGroup.getChildren().add(dataRilascio);
        HBox.setHgrow(dataRilascioGroup, Priority.ALWAYS);

        VBox dataScadenzaGroup = fieldGroup("Data scadenza");
        styleDatePicker(dataScadenza);
        dataScadenzaGroup.getChildren().add(dataScadenza);
        HBox.setHgrow(dataScadenzaGroup, Priority.ALWAYS);

        dateRow.getChildren().addAll(dataRilascioGroup, dataScadenzaGroup);

        // Info stato validazione
        Label infoLbl = new Label("ℹ La certificazione sarà in stato \"in attesa\" fino alla verifica da parte dell'amministratore.");
        infoLbl.setFont(Font.font("System", 12));
        infoLbl.setTextFill(Color.web("#60A5FA"));
        infoLbl.setWrapText(true);
        infoLbl.setStyle("-fx-background-color: rgba(96,165,250,0.1); -fx-background-radius: 8; -fx-padding: 10 14;");

        // Errore
        errorLabel.setFont(Font.font("System", 12));
        errorLabel.setTextFill(Color.web("#EF4444"));
        errorLabel.setVisible(false);

        card.getChildren().addAll(tipologiaGroup, nCertGroup, enteGroup, dateRow, infoLbl, errorLabel);

        // Pulsanti
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button salvaBtn = new Button("Aggiungi certificazione");
        salvaBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(salvaBtn);
        salvaBtn.setOnAction(e -> tentaSalva());

        Button annullaBtn = new Button("Annulla");
        styleGhostBtn(annullaBtn);
        annullaBtn.setOnAction(e -> onAnnulla.run());

        actions.getChildren().addAll(salvaBtn, annullaBtn);

        page.getChildren().addAll(back, titoloPag, card, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────

    public void setTipologie(List<TipologiaCertificazione> lista) {
        this.tipologie = lista;
        tipologiaBox.getItems().setAll(lista);
    }

    public void setCfUtente(String cf)                     { this.cfUtente = cf; }
    public void setOnSalva(Consumer<Certificazione> h)     { this.onSalva   = h; }
    public void setOnAnnulla(Runnable h)                   { this.onAnnulla = h; }
    public ScrollPane getRoot()                            { return root; }

    public void mostraErrore(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public void pulisciForm() {
        tipologiaBox.getSelectionModel().clearSelection();
        nCertificazione.clear();
        enteRilasciante.clear();
        dataRilascio.setValue(null);
        dataScadenza.setValue(null);
        errorLabel.setVisible(false);
    }

    // ── Validazione ───────────────────────────────────────────────

    private void tentaSalva() {
        errorLabel.setVisible(false);

        TipologiaCertificazione tipSel = tipologiaBox.getValue();
        if (tipSel == null)               { mostraErrore("Seleziona una tipologia."); return; }
        if (nCertificazione.getText().isBlank()) { mostraErrore("Inserisci il numero certificazione."); return; }
        if (enteRilasciante.getText().isBlank()) { mostraErrore("Inserisci l'ente rilasciante."); return; }
        if (dataRilascio.getValue() == null)     { mostraErrore("Inserisci la data di rilascio."); return; }
        if (dataScadenza.getValue() == null)     { mostraErrore("Inserisci la data di scadenza."); return; }
        if (dataScadenza.getValue().isBefore(dataRilascio.getValue())) {
            mostraErrore("La data di scadenza deve essere dopo il rilascio."); return;
        }

        Certificazione nuova = new Certificazione(
            tipSel,
            nCertificazione.getText().trim(),
            enteRilasciante.getText().trim(),
            dataRilascio.getValue(),
            dataScadenza.getValue(),
            "in_attesa",
            cfUtente,
            null
        );

        onSalva.accept(nuova);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private VBox fieldGroup(String labelTesto) {
        VBox group = new VBox(5);
        Label lbl = new Label(labelTesto);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        group.getChildren().add(lbl);
        return group;
    }

    private void styleField(TextField f, String prompt) {
        f.setPromptText(prompt);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-text-fill: white;"
                + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 10 12;");
    }

    private void styleDatePicker(DatePicker dp) {
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8;");
    }

    private void styleAccentBtn(Button btn) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 10 22; -fx-font-weight: bold;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(ACCENT, ACCENT_HOVER)));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }

    private void styleGhostBtn(Button btn) {
        String s = "-fx-background-color: rgba(255,255,255,0.07); -fx-text-fill: white;"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 18;";
        btn.setStyle(s);
        btn.setOnMouseEntered(e -> btn.setStyle(s.replace("0.07","0.14")));
        btn.setOnMouseExited(e -> btn.setStyle(s));
    }
}
