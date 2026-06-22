package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.LuogoEsplorabile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.function.Consumer;

public class AggiungiLuogoView {

    private Consumer<LuogoEsplorabile> onSalva = l -> {};
    private Consumer<String> onPaeseSelezionato = paese -> {};
    private Runnable onAnnulla = () -> {};

    private final TextField nomeField = new TextField();
    private final ComboBox<String> paeseCombo = new ComboBox<>();
    private final ComboBox<String> zonaCombo = new ComboBox<>();
    private final ComboBox<String> categoriaCombo = new ComboBox<>();
    private final Label errorLabel = new Label();
    private final ScrollPane root;

    public AggiungiLuogoView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Profilo");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());
        breadcrumb.getChildren().add(back);

        Label titoloPag = new Label("Aggiungi nuovo luogo esplorabile");
        titoloPag.getStyleClass().add("auth-title");
        titoloPag.setStyle("-fx-font-size: 22px;");

        VBox formBox = sectionBox("📍 Dettagli del Luogo");
        
        nomeField.setPromptText("Es. Rifugio Vittorio Emanuele II");
        nomeField.getStyleClass().add("form-field");
        
        paeseCombo.setPromptText("Seleziona Paese");
        paeseCombo.setMaxWidth(Double.MAX_VALUE);
        paeseCombo.getStyleClass().add("form-field");
        // Quando la guida sceglie il paese, avvisiamo il controller per caricare le zone corrette
        paeseCombo.setOnAction(e -> {
            String paese = paeseCombo.getValue();
            if (paese != null) onPaeseSelezionato.accept(paese);
        });

        zonaCombo.setPromptText("Seleziona prima un Paese");
        zonaCombo.setMaxWidth(Double.MAX_VALUE);
        zonaCombo.getStyleClass().add("form-field");

        categoriaCombo.setPromptText("Seleziona Categoria");
        categoriaCombo.setMaxWidth(Double.MAX_VALUE);
        categoriaCombo.getStyleClass().add("form-field");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        
        addLabeledField(grid, "Nome del Luogo / Rifugio / Attrazione", nomeField, 0, 0, 2);
        addLabeledField(grid, "Stato / Paese", paeseCombo, 0, 1, 1);
        addLabeledField(grid, "Zona / Regione", zonaCombo, 1, 1, 1);
        addLabeledField(grid, "Categoria", categoriaCombo, 0, 2, 2);

        formBox.getChildren().add(grid);

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        HBox actions = new HBox(12);
        Button salvaBtn = new Button("Salva Luogo");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> tentaSalva());

        Button annullaBtn = new Button("Annulla");
        annullaBtn.getStyleClass().add("btn-ghost");
        annullaBtn.setOnAction(e -> onAnnulla.run());
        actions.getChildren().addAll(salvaBtn, annullaBtn);

        page.getChildren().addAll(breadcrumb, titoloPag, formBox, errorLabel, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.getStyleClass().add("scroll-pane");
    }

    // API Pubbliche per popolare i menu dal Controller
    public void setPaesi(List<String> paesi) { paeseCombo.getItems().setAll(paesi); }
    public void setZone(List<String> zone) { 
        zonaCombo.getItems().setAll(zone); 
        zonaCombo.setPromptText("Seleziona Zona");
    }
    public void setCategorie(List<String> categorie) { categoriaCombo.getItems().setAll(categorie); }

    public void setOnSalva(Consumer<LuogoEsplorabile> handler) { this.onSalva = handler; }
    public void setOnPaeseSelezionato(Consumer<String> handler) { this.onPaeseSelezionato = handler; }
    public void setOnAnnulla(Runnable handler) { this.onAnnulla = handler; }
    public ScrollPane getRoot() { return root; }

    public void pulisciForm() {
        nomeField.clear();
        paeseCombo.setValue(null);
        zonaCombo.getItems().clear();
        zonaCombo.setPromptText("Seleziona prima un Paese");
        categoriaCombo.setValue(null);
        errorLabel.setVisible(false);
    }

    private void tentaSalva() {
        if (nomeField.getText().isBlank() || paeseCombo.getValue() == null || 
            zonaCombo.getValue() == null || categoriaCombo.getValue() == null) {
            errorLabel.setText("Tutti i campi sono obbligatori.");
            errorLabel.setVisible(true);
            return;
        }

        var nuovoLuogo = new LuogoEsplorabile(
            nomeField.getText().trim(),
            zonaCombo.getValue(),
            paeseCombo.getValue(),
            categoriaCombo.getValue()
        );
        onSalva.accept(nuovoLuogo);
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

    private void addLabeledField(GridPane g, String lbl, Control field, int col, int row, int span) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.getStyleClass().add("text-muted");
        field.setMaxWidth(Double.MAX_VALUE);
        cell.getChildren().addAll(l, field);
        g.add(cell, col, row, span, 1);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }
}