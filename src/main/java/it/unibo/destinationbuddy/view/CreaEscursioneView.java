package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * CreaEscursioneView — form per le guide per creare una nuova escursione.
 *
 * UTILIZZO DAL CONTROLLER:
 *   CreaEscursioneView view = new CreaEscursioneView();
 *   view.setTipologieDisponibili(lista);
 *   view.setOnCrea(escursione -> controller.creaEscursione(escursione));
 *   view.setOnAnnulla(() -> controller.tornaProfilo());
 *   mainView.setContenuto(view.getRoot());
 */
public class CreaEscursioneView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

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
    private final CheckBox[]    tipologieCheck = new CheckBox[20];
    private final VBox          tipologieBox   = new VBox(8);

    private final List<GiornataForm> giornataForms = new ArrayList<>();
    private String guidaCF = "";

    private final ScrollPane root;

    public CreaEscursioneView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + DARK_BG + ";");

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Profilo");
        back.setFont(Font.font("System", 12));
        back.setTextFill(Color.web(ACCENT));
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onAnnulla.run());
        breadcrumb.getChildren().add(back);

        Label titoloPag = new Label("Crea nuova escursione");
        titoloPag.setFont(Font.font("System", FontWeight.BOLD, 22));
        titoloPag.setTextFill(Color.WHITE);

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

        dataApertura.setPromptText("Data apertura iscrizioni");
        dataApertura.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8;");
        dataChiusura.setPromptText("Data chiusura iscrizioni");
        dataChiusura.setStyle(dataApertura.getStyle());

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
        addGiornataBtn.setFont(Font.font("System", 12));
        addGiornataBtn.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-text-fill: white; -fx-border-color: rgba(255,255,255,0.2);"
                + "-fx-border-radius: 8; -fx-background-radius: 8;"
                + "-fx-cursor: hand; -fx-padding: 7 14;");
        addGiornataBtn.setOnAction(e -> aggiungiGiornata());
        giornateSection.getChildren().addAll(giornateBox, addGiornataBtn);

        // Aggiungi prima giornata di default
        aggiungiGiornata();

        // Errore
        errorLabel.setFont(Font.font("System", 12));
        errorLabel.setTextFill(Color.web("#EF4444"));
        errorLabel.setVisible(false);

        // Pulsanti
        HBox actions = new HBox(12);
        Button creaBtn = new Button("Pubblica escursione");
        creaBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(creaBtn, 12, 24);
        creaBtn.setOnAction(e -> tentaCrea());

        Button annullaBtn = new Button("Annulla");
        styleGhostBtn(annullaBtn);
        annullaBtn.setOnAction(e -> onAnnulla.run());
        actions.getChildren().addAll(creaBtn, annullaBtn);

        page.getChildren().addAll(breadcrumb, titoloPag, infoBase,
                tipologieSection, giornateSection, errorLabel, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────

    public void setTipologieDisponibili(List<TipologiaEscursione> lista) {
        this.tipologieDisponibili = lista;
        tipologieBox.getChildren().clear();

        FlowPane flow = new FlowPane(10, 8);

        for (TipologiaEscursione tipologia : lista) {
            CheckBox cb = new CheckBox(tipologia.idTipologia);
            cb.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
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

    // ── Giornate ──────────────────────────────────────────────────

    private void aggiungiGiornata() {
        int numero = giornataForms.size() + 1;
        GiornataForm form = new GiornataForm(numero, () -> {
            giornataForms.remove(form(numero));
            giornateBox.getChildren().removeIf(n -> (numero + "-giornata").equals(n.getId()));
        });
        // workaround per lambda
        GiornataForm gf = new GiornataForm(numero, null);
        giornataForms.add(gf);
        giornateBox.getChildren().add(gf.build(numero));
    }

    private GiornataForm form(int n) {
        return giornataForms.stream().filter(f -> f.numero == n).findFirst().orElse(null);
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
            box.setStyle("-fx-background-color: #0E2A1A; -fx-background-radius: 8;"
                    + "-fx-border-color: #1E4030; -fx-border-radius: 8; -fx-border-width: 0.5;");

            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label("Giorno " + n);
            lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
            lbl.setTextFill(Color.web("#D4673A"));
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            header.getChildren().addAll(lbl, sp);

            dataPicker.setPromptText("Data giornata");
            dataPicker.setMaxWidth(Double.MAX_VALUE);
            dataPicker.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                    + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                    + "-fx-background-radius: 8;");

            programmaField.setPromptText("Descrivi il programma della giornata...");
            programmaField.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                    + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                    + "-fx-background-radius: 8; -fx-text-fill: white;"
                    + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 8 12;");

            box.getChildren().addAll(header, dataPicker, programmaField);
            return box;
        }
    }

    // ── UI helpers ────────────────────────────────────────────────

    private VBox sectionBox(String titolo) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 12; -fx-border-width: 0.5;");
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.WHITE);
        box.getChildren().add(lbl);
        return box;
    }

    private void styleField(TextField f, String prompt) {
        f.setPromptText(prompt);
        f.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-text-fill: white;"
                + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 8 12;");
    }

    private void styleArea(TextArea a, String prompt) {
        a.setPromptText(prompt);
        a.setStyle("-fx-background-color: rgba(255,255,255,0.07);"
                + "-fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 8;"
                + "-fx-background-radius: 8; -fx-text-fill: white;"
                + "-fx-prompt-text-fill: rgba(255,255,255,0.35); -fx-padding: 8 12;");
    }

    private void addLabeledField(GridPane g, String lbl, Control field, int col, int row, int span) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.setFont(Font.font("System", 11));
        l.setTextFill(Color.web(TEXT_MUTED));
        field.setMaxWidth(Double.MAX_VALUE);
        cell.getChildren().addAll(l, field);
        g.add(cell, col, row, span, 1);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private void addLabeledNode(GridPane g, String lbl, javafx.scene.Node node, int col, int row) {
        VBox cell = new VBox(4);
        Label l = new Label(lbl);
        l.setFont(Font.font("System", 11));
        l.setTextFill(Color.web(TEXT_MUTED));
        cell.getChildren().addAll(l, node);
        g.add(cell, col, row);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private void styleAccentBtn(Button btn, int padV, int padH) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8;"
                + "-fx-padding: " + padV + " " + padH + ";";
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
