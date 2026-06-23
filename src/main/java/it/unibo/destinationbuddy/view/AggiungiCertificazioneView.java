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

import java.util.List;
import java.util.function.Consumer;

public class AggiungiCertificazioneView {

    private static final String APP_BG     = "#F4EFE6";
    private static final String ACCENT     = "#B85D38";
    private static final String TEXT_DARK  = "#2C2A26";
    private static final String TEXT_MUTED = "#807B73";
    private Consumer<Certificazione> onSalva   = c -> {};
    private Runnable                 onAnnulla = () -> {};
    private String cfUtente = "";
    private List<TipologiaCertificazione> tipologie = List.of();
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
        page.setStyle("-fx-background-color: " + APP_BG + ";");
        Label back = new Label("← Profilo");
        back.setFont(Font.font("System", 12));
        back.setTextFill(Color.web(ACCENT));
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());

        Label titoloPag = new Label("Aggiungi certificazione");
        titoloPag.setFont(Font.font("System", FontWeight.BOLD, 24));
        titoloPag.setTextFill(Color.web(TEXT_DARK));
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.getStyleClass().add("card");
        VBox tipologiaGroup = fieldGroup("Tipologia certificazione");
        tipologiaBox.setMaxWidth(Double.MAX_VALUE);
        tipologiaBox.setPromptText("Seleziona tipologia...");
        tipologiaBox.getStyleClass().add("form-field");
        tipologiaBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TipologiaCertificazione item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.idCertificazione + " — " + item.livello);
                setTextFill(Color.web(TEXT_DARK));
            }
        });
        tipologiaBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipologiaCertificazione item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleziona tipologia..." : item.idCertificazione + " — " + item.livello);
                setTextFill(item == null ? Color.web(TEXT_MUTED) : Color.web(TEXT_DARK));
            }
        });

        tipologiaGroup.getChildren().add(tipologiaBox);
        VBox nCertGroup = fieldGroup("Numero certificazione");
        styleField(nCertificazione, "Es. AG-GD-001");
        nCertGroup.getChildren().add(nCertificazione);
        VBox enteGroup = fieldGroup("Ente rilasciante");
        styleField(enteRilasciante, "Es. UIAGM, CAI, Croce Rossa...");
        enteGroup.getChildren().add(enteRilasciante);
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
        Label infoLbl = new Label("ℹ La certificazione sarà in stato \"in attesa\" fino alla verifica da parte dell'amministratore.");
        infoLbl.setFont(Font.font("System", 12));
        infoLbl.setTextFill(Color.web("#2874A6"));
        infoLbl.setWrapText(true);
        infoLbl.setStyle("-fx-background-color: #EBF5F8; -fx-background-radius: 8; -fx-padding: 10 14; -fx-border-color: #B5D2D9; -fx-border-radius: 8;");

        errorLabel.setFont(Font.font("System", 12));
        errorLabel.setTextFill(Color.web("#B03A2E"));
        errorLabel.setVisible(false);

        card.getChildren().addAll(tipologiaGroup, nCertGroup, enteGroup, dateRow, infoLbl, errorLabel);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button salvaBtn = new Button("Aggiungi certificazione");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> tentaSalva());

        Button annullaBtn = new Button("Annulla");
        annullaBtn.getStyleClass().add("btn-ghost");
        annullaBtn.setOnAction(e -> onAnnulla.run());

        actions.getChildren().addAll(salvaBtn, annullaBtn);

        page.getChildren().addAll(back, titoloPag, card, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + APP_BG + "; -fx-background-color: " + APP_BG + "; -fx-border-color: transparent;");
    }

    public void setTipologie(List<TipologiaCertificazione> lista) {
        this.tipologie = lista;
        tipologiaBox.getItems().setAll(lista);
    }

    public void setCfUtente(String cf) { this.cfUtente = cf; }
    public void setOnSalva(Consumer<Certificazione> h) { this.onSalva   = h; }
    public void setOnAnnulla(Runnable h) { this.onAnnulla = h; }
    public ScrollPane getRoot() { return root; }

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

    private VBox fieldGroup(String labelTesto) {
        VBox group = new VBox(5);
        Label lbl = new Label(labelTesto);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web(TEXT_DARK));
        group.getChildren().add(lbl);
        return group;
    }

    private void styleField(TextField f, String prompt) {
        f.setPromptText(prompt);
        f.setMaxWidth(Double.MAX_VALUE);
        f.getStyleClass().add("form-field");
    }

    private void styleDatePicker(DatePicker dp) {
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.getStyleClass().add("form-field");
    }
}