package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * CreaEscursioneView — form per le guide per creare una nuova escursione.
 * Light Theme tramite classi CSS.
 *
 * UTILIZZO DAL CONTROLLER:
 *   CreaEscursioneView view = new CreaEscursioneView();
 *   view.setTipologieDisponibili(lista);
 *   view.setOnCrea(escursione -> controller.creaEscursione(escursione));
 *   view.setOnAnnulla(() -> controller.tornaProfilo());
 *   mainView.setContenuto(view.getRoot());
 */
public class CreaEscursioneView {

    private Consumer<EscursioneFormData> onCrea    = d -> {};
    private Runnable                     onAnnulla = () -> {};

    // Campi form
    private final TextField     titolo         = new TextField();
    private final TextArea      descrizione    = new TextArea();
    private final TextField     difficolta     = new TextField();
    private final TextField     costo          = new TextField();
    private final TextField     maxPartecipanti = new TextField();
    private final DatePicker    dataApertura   = new DatePicker();
    private final DatePicker    dataChiusura   = new DatePicker();
    private final VBox          giornateBox    = new VBox(10);
    private final Label         errorLabel     = new Label();

    private List<TipologiaEscursione> tipologieDisponibili = new ArrayList<>();
    private final VBox          tipologieBox   = new VBox(8);

    private final List<GiornataForm> giornataForms = new ArrayList<>();
    private String guidaCF = "";

    private final ScrollPane root;

    public CreaEscursioneView() {
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

        Label titoloPag = new Label("Crea nuova escursione");
        titoloPag.getStyleClass().add("auth-title");
        titoloPag.setStyle("-fx-font-size: 22px;");

        // Sezione info base
        VBox infoBase = sectionBox("📋 Informazioni generali");
        styleField(titolo, "Es. Gran Paradiso Alpine Trek");
        styleArea(descrizione, "Descrivi l'escursione...");
        descrizione.setPrefRowCount(3);

        GridPane grid1 = new GridPane();
        grid1.setHgap(16);
        grid1.setVgap(12);
        addLabeledField(grid1, "Titolo", titolo, 0, 0, 2);
        addLabeledField(grid1, "Descrizione", descrizione, 0, 1, 2);

        GridPane grid2 = new GridPane();
        grid2.setHgap(16);
        grid2.setVgap(12);
        styleField(difficolta, "Es. Facile, Media, Difficile, Esperto");
        styleField(costo, "Es. 350.00");
        styleField(maxPartecipanti, "Es. 12");
        addLabeledField(grid2, "Difficoltà", difficolta, 0, 0, 1);
        addLabeledField(grid2, "Costo iscrizione (€)", costo, 1, 0, 1);
        addLabeledField(grid2, "Max partecipanti", maxPartecipanti, 2, 0, 1);

        styleDatePicker(dataApertura, "Data apertura iscrizioni");
        styleDatePicker(dataChiusura, "Data chiusura iscrizioni");

        GridPane grid3 = new GridPane();
        grid3.setHgap(16);
        grid3.setVgap(12);
        addLabeledNode(grid3, "Apertura iscrizioni", dataApertura, 0, 0);
        addLabeledNode(grid3, "Chiusura iscrizioni", dataChiusura, 1, 0);

        infoBase.getChildren().addAll(grid1, grid2, grid3);

        // Tipologie
        VBox tipologieSection = sectionBox("🏷 Tipologie escursione");
        tipologieSection.getChildren().add(tipologieBox);

        // Giornate
        VBox giornateSection = sectionBox("📅 Programma giornaliero");
        Button addGiornataBtn = new Button("+ Aggiungi giornata");
        addGiornataBtn.getStyleClass().add("btn-ghost");
        addGiornataBtn.setOnAction(e -> aggiungiGiornata());
        giornateSection.getChildren().addAll(giornateBox, addGiornataBtn);

        // Aggiungi prima giornata di default
        aggiungiGiornata();

        // Errore
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        // Pulsanti
        HBox actions = new HBox(12);
        Button creaBtn = new Button("Pubblica escursione");
        creaBtn.getStyleClass().add("btn-accent");
        creaBtn.setOnAction(e -> tentaCrea());

        Button annullaBtn = new Button("Annulla");
        annullaBtn.getStyleClass().add("btn-ghost");
        annullaBtn.setOnAction(e -> onAnnulla.run());
        actions.getChildren().addAll(creaBtn, annullaBtn);

        page.getChildren().addAll(breadcrumb, titoloPag, infoBase,
                tipologieSection, giornateSection, errorLabel, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    // ── API pubblica ──────────────────────────────────────────────

    public void setTipologieDisponibili(List<TipologiaEscursione> lista) {
        this.tipologieDisponibili = lista;
        tipologieBox.getChildren().clear();

        FlowPane flow = new FlowPane(10, 8);

        for (TipologiaEscursione tipologia : lista) {
            CheckBox cb = new CheckBox(tipologia.idTipologia);
            cb.getStyleClass().add("sidebar-item-text");
            flow.getChildren().add(cb);
        }

        tipologieBox.getChildren().add(flow);
    }

    public void setGuidaCF(String cf)                            { this.guidaCF = cf; }
    public void setOnCrea(Consumer<EscursioneFormData> handler)  { this.onCrea = handler; }
    public void setOnAnnulla(Runnable handler)                   { this.onAnnulla = handler; }
    public ScrollPane getRoot()                                  { return root; }

    public void mostraErrore(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public void pulisciForm() {
        titolo.clear(); descrizione.clear(); difficolta.clear();
        costo.clear(); maxPartecipanti.clear();
        dataApertura.setValue(null); dataChiusura.setValue(null);
        giornataForms.clear(); giornateBox.getChildren().clear();
        errorLabel.setVisible(false);
        aggiungiGiornata();
    }

    public void mostraConferma(String titoloEscursione) {
        ScrollPane sp = (ScrollPane) root;
        VBox page = (VBox) sp.getContent();
        page.getChildren().clear();

        VBox success = new VBox(16);
        success.setAlignment(Pos.CENTER);
        success.setPadding(new Insets(60, 0, 0, 0));

        Label icon = new Label("✅");
        icon.setStyle("-fx-font-size: 48px;");

        Label titolo = new Label("Escursione pubblicata!");
        titolo.getStyleClass().add("auth-title");
        titolo.setStyle("-fx-font-size: 22px;");

        Label sub = new Label("\"" + titoloEscursione + "\" è ora visibile nel catalogo.");
        sub.getStyleClass().add("text-muted");

        Button tornaBtn = new Button("Torna al profilo");
        tornaBtn.getStyleClass().add("btn-accent");
        tornaBtn.setOnAction(e -> onAnnulla.run());

        success.getChildren().addAll(icon, titolo, sub, tornaBtn);
        page.getChildren().add(success);
    }

    // ── Giornate ──────────────────────────────────────────────────

    private void aggiungiGiornata() {
        int numero = giornataForms.size() + 1;
        GiornataForm gf = new GiornataForm(numero, null);
        giornataForms.add(gf);
        giornateBox.getChildren().add(gf.build(numero));
    }

    // ── Validazione e creazione ───────────────────────────────────

    private void tentaCrea() {
        errorLabel.setVisible(false);

        if (titolo.getText().isBlank()) { mostraErrore("Inserisci un titolo."); return; }
        if (difficolta.getText().isBlank()) { mostraErrore("Inserisci la difficoltà."); return; }
        if (dataApertura.getValue() == null || dataChiusura.getValue() == null) {
            mostraErrore("Inserisci le date di apertura e chiusura."); return;
        }

        double costoVal;
        int maxP;
        try {
            costoVal = Double.parseDouble(costo.getText().replace(",", "."));
            maxP     = Integer.parseInt(maxPartecipanti.getText());
        } catch (NumberFormatException ex) {
            mostraErrore("Costo e numero partecipanti devono essere numeri validi."); return;
        }

        // Raccoglie tipologie selezionate
        List<String> tipologieSel = new ArrayList<>();

        if (!tipologieBox.getChildren().isEmpty()
                && tipologieBox.getChildren().get(0) instanceof FlowPane fp) {

            for (var node : fp.getChildren()) {
                if (node instanceof CheckBox cb && cb.isSelected()) {
                    tipologieSel.add(cb.getText());
                }
            }
        }

        // Raccoglie giornate
        List<Giornata> giornate = new ArrayList<>();
        String idEsc = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        for (GiornataForm gf : giornataForms) {
            LocalDate dataG = gf.dataPicker.getValue();
            String prog     = gf.programmaField.getText();
            if (dataG != null && !prog.isBlank()) {
                giornate.add(new Giornata(idEsc, dataG, prog, new ArrayList<>()));
            }
        }

        Escursione nuova = new Escursione(
            idEsc, titolo.getText(), difficolta.getText(), costoVal, maxP,
            dataApertura.getValue(), dataChiusura.getValue(),
            new ArrayList<>(), "", "", giornate, new ArrayList<>(), tipologieSel
        );

        onCrea.accept(new EscursioneFormData(nuova, descrizione.getText(), maxP, guidaCF, tipologieSel));
    }

    // ── Helper classi interne ─────────────────────────────────────

    /** DTO che aggrega tutti i dati necessari a EscursioniModel.creaEscursione() */
    public static class EscursioneFormData {
        public final Escursione escursione;
        public final String     descrizione;
        public final int        numeroPartecipanti;
        public final String     guidaCF;
        public final List<String> tipologie;

        public EscursioneFormData(Escursione e, String desc, int np, String cf, List<String> tip) {
            this.escursione         = e;
            this.descrizione        = desc;
            this.numeroPartecipanti = np;
            this.guidaCF            = cf;
            this.tipologie          = tip;
        }
    }

    private static class GiornataForm {
        final int        numero;
        final DatePicker dataPicker     = new DatePicker();
        final TextField  programmaField = new TextField();
        final Runnable   onRimuovi;

        GiornataForm(int n, Runnable onRimuovi) {
            this.numero    = n;
            this.onRimuovi = onRimuovi;
        }

        VBox build(int n) {
            VBox box = new VBox(8);
            box.setId(n + "-giornata");
            box.setPadding(new Insets(12));
            box.getStyleClass().add("card");

            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label("Giorno " + n);
            lbl.getStyleClass().add("text-accent");
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            header.getChildren().addAll(lbl, sp);

            dataPicker.setPromptText("Data giornata");
            dataPicker.setMaxWidth(Double.MAX_VALUE);
            dataPicker.getStyleClass().add("form-field");

            programmaField.setPromptText("Descrivi il programma della giornata...");
            programmaField.getStyleClass().add("form-field");

            box.getChildren().addAll(header, dataPicker, programmaField);
            return box;
        }
    }

    // ── UI helpers ────────────────────────────────────────────────

    private VBox sectionBox(String titolo) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.getStyleClass().add("card");
        Label lbl = new Label(titolo);
        lbl.getStyleClass().add("card-title");
        box.getChildren().add(lbl);
        return box;
    }

    private void styleField(TextField f, String prompt) {
        f.setPromptText(prompt);
        f.getStyleClass().add("form-field");
    }

    private void styleArea(TextArea a, String prompt) {
        a.setPromptText(prompt);
        a.getStyleClass().add("form-field");
    }

    private void styleDatePicker(DatePicker dp, String prompt) {
        dp.setPromptText(prompt);
        dp.getStyleClass().add("form-field");
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

    private void addLabeledNode(GridPane g, String lbl, javafx.scene.Node node, int col, int row) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.getStyleClass().add("text-muted");
        cell.getChildren().addAll(l, node);
        g.add(cell, col, row);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }
}
