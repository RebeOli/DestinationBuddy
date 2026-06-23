package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.Resoconto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;

public class InserisciResocontoView {

    private Runnable              onAnnulla = () -> {};
    private Consumer<Resoconto>   onSalva   = r -> {};
    private String cfGuida = "";
    private final ComboBox<EscursionePreview> escursioneCombo = new ComboBox<>();
    private final DatePicker dataInizio  = new DatePicker();
    private final DatePicker dataFine    = new DatePicker();
    private final TextField  temperatura = new TextField();
    private final TextField  precipitazioni = new TextField();
    private final Label      errorLabel  = new Label();

    private final ScrollPane root;

    public InserisciResocontoView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Profilo");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());
        breadcrumb.getChildren().add(back);

        Label titoloPag = new Label("Inserisci resoconto escursione");
        titoloPag.getStyleClass().add("auth-title");
        titoloPag.setStyle("-fx-font-size: 22px;");
        VBox formSection = sectionBox("📋 Dati resoconto");
        escursioneCombo.setMaxWidth(Double.MAX_VALUE);
        escursioneCombo.setPromptText("Seleziona un'escursione...");
        escursioneCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(EscursionePreview item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.titolo + " (" + item.idEscursione + ")");
            }
        });
        escursioneCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(EscursionePreview item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.titolo + " (" + item.idEscursione + ")");
            }
        });

        styleDatePicker(dataInizio, "Data inizio escursione");
        styleDatePicker(dataFine, "Data fine escursione");

        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(16);
        dateGrid.setVgap(12);
        addLabeledNode(dateGrid, "Data inizio", dataInizio, 0, 0);
        addLabeledNode(dateGrid, "Data fine", dataFine, 1, 0);
        styleField(temperatura, "Es. 18.5");
        styleField(precipitazioni, "Es. 0.0");
        GridPane meteoGrid = new GridPane();
        meteoGrid.setHgap(16);
        meteoGrid.setVgap(12);
        addLabeledField(meteoGrid, "Temperatura rilevata (°C)", temperatura, 0, 0);
        addLabeledField(meteoGrid, "Precipitazioni (mm)", precipitazioni, 1, 0);
        formSection.getChildren().addAll(
            labeledNode("Escursione", escursioneCombo),
            dateGrid,
            meteoGrid
        );
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        HBox actions = new HBox(12);
        Button salvaBtn = new Button("Salva resoconto");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> tentaSalva());

        Button annullaBtn = new Button("Annulla");
        annullaBtn.getStyleClass().add("btn-ghost");
        annullaBtn.setOnAction(e -> onAnnulla.run());
        actions.getChildren().addAll(salvaBtn, annullaBtn);

        page.getChildren().addAll(breadcrumb, titoloPag, formSection, errorLabel, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    public void setEscursioni(List<EscursionePreview> lista) {
        escursioneCombo.getItems().clear();
        if (lista != null) escursioneCombo.getItems().addAll(lista);
    }

    public void setCfGuida(String cf) { this.cfGuida = cf; }
    public void setOnSalva(Consumer<Resoconto> handler)  { this.onSalva   = handler; }
    public void setOnAnnulla(Runnable handler)            { this.onAnnulla = handler; }
    public ScrollPane getRoot()                           { return root; }

    public void pulisciForm() {
        escursioneCombo.getSelectionModel().clearSelection();
        dataInizio.setValue(null);
        dataFine.setValue(null);
        temperatura.clear();
        precipitazioni.clear();
        errorLabel.setVisible(false);
    }

    public void mostraErrore(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public void mostraConferma() {
        ScrollPane sp = root;
        VBox page = (VBox) sp.getContent();
        page.getChildren().clear();

        VBox success = new VBox(16);
        success.setAlignment(Pos.CENTER);
        success.setPadding(new Insets(60, 0, 0, 0));

        Label icon = new Label("✅");
        icon.setStyle("-fx-font-size: 48px;");

        Label titolo = new Label("Resoconto salvato!");
        titolo.getStyleClass().add("auth-title");
        titolo.setStyle("-fx-font-size: 22px;");

        Label sub = new Label("Il resoconto è stato registrato correttamente.");
        sub.getStyleClass().add("text-muted");

        Button tornaBtn = new Button("Torna al profilo");
        tornaBtn.getStyleClass().add("btn-accent");
        tornaBtn.setOnAction(e -> onAnnulla.run());

        success.getChildren().addAll(icon, titolo, sub, tornaBtn);
        page.getChildren().add(success);
    }

    private void tentaSalva() {
        errorLabel.setVisible(false);

        EscursionePreview sel = escursioneCombo.getSelectionModel().getSelectedItem();
        if (sel == null) { mostraErrore("Seleziona un'escursione."); return; }
        if (dataInizio.getValue() == null) { mostraErrore("Inserisci la data di inizio."); return; }
        if (dataFine.getValue() == null)   { mostraErrore("Inserisci la data di fine."); return; }
        if (dataFine.getValue().isBefore(dataInizio.getValue())) {
            mostraErrore("La data di fine non può essere precedente alla data di inizio."); return;
        }

        double temp;
        double prec;
        try {
            temp = Double.parseDouble(temperatura.getText().replace(",", "."));
            prec = Double.parseDouble(precipitazioni.getText().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostraErrore("Temperatura e precipitazioni devono essere numeri validi."); return;
        }

        Resoconto r = new Resoconto(
            sel.idEscursione,
            dataInizio.getValue(),
            dataFine.getValue(),
            temp,
            prec,
            cfGuida
        );

        onSalva.accept(r);
    }

    private VBox sectionBox(String titolo) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.getStyleClass().add("card");
        Label lbl = new Label(titolo);
        lbl.getStyleClass().add("card-title");
        box.getChildren().add(lbl);
        return box;
    }

    private VBox labeledNode(String lbl, javafx.scene.Node node) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.getStyleClass().add("text-muted");
        cell.getChildren().addAll(l, node);
        return cell;
    }

    private void styleField(TextField f, String prompt) {
        f.setPromptText(prompt);
        f.getStyleClass().add("form-field");
    }

    private void styleDatePicker(DatePicker dp, String prompt) {
        dp.setPromptText(prompt);
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.getStyleClass().add("form-field");
    }

    private void addLabeledField(GridPane g, String lbl, Control field, int col, int row) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.getStyleClass().add("text-muted");
        field.setMaxWidth(Double.MAX_VALUE);
        cell.getChildren().addAll(l, field);
        g.add(cell, col, row);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private void addLabeledNode(GridPane g, String lbl, javafx.scene.Node node, int col, int row) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.getStyleClass().add("text-muted");
        cell.getChildren().addAll(l, node);
        g.add(cell, col, row);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }
}
