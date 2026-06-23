package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.LuogoEsplorabile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.function.Consumer;

public class GestioneGeograficaAdminView {

    private Consumer<LuogoEsplorabile> onSalvaLuogo = l -> {};
    private Consumer<String> onSalvaPaese = p -> {};
    private Consumer<String[]> onSalvaZona = z -> {};
    private Consumer<String> onSalvaCategoria = c -> {};
    private final TextField nuovaCategoriaField = new TextField();

    private Consumer<String> onPaeseSelezionato = paese -> {};
    private Runnable onAnnulla = () -> {};
    private final TextField nomeField = new TextField();
    private final ComboBox<String> paeseComboLuogo = new ComboBox<>();
    private final ComboBox<String> zonaComboLuogo = new ComboBox<>();
    private final ComboBox<String> categoriaCombo = new ComboBox<>();
    private final TextField quotaField = new TextField();
    private final TextField nuovoPaeseField = new TextField();
    private final ComboBox<String> paeseComboZona = new ComboBox<>();
    private final TextField nuovaZonaField = new TextField();
    private final TextArea descZonaField = new TextArea();

    private final Label errorLabel = new Label();
    private final ScrollPane root;

    public GestioneGeograficaAdminView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));

        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Torna all'Admin Dashboard");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());
        breadcrumb.getChildren().add(back);

        Label titoloPag = new Label("Gestione Geografica");
        titoloPag.getStyleClass().add("auth-title");
        titoloPag.setStyle("-fx-font-size: 22px;");

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("card");
        Tab tabPaese = new Tab("1. Aggiungi Paese");
        tabPaese.setContent(buildTabPaese());

        Tab tabZona = new Tab("2. Aggiungi Zona");
        tabZona.setContent(buildTabZona());

        Tab tabCategoria = new Tab("3. Aggiungi Categoria");
        tabCategoria.setContent(buildTabCategoria());

        Tab tabLuogo = new Tab("4. Aggiungi Luogo");
        tabLuogo.setContent(buildTabLuogo());
        tabPane.getTabs().addAll(tabPaese, tabZona, tabCategoria, tabLuogo);

        page.getChildren().addAll(breadcrumb, titoloPag, tabPane, errorLabel);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.getStyleClass().add("scroll-pane");
    }

    private VBox buildTabPaese() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        nuovoPaeseField.setPromptText("Es. Giappone");
        nuovoPaeseField.getStyleClass().add("form-field");

        Button salvaBtn = new Button("Salva Paese");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> {
            if(nuovoPaeseField.getText().isBlank()) {
                mostraErrore("Inserisci il nome del paese."); return;
            }
            onSalvaPaese.accept(nuovoPaeseField.getText().trim());
        });

        box.getChildren().addAll(new Label("Nome del nuovo Paese:"), nuovoPaeseField, salvaBtn);
        return box;
    }

    private VBox buildTabZona() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        paeseComboZona.setPromptText("Seleziona Paese");
        paeseComboZona.setMaxWidth(Double.MAX_VALUE);
        
        nuovaZonaField.setPromptText("Es. Kanto");
        nuovaZonaField.getStyleClass().add("form-field");

        descZonaField.setPromptText("Descrizione della zona...");
        descZonaField.setPrefRowCount(3);
        descZonaField.getStyleClass().add("form-field");

        Button salvaBtn = new Button("Salva Zona");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> {
            if(paeseComboZona.getValue() == null || nuovaZonaField.getText().isBlank()) {
                mostraErrore("Paese e Nome Zona sono obbligatori."); return;
            }
            String[] dati = {paeseComboZona.getValue(), nuovaZonaField.getText().trim(), descZonaField.getText().trim()};
            onSalvaZona.accept(dati);
        });

        box.getChildren().addAll(
            new Label("Seleziona Paese:"), paeseComboZona,
            new Label("Nome della nuova Zona:"), nuovaZonaField,
            new Label("Descrizione:"), descZonaField,
            salvaBtn
        );
        return box;
    }

    private VBox buildTabCategoria() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        nuovaCategoriaField.setPromptText("Es. Cascate, Vulcano...");
        nuovaCategoriaField.getStyleClass().add("form-field");

        Button salvaBtn = new Button("Salva Categoria");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> {
            if(nuovaCategoriaField.getText().isBlank()) {
                mostraErrore("Inserisci il nome della categoria."); return;
            }
            onSalvaCategoria.accept(nuovaCategoriaField.getText().trim());
        });

        box.getChildren().addAll(new Label("Nome della nuova Categoria:"), nuovaCategoriaField, salvaBtn);
        return box;
    }

    private VBox buildTabLuogo() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        nomeField.setPromptText("Es. Monte Fuji");
        nomeField.getStyleClass().add("form-field");
        
        quotaField.setPromptText("Es. 3776");
        quotaField.getStyleClass().add("form-field");

        paeseComboLuogo.setPromptText("Seleziona Paese");
        paeseComboLuogo.setMaxWidth(Double.MAX_VALUE);
        paeseComboLuogo.setOnAction(e -> {
            String paese = paeseComboLuogo.getValue();
            if (paese != null) onPaeseSelezionato.accept(paese);
        });

        zonaComboLuogo.setPromptText("Seleziona prima un Paese");
        zonaComboLuogo.setMaxWidth(Double.MAX_VALUE);

        categoriaCombo.setPromptText("Seleziona Categoria");
        categoriaCombo.setMaxWidth(Double.MAX_VALUE);

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(12);
        
        addLabeledField(grid, "Nome del Luogo / Attrazione", nomeField, 0, 0, 2);
        addLabeledField(grid, "Quota (m)", quotaField, 2, 0, 1);
        addLabeledField(grid, "Stato / Paese", paeseComboLuogo, 0, 1, 1);
        addLabeledField(grid, "Zona / Regione", zonaComboLuogo, 1, 1, 1);
        addLabeledField(grid, "Categoria", categoriaCombo, 0, 2, 2);

        Button salvaBtn = new Button("Salva Luogo");
        salvaBtn.getStyleClass().add("btn-accent");
        salvaBtn.setOnAction(e -> tentaSalvaLuogo());

        box.getChildren().addAll(grid, salvaBtn);
        return box;
    }

    public void setPaesi(List<String> paesi) { 
        paeseComboLuogo.getItems().setAll(paesi); 
        paeseComboZona.getItems().setAll(paesi);
    }
    
    public void setZone(List<String> zone) { 
        zonaComboLuogo.getItems().setAll(zone); 
        zonaComboLuogo.setPromptText("Seleziona Zona");
    }
    
    public void setCategorie(List<String> categorie) { categoriaCombo.getItems().setAll(categorie); }

    public void setOnSalvaLuogo(Consumer<LuogoEsplorabile> handler) { this.onSalvaLuogo = handler; }
    public void setOnSalvaPaese(Consumer<String> handler) { this.onSalvaPaese = handler; }
    public void setOnSalvaZona(Consumer<String[]> handler) { this.onSalvaZona = handler; }
    public void setOnSalvaCategoria(Consumer<String> handler) { this.onSalvaCategoria = handler; }
    public void setOnPaeseSelezionato(Consumer<String> handler) { this.onPaeseSelezionato = handler; }
    public void setOnAnnulla(Runnable handler) { this.onAnnulla = handler; }
    public ScrollPane getRoot() { return root; }

    public void mostraErrore(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public void pulisciForm() {
        nomeField.clear(); quotaField.clear();
        paeseComboLuogo.setValue(null); zonaComboLuogo.getItems().clear();
        zonaComboLuogo.setPromptText("Seleziona prima un Paese");
        categoriaCombo.setValue(null);
        
        nuovoPaeseField.clear();
        paeseComboZona.setValue(null); nuovaZonaField.clear(); descZonaField.clear();

        errorLabel.setVisible(false);
        nuovaCategoriaField.clear();
    }

    private void tentaSalvaLuogo() {
        if (nomeField.getText().isBlank() || paeseComboLuogo.getValue() == null || 
            zonaComboLuogo.getValue() == null || categoriaCombo.getValue() == null) {
            mostraErrore("Tutti i campi (tranne quota) sono obbligatori.");
            return;
        }

        var nuovoLuogo = new LuogoEsplorabile(
            nomeField.getText().trim(),
            zonaComboLuogo.getValue(),
            paeseComboLuogo.getValue(),
            categoriaCombo.getValue()
        );
        onSalvaLuogo.accept(nuovoLuogo);
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