package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Equipaggiamento;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * BookingView — prenotazione escursione + noleggio equipaggiamento.
 * Light Theme tramite classi CSS.
 *
 * UTILIZZO DAL CONTROLLER:
 *   BookingView view = new BookingView();
 *   view.setEscursione(escursione, utente, postiRimanenti, scontoNoleggio);
 *   view.setOnConferma((idEscursione, equipSelezionati) -> controller.conferma(...));
 *   view.setOnIndietro(() -> controller.tornaDettaglio());
 *   mainView.setContenuto(view.getRoot());
 */
public class BookingView {

    private BiConsumer<String, Map<String, Boolean>> onConferma = (id, eq) -> {};
    private Runnable onIndietro = () -> {};

    private final ScrollPane root;
    private final VBox       contentBox;

    private Escursione       escursioneCorrente;
    private Persona          utenteCorrente;
    private double           scontoNoleggio = 0.0;
    private final Map<String, CheckBox> equipCheckboxes = new HashMap<>();

    private final Label totalLabel = new Label("€ 0.00");

    public BookingView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    // ── API pubblica ──────────────────────────────────────────────

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
        icon.setStyle("-fx-font-size: 48px;");

        Label titolo = new Label("Prenotazione confermata!");
        titolo.getStyleClass().add("auth-title");
        titolo.setStyle("-fx-font-size: 22px;");

        Label sub = new Label("Sei iscritto a: " + (escursioneCorrente != null ? escursioneCorrente.titolo : ""));
        sub.getStyleClass().add("text-muted");

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
        err.getStyleClass().add("error-label");
        err.setStyle("-fx-background-color: rgba(176,58,46,0.08); -fx-background-radius: 8; -fx-padding: 10 14;");
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
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        Label sep = new Label("›");
        sep.getStyleClass().add("text-muted");
        Label cur = new Label("Prenotazione");
        cur.getStyleClass().add("text-muted");
        breadcrumb.getChildren().addAll(back, sep, cur);

        // Riepilogo escursione
        VBox riepilogo = new VBox(10);
        riepilogo.setPadding(new Insets(16));
        riepilogo.getStyleClass().add("card");
        riepilogo.setStyle("-fx-border-color: -db-accent; -fx-border-width: 1.5;");

        Label escTitolo = new Label(exc.titolo);
        escTitolo.getStyleClass().add("auth-title");
        escTitolo.setStyle("-fx-font-size: 18px;");

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

        // Dati partecipante
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
            scontoLbl.getStyleClass().addAll("badge", "badge-green");
            equipSection.getChildren().add(scontoLbl);
        }

        if (exc.equipaggiamento == null || exc.equipaggiamento.isEmpty()) {
            Label noEquip = new Label("Nessun equipaggiamento richiesto per questa escursione.");
            noEquip.getStyleClass().add("text-muted");
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
        totaleBox.getStyleClass().add("card");
        totaleBox.setStyle("-fx-border-color: -db-accent; -fx-border-width: 1;");

        HBox costoEscRow = totaleRow("Costo iscrizione", String.format("€ %.2f", exc.costo));
        HBox equipRow    = totaleRow("Noleggio selezionato", "€ 0.00");
        equipRow.setId("equip-totale-row");

        totalLabel.getStyleClass().add("text-accent");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        totalLabel.setText(String.format("€ %.2f", exc.costo));

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totaleTxt = new Label("Totale");
        totaleTxt.getStyleClass().add("auth-title");
        totaleTxt.setStyle("-fx-font-size: 15px;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        totalRow.setStyle("-fx-border-color: -db-border; -fx-border-width: 1 0 0 0;");
        totalRow.setPadding(new Insets(8, 0, 0, 0));
        totalRow.getChildren().addAll(totaleTxt, sp, totalLabel);

        totaleBox.getChildren().addAll(costoEscRow, equipRow, totalRow);

        // Pulsanti
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button confermaBtn = new Button("Conferma prenotazione");
        confermaBtn.getStyleClass().add("btn-accent");
        confermaBtn.setOnAction(e -> {
            Map<String, Boolean> selezioni = new HashMap<>();
            equipCheckboxes.forEach((id, cb) -> selezioni.put(id, cb.isSelected()));
            onConferma.accept(exc.idEscursione, selezioni);
        });

        Button indietroBtn = new Button("← Annulla");
        indietroBtn.getStyleClass().add("btn-ghost");
        indietroBtn.setOnAction(e -> onIndietro.run());

        actions.getChildren().addAll(confermaBtn, indietroBtn);

        contentBox.getChildren().addAll(
                breadcrumb, riepilogo, partecipante, equipSection, totaleBox, actions);
    }

    private HBox buildEquipRow(Equipaggiamento eq, int durata) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setStyle("-fx-border-color: -db-border; -fx-border-width: 0 0 1 0;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label catLbl = new Label(eq.idCategoria);
        catLbl.getStyleClass().add("cert-title");

        double costoBase   = eq.costoTotaleGiornaliero * durata;
        double costoScontato = costoBase * (1 - scontoNoleggio);
        Label costoLbl = new Label(String.format("€ %.2f / giorno × %d giorni", eq.costoTotaleGiornaliero, durata));
        costoLbl.getStyleClass().add("text-muted");
        info.getChildren().addAll(catLbl, costoLbl);

        VBox prezziBox = new VBox(2);
        prezziBox.setAlignment(Pos.CENTER_RIGHT);
        Label prezzoFinal = new Label(String.format("€ %.2f", costoScontato));
        prezzoFinal.getStyleClass().add("text-price");
        if (scontoNoleggio > 0) {
            Label prezzoBase = new Label(String.format("€ %.2f", costoBase));
            prezzoBase.getStyleClass().add("text-muted");
            prezzoBase.setStyle("-fx-strikethrough: true;");
            prezziBox.getChildren().addAll(prezzoFinal, prezzoBase);
        } else {
            prezziBox.getChildren().add(prezzoFinal);
        }

        CheckBox cb = new CheckBox();
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
        box.getStyleClass().add("card");
        Label lbl = new Label(titolo);
        lbl.getStyleClass().add("card-title");
        box.getChildren().add(lbl);
        return box;
    }

    private void addInfoCell(GridPane grid, String label, String value, int col, int row) {
        VBox cell = new VBox(3);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("text-muted");
        Label val = new Label(value);
        val.getStyleClass().add("cert-title");
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, row);
    }

    private void addFormField(GridPane grid, String label, String value, int col) {
        VBox cell = new VBox(4);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("text-muted");
        Label val = new Label(value.isEmpty() ? "—" : value);
        val.getStyleClass().add("sidebar-item-text");
        val.setStyle("-fx-background-color: rgba(0,0,0,0.03); -fx-background-radius: 6; -fx-padding: 8 12;");
        val.setMaxWidth(Double.MAX_VALUE);
        cell.getChildren().addAll(lbl, val);
        grid.add(cell, col, 0);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private HBox totaleRow(String label, String valore) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("text-muted");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label val = new Label(valore);
        val.getStyleClass().add("sidebar-item-text");
        row.getChildren().addAll(lbl, sp, val);
        return row;
    }
}
