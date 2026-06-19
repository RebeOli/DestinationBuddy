package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Equipaggiamento;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * BookingView — prenotazione escursione + noleggio equipaggiamento.
 *
 * UTILIZZO DAL CONTROLLER:
 *   BookingView view = new BookingView();
 *   view.setEscursione(escursione, utente, postiRimanenti, scontoNoleggio);
 *   view.setOnConferma((idEscursione, equipSelezionati) -> controller.conferma(...));
 *   view.setOnIndietro(() -> controller.tornaDettaglio());
 *   mainView.setContenuto(view.getRoot());
 */
public class BookingView {

    private static final String DARK_BG      = "#0E2A1A";
    private static final String CARD_BG      = "#152E1C";
    private static final String ACCENT       = "#D4673A";
    private static final String ACCENT_HOVER = "#B85530";
    private static final String TEXT_MUTED   = "#A0B8AA";

    // Callbacks
    private BiConsumer<String, Map<String, Boolean>> onConferma = (id, eq) -> {};
    private Runnable onIndietro = () -> {};

    private final ScrollPane root;
    private final VBox       contentBox;

    // Stato interno
    private Escursione       escursioneCorrente;
    private Persona          utenteCorrente;
    private double           scontoNoleggio = 0.0;
    private final Map<String, CheckBox> equipCheckboxes = new HashMap<>();

    // Label totale dinamico
    private final Label totalLabel = new Label("€ 0.00");

    public BookingView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));
        contentBox.setStyle("-fx-background-color: " + DARK_BG + ";");

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG
                + "; -fx-border-color: transparent;");
    }

    // ── API pubblica ──────────────────────────────────────────────

    /**
     * Popola la view con i dati dell'escursione e dell'utente.
     * @param escursione      escursione da prenotare
     * @param utente          utente loggato
     * @param postiRimanenti  posti ancora disponibili
     * @param scontoNoleggio  percentuale sconto (es. 0.20 = 20%)
     */
    public void setEscursione(Escursione escursione, Persona utente,
                               int postiRimanenti, double scontoNoleggio) {
        this.escursioneCorrente = escursione;
        this.utenteCorrente     = utente;
        this.scontoNoleggio     = scontoNoleggio;
        equipCheckboxes.clear();
        contentBox.getChildren().clear();
        buildUI(postiRimanenti);
    }

    public void setOnConferma(BiConsumer<String, Map<String, Boolean>> handler) {
        this.onConferma = handler;
    }
    public void setOnIndietro(Runnable handler) { this.onIndietro = handler; }
    public ScrollPane getRoot()                 { return root; }

    /** Mostra un messaggio di successo dopo la conferma. */
    public void mostraConferma() {
        contentBox.getChildren().clear();
        VBox success = new VBox(16);
        success.setAlignment(Pos.CENTER);
        success.setPadding(new Insets(60, 0, 0, 0));

        Label icon = new Label("✅");
        icon.setFont(Font.font(48));

        Label titolo = new Label("Prenotazione confermata!");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 22));
        titolo.setTextFill(Color.WHITE);

        Label sub = new Label("Sei iscritto a: " + (escursioneCorrente != null ? escursioneCorrente.titolo : ""));
        sub.setFont(Font.font("System", 14));
        sub.setTextFill(Color.web(TEXT_MUTED));

        Button tornaBtn = new Button("Torna all'esplora");
        tornaBtn.getStyleClass().add("btn-accent");
        tornaBtn.setOnAction(e -> onIndietro.run());

        success.getChildren().addAll(icon, titolo, sub, tornaBtn);
        contentBox.getChildren().add(success);
    }

    /** Mostra un errore (certificazioni mancanti, posti esauriti, ecc.). */
    public void mostraErrore(String messaggio) {
        contentBox.getChildren().removeIf(n -> "error-box".equals(n.getId()));
        Label err = new Label("⚠ " + messaggio);
        err.setId("error-box");
        err.setFont(Font.font("System", 13));
        err.setTextFill(Color.web("#EF4444"));
        err.setStyle("-fx-background-color: rgba(239,68,68,0.1); -fx-background-radius: 8; -fx-padding: 10 14;");
        err.setMaxWidth(Double.MAX_VALUE);
        contentBox.getChildren().add(0, err);
    }

    // ── Costruzione UI ────────────────────────────────────────────

    private void buildUI(int postiRimanenti) {
        Escursione exc = escursioneCorrente;

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Dettaglio");
        back.setFont(Font.font("System", 12));
        back.setTextFill(Color.web(ACCENT));
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        Label sep = new Label("›");
        sep.setTextFill(Color.web(TEXT_MUTED));
        Label cur = new Label("Prenotazione");
        cur.setFont(Font.font("System", 12));
        cur.setTextFill(Color.web(TEXT_MUTED));
        breadcrumb.getChildren().addAll(back, sep, cur);

        // Riepilogo escursione
        VBox riepilogo = new VBox(10);
        riepilogo.setPadding(new Insets(16));
        riepilogo.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + "-fx-border-color: rgba(212,103,58,0.3); -fx-border-radius: 12; -fx-border-width: 0.5;");

        Label escTitolo = new Label(exc.titolo);
        escTitolo.setFont(Font.font("System", FontWeight.BOLD, 18));
        escTitolo.setTextFill(Color.WHITE);

        GridPane info = new GridPane();
        info.setHgap(24);
        info.setVgap(8);
        addInfoCell(info, "Guida",       exc.guidaNome + " " + exc.guidaCognome, 0, 0);
        addInfoCell(info, "Difficoltà",  exc.difficolta, 1, 0);
        addInfoCell(info, "Costo",       String.format("€ %.2f", exc.costo), 2, 0);
        addInfoCell(info, "Posti rimasti", String.valueOf(postiRimanenti), 0, 1);
        addInfoCell(info, "Apertura",
                exc.dataAperturaEscursione != null ? exc.dataAperturaEscursione.toString() : "—", 1, 1);
        addInfoCell(info, "Chiusura",
                exc.dataChiusuraEscursione != null ? exc.dataChiusuraEscursione.toString() : "—", 2, 1);

        riepilogo.getChildren().addAll(escTitolo, info);

        // Dati partecipante (sola lettura, presi dall'utente loggato)
        VBox partecipante = sectionBox("👤 Partecipante");
        GridPane partGrid = new GridPane();
        partGrid.setHgap(16);
        partGrid.setVgap(10);
        addFormField(partGrid, "Nome",   utenteCorrente != null ? utenteCorrente.nome    : "", 0);
        addFormField(partGrid, "Cognome",utenteCorrente != null ? utenteCorrente.cognome : "", 1);
        addFormField(partGrid, "Email",  utenteCorrente != null ? utenteCorrente.email   : "", 2);
        partecipante.getChildren().add(partGrid);

        // Equipaggiamento noleggio
        VBox equipSection = sectionBox("🎒 Noleggio equipaggiamento");
        if (scontoNoleggio > 0) {
            Label scontoLbl = new Label(String.format("✓ Sconto Premium %.0f%% applicato", scontoNoleggio * 100));
            scontoLbl.setFont(Font.font("System", 12));
            scontoLbl.setTextFill(Color.web("#4AC582"));
            scontoLbl.setStyle("-fx-background-color: rgba(74,197,130,0.1);"
                    + "-fx-background-radius: 6; -fx-padding: 4 10;");
            equipSection.getChildren().add(scontoLbl);
        }

        if (exc.equipaggiamento == null || exc.equipaggiamento.isEmpty()) {
            Label noEquip = new Label("Nessun equipaggiamento richiesto per questa escursione.");
            noEquip.setFont(Font.font("System", 13));
            noEquip.setTextFill(Color.web(TEXT_MUTED));
            equipSection.getChildren().add(noEquip);
        } else {
            int durata = exc.giornate != null ? exc.giornate.size() : 1;
            for (Equipaggiamento eq : exc.equipaggiamento) {
                equipSection.getChildren().add(buildEquipRow(eq, durata));
            }
        }

        // Totale
        VBox totaleBox = new VBox(8);
        totaleBox.setPadding(new Insets(14));
        totaleBox.setStyle("-fx-background-color: rgba(212,103,58,0.08);"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: rgba(212,103,58,0.25);"
                + "-fx-border-radius: 10; -fx-border-width: 0.5;");

        HBox costoEscRow = totaleRow("Costo iscrizione", String.format("€ %.2f", exc.costo));
        HBox equipRow    = totaleRow("Noleggio selezionato", "€ 0.00");
        equipRow.setId("equip-totale-row");

        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        totalLabel.setTextFill(Color.web(ACCENT));
        totalLabel.setText(String.format("€ %.2f", exc.costo));

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totaleTxt = new Label("Totale");
        totaleTxt.setFont(Font.font("System", FontWeight.BOLD, 15));
        totaleTxt.setTextFill(Color.WHITE);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        totalRow.setStyle("-fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 1 0 0 0;");
        totalRow.setPadding(new Insets(8, 0, 0, 0));
        totalRow.getChildren().addAll(totaleTxt, sp, totalLabel);

        totaleBox.getChildren().addAll(costoEscRow, equipRow, totalRow);

        // Pulsanti
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button confermaBtn = new Button("Conferma prenotazione");
        confermaBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        styleAccentBtn(confermaBtn, 12, 24);
        confermaBtn.setOnAction(e -> {
            Map<String, Boolean> selezioni = new HashMap<>();
            equipCheckboxes.forEach((id, cb) -> selezioni.put(id, cb.isSelected()));
            onConferma.accept(exc.idEscursione, selezioni);
        });

        Button indietroBtn = new Button("← Annulla");
        styleGhostBtn(indietroBtn);
        indietroBtn.setOnAction(e -> onIndietro.run());

        actions.getChildren().addAll(confermaBtn, indietroBtn);

        contentBox.getChildren().addAll(
                breadcrumb, riepilogo, partecipante, equipSection, totaleBox, actions);
    }

    private HBox buildEquipRow(Equipaggiamento eq, int durata) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setStyle("-fx-border-color: rgba(255,255,255,0.07);"
                + "-fx-border-width: 0 0 0.5 0;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label catLbl = new Label(eq.idCategoria);
        catLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        catLbl.setTextFill(Color.WHITE);

        double costoBase  = eq.costoTotaleGiornaliero * durata;
        double costoSconт = costoBase * (1 - scontoNoleggio);
        Label costoLbl = new Label(String.format("€ %.2f / giorno × %d giorni", eq.costoTotaleGiornaliero, durata));
        costoLbl.setFont(Font.font("System", 11));
        costoLbl.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(catLbl, costoLbl);

        VBox prezziBox = new VBox(2);
        prezziBox.setAlignment(Pos.CENTER_RIGHT);
        Label prezzoFinal = new Label(String.format("€ %.2f", costoSconт));
        prezzoFinal.setFont(Font.font("System", FontWeight.BOLD, 14));
        prezzoFinal.setTextFill(Color.web(ACCENT));
        if (scontoNoleggio > 0) {
            Label prezzoBase = new Label(String.format("€ %.2f", costoBase));
            prezzoBase.setFont(Font.font("System", 11));
            prezzoBase.setTextFill(Color.web(TEXT_MUTED));
            prezzoBase.setStyle("-fx-strikethrough: true;");
            prezziBox.getChildren().addAll(prezzoFinal, prezzoBase);
        } else {
            prezziBox.getChildren().add(prezzoFinal);
        }

        CheckBox cb = new CheckBox();
        cb.setStyle("-fx-text-fill: white;");
        cb.selectedProperty().addListener((obs, old, selected) -> aggiornaTotale());
        equipCheckboxes.put(eq.idCategoria, cb);

        row.getChildren().addAll(info, prezziBox, cb);
        return row;
    }

    private void aggiornaTotale() {
        if (escursioneCorrente == null) return;
        double tot = escursioneCorrente.costo;
        int durata = escursioneCorrente.giornate != null ? escursioneCorrente.giornate.size() : 1;
        if (escursioneCorrente.equipaggiamento != null) {
            for (Equipaggiamento eq : escursioneCorrente.equipaggiamento) {
                CheckBox cb = equipCheckboxes.get(eq.idCategoria);
                if (cb != null && cb.isSelected()) {
                    tot += eq.costoTotaleGiornaliero * durata * (1 - scontoNoleggio);
                }
            }
        }
        totalLabel.setText(String.format("€ %.2f", tot));
    }

    // ── Helpers ───────────────────────────────────────────────────

    private VBox sectionBox(String titolo) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + "-fx-border-color: #1E4030; -fx-border-radius: 12; -fx-border-width: 0.5;");
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.WHITE);
        box.getChildren().add(lbl);
        return box;
    }

    private void addInfoCell(GridPane grid, String label, String value, int col, int row) {
        VBox cell = new VBox(3);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        Label val = new Label(value);
        val.setFont(Font.font("System", FontWeight.BOLD, 13));
        val.setTextFill(Color.WHITE);
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, row);
    }

    private void addFormField(GridPane grid, String label, String value, int col) {
        VBox cell = new VBox(4);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        Label val = new Label(value.isEmpty() ? "—" : value);
        val.setFont(Font.font("System", 13));
        val.setTextFill(Color.WHITE);
        val.setStyle("-fx-background-color: rgba(255,255,255,0.05);"
                + "-fx-background-radius: 6; -fx-padding: 8 12;");
        val.setMaxWidth(Double.MAX_VALUE);
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, 0);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private HBox totaleRow(String label, String valore) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 13));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label val = new Label(valore);
        val.setFont(Font.font("System", 13));
        val.setTextFill(Color.WHITE);
        row.getChildren().addAll(lbl, sp, val);
        return row;
    }

    private void styleAccentBtn(Button btn, int padV, int padH) {
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                + "-fx-cursor: hand; -fx-background-radius: 8;"
                + "-fx-padding: " + padV + " " + padH + ";";
        String hover = base.replace(ACCENT, ACCENT_HOVER);
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
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
