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
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * CreaEscursioneView — form per le guide per creare una nuova escursione.
 * Light Theme tramite classi CSS.
 */
public class CreaEscursioneView {

    private Consumer<EscursioneFormData> onCrea    = d -> {};
    private Runnable                     onAnnulla = () -> {};

    // Fornitori di dati per i menu a tendina a cascata
    private Supplier<List<String>> providerPaesi = ArrayList::new;
    private Function<String, List<String>> providerZone = p -> new ArrayList<>();
    private BiFunction<String, String, List<String>> providerLuoghi = (p, z) -> new ArrayList<>();

    // Campi form
    private final TextField    titolo         = new TextField();
    private final TextArea     descrizione    = new TextArea();
    private final TextField    difficolta     = new TextField();
    private final TextField    costo          = new TextField();
    private final TextField    maxPartecipanti = new TextField();
    private final DatePicker   dataApertura   = new DatePicker();
    private final DatePicker   dataChiusura   = new DatePicker();
    private final VBox         giornateBox    = new VBox(10);
    private final Label        errorLabel     = new Label();

    private List<TipologiaEscursione> tipologieDisponibili = new ArrayList<>();
    private final VBox          tipologieBox   = new VBox(8);

    private List<TipologiaCertificazione> certificazioniDisponibili = new ArrayList<>();
    private final VBox          certificazioniBox = new VBox(8);
    private final CheckBox      creaNuovaCertCb   = new CheckBox("Crea nuova certificazione");
    private final TextField     nuovaCertIdField  = new TextField();
    private final TextField     nuovaCertLivField = new TextField();
    private final VBox          nuovaCertBox      = new VBox(8);

    private final List<GiornataForm> giornataForms = new ArrayList<>();
    private String guidaCF = "";

    private final ScrollPane root;

    public CreaEscursioneView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));

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

        VBox tipologieSection = sectionBox("🏷 Tipologie escursione");
        tipologieSection.getChildren().add(tipologieBox);

        VBox certificazioniSection = sectionBox("📜 Requisiti (Certificazioni)");
        Label lblCertIstruzioni = new Label("Seleziona le certificazioni richieste per l'escursione:");
        lblCertIstruzioni.getStyleClass().add("text-muted");
        
        styleField(nuovaCertIdField, "Codice (Es. CAI-BASE)");
        styleField(nuovaCertLivField, "Livello (Es. Base)");
        nuovaCertBox.getChildren().addAll(new Label("Nuova certificazione:"), nuovaCertIdField, nuovaCertLivField);
        nuovaCertBox.setVisible(false);
        nuovaCertBox.setManaged(false);

        creaNuovaCertCb.setOnAction(e -> {
            boolean crea = creaNuovaCertCb.isSelected();
            nuovaCertBox.setVisible(crea);
            nuovaCertBox.setManaged(crea);
        });

        certificazioniSection.getChildren().addAll(lblCertIstruzioni, certificazioniBox, creaNuovaCertCb, nuovaCertBox);

        VBox giornateSection = sectionBox("📅 Programma giornaliero e Tappe");
        Button addGiornataBtn = new Button("+ Aggiungi giornata");
        addGiornataBtn.getStyleClass().add("btn-ghost");
        addGiornataBtn.setOnAction(e -> aggiungiGiornata());
        giornateSection.getChildren().addAll(giornateBox, addGiornataBtn);

        aggiungiGiornata();

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        HBox actions = new HBox(12);
        Button creaBtn = new Button("Pubblica escursione");
        creaBtn.getStyleClass().add("btn-accent");
        creaBtn.setOnAction(e -> tentaCrea());

        Button annullaBtn = new Button("Annulla");
        annullaBtn.getStyleClass().add("btn-ghost");
        annullaBtn.setOnAction(e -> onAnnulla.run());
        actions.getChildren().addAll(creaBtn, annullaBtn);

        page.getChildren().addAll(breadcrumb, titoloPag, infoBase,
                tipologieSection, certificazioniSection, giornateSection, errorLabel, actions);

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    public void setProviderPaesi(Supplier<List<String>> provider) { this.providerPaesi = provider; }
    public void setProviderZone(Function<String, List<String>> provider) { this.providerZone = provider; }
    public void setProviderLuoghi(BiFunction<String, String, List<String>> provider) { this.providerLuoghi = provider; }

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

   public void setCertificazioniDisponibili(List<TipologiaCertificazione> lista) {
        this.certificazioniDisponibili = lista;
        certificazioniBox.getChildren().clear();
        FlowPane flow = new FlowPane(10, 8);
        for (TipologiaCertificazione cert : lista) {
            String testoDaMostrare = cert.idCertificazione + " (Livello: " + cert.livello + ")";
            CheckBox cb = new CheckBox(testoDaMostrare);
            cb.setId(cert.idCertificazione);
            cb.getStyleClass().add("sidebar-item-text");
            flow.getChildren().add(cb);
        }
        certificazioniBox.getChildren().add(flow);
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

        nuovaCertIdField.clear(); nuovaCertLivField.clear();
        creaNuovaCertCb.setSelected(false);
        nuovaCertBox.setVisible(false); nuovaCertBox.setManaged(false);
    }

    private void aggiungiGiornata() {
        int numero = giornataForms.size() + 1;
        GiornataForm gf = new GiornataForm(numero);
        giornataForms.add(gf);
        giornateBox.getChildren().add(gf.build(numero));
    }

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

        List<String> tipologieSel = new ArrayList<>();
        if (!tipologieBox.getChildren().isEmpty() && tipologieBox.getChildren().get(0) instanceof FlowPane fp) {
            for (var node : fp.getChildren()) {
                if (node instanceof CheckBox cb && cb.isSelected()) {
                    tipologieSel.add(cb.getText());
                }
            }
        }

        List<String> certSelezionate = new ArrayList<>();
        if (!certificazioniBox.getChildren().isEmpty() && certificazioniBox.getChildren().get(0) instanceof FlowPane fpCert) {
            for (var node : fpCert.getChildren()) {
                if (node instanceof CheckBox cb && cb.isSelected()) {
                    certSelezionate.add(cb.getId());
                }
            }
        }

        TipologiaCertificazione nuovaCertificazione = null;
        if (creaNuovaCertCb.isSelected()) {
            if (nuovaCertIdField.getText().isBlank() || nuovaCertLivField.getText().isBlank()) {
                mostraErrore("Compila tutti i campi della nuova certificazione."); return;
            }
            nuovaCertificazione = new TipologiaCertificazione(nuovaCertIdField.getText().trim(), nuovaCertLivField.getText().trim());
        }

        List<Giornata> giornate = new ArrayList<>();
        String idEsc = "ESC-" + java.util.UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        
        for (GiornataForm gf : giornataForms) {
            LocalDate dataG = gf.dataPicker.getValue();
            String prog     = gf.programmaField.getText();
            
            if (dataG != null && !prog.isBlank()) {
                List<Tappa> tappeGiornata = new ArrayList<>();
                
                for (TappaForm tf : gf.tappaForms) {
                    String p = tf.paeseBox.getValue();
                    String z = tf.zonaBox.getValue();
                    String l = tf.luogoBox.getValue();
                    String dStr = tf.durataField.getText().trim();
                    
                    if (p != null && z != null && l != null && !dStr.isEmpty()) {
                        try {
                            int durataTappa = Integer.parseInt(dStr);
                            String idTappa = "T" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                            tappeGiornata.add(new Tappa(idTappa, durataTappa, idEsc, dataG, p, z, l));
                        } catch (NumberFormatException ignored) {} 
                    }
                }
                giornate.add(new Giornata(idEsc, dataG, prog, tappeGiornata));
            }
        }

        Escursione nuova = new Escursione(
            idEsc, titolo.getText(), difficolta.getText(), costoVal, maxP,
            dataApertura.getValue(), dataChiusura.getValue(),
            new ArrayList<>(), "", "", giornate, new ArrayList<>(), tipologieSel
        );

        onCrea.accept(new EscursioneFormData(nuova, descrizione.getText(), maxP, guidaCF, tipologieSel, certSelezionate, nuovaCertificazione));
    }

    public static class EscursioneFormData {
        public final Escursione escursione;
        public final String     descrizione;
        public final int        numeroPartecipanti;
        public final String     guidaCF;
        public final List<String> tipologie;
        public final List<String> certificazioniSelezionate;
        public final TipologiaCertificazione nuovaCertificazione;

        public EscursioneFormData(Escursione e, String desc, int np, String cf, List<String> tip, 
                                  List<String> certSel, TipologiaCertificazione nuovaCert) {
            this.escursione         = e;
            this.descrizione        = desc;
            this.numeroPartecipanti = np;
            this.guidaCF            = cf;
            this.tipologie          = tip;
            this.certificazioniSelezionate = certSel;
            this.nuovaCertificazione = nuovaCert;
        }
    }

    private class TappaForm {
        final ComboBox<String> paeseBox = new ComboBox<>();
        final ComboBox<String> zonaBox = new ComboBox<>();
        final ComboBox<String> luogoBox = new ComboBox<>();
        final TextField durataField = new TextField();

        VBox build(int n) {
            VBox box = new VBox(8);
            box.setPadding(new Insets(10, 0, 10, 16));
            box.setStyle("-fx-border-color: #B85D38; -fx-border-width: 0 0 0 3;");

            Label lbl = new Label("Tappa " + n);
            lbl.getStyleClass().add("text-muted");
            lbl.setStyle("-fx-font-weight: bold;");

            paeseBox.setPromptText("Scegli Paese...");
            paeseBox.setMaxWidth(Double.MAX_VALUE);
            paeseBox.getItems().setAll(providerPaesi.get());

            zonaBox.setPromptText("Scegli Zona...");
            zonaBox.setMaxWidth(Double.MAX_VALUE);
            zonaBox.setDisable(true);

            luogoBox.setPromptText("Scegli Luogo...");
            luogoBox.setMaxWidth(Double.MAX_VALUE);
            luogoBox.setDisable(true);

            durataField.setPromptText("Durata in ore (Es. 4)");
            durataField.getStyleClass().add("form-field");

            // --- LOGICA A CASCATA ---
            paeseBox.setOnAction(e -> {
                String p = paeseBox.getValue();
                if (p != null) {
                    zonaBox.getItems().setAll(providerZone.apply(p));
                    zonaBox.setDisable(false);
                    luogoBox.getItems().clear();
                    luogoBox.setDisable(true);
                }
            });

            zonaBox.setOnAction(e -> {
                String p = paeseBox.getValue();
                String z = zonaBox.getValue();
                if (p != null && z != null) {
                    luogoBox.getItems().setAll(providerLuoghi.apply(p, z));
                    luogoBox.setDisable(false);
                }
            });

            HBox row1 = new HBox(8, paeseBox, zonaBox);
            HBox row2 = new HBox(8, luogoBox, durataField);
            
            HBox.setHgrow(paeseBox, Priority.ALWAYS);
            HBox.setHgrow(zonaBox, Priority.ALWAYS);
            HBox.setHgrow(luogoBox, Priority.ALWAYS);
            HBox.setHgrow(durataField, Priority.ALWAYS);

            box.getChildren().addAll(lbl, row1, row2);
            return box;
        }
    }

    private class GiornataForm {
        final int        numero;
        final DatePicker dataPicker     = new DatePicker();
        final TextField  programmaField = new TextField();
        final VBox       tappeBox       = new VBox(8);
        final List<TappaForm> tappaForms = new ArrayList<>();

        GiornataForm(int n) { this.numero = n; }

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

            Button addTappaBtn = new Button("+ Aggiungi Tappa a questo giorno");
            addTappaBtn.getStyleClass().add("btn-ghost");
            addTappaBtn.setStyle("-fx-font-size: 11px; -fx-padding: 4 8;");
            addTappaBtn.setOnAction(e -> {
                int numTappa = tappaForms.size() + 1;
                TappaForm tf = new TappaForm();
                tappaForms.add(tf);
                tappeBox.getChildren().add(tf.build(numTappa));
            });

            box.getChildren().addAll(header, dataPicker, programmaField, tappeBox, addTappaBtn);
            return box;
        }
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