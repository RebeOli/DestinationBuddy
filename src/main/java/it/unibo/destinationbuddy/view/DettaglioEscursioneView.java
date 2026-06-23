package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Equipaggiamento;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.Giornata;
import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * DettaglioEscursioneView — mostra tutti i dati di un'Escursione completa.
 * Light Theme tramite classi CSS.
 */
public class DettaglioEscursioneView {

    private Consumer<Escursione> onPrenota  = e -> {};
    private Runnable             onIndietro = () -> {};
    private Consumer<Recensione> onInviaRecensione = r -> {};

    private final ScrollPane root;
    private final VBox        contentBox;
    private final VBox        recensioniBox;

    public DettaglioEscursioneView() {
        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20, 24, 24, 24));

        Label placeholder = new Label("Seleziona un'escursione dalla lista.");
        placeholder.getStyleClass().add("text-muted");
        contentBox.getChildren().add(placeholder);

        recensioniBox = buildSection("⭐ Recensioni");

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("scroll-pane");
    }

    /** Riempie la view con tutti i dati dell'escursione. */
    public void setEscursione(Escursione exc) {
        contentBox.getChildren().clear();

        // Breadcrumb
        HBox breadcrumb = new HBox(6);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label back = new Label("← Esplora");
        back.getStyleClass().add("switch-link");
        back.setCursor(javafx.scene.Cursor.HAND);
        back.setOnMouseClicked(e -> onIndietro.run());
        Label sep = new Label("›");
        sep.getStyleClass().add("text-muted");
        Label current = new Label(exc.titolo);
        current.getStyleClass().add("text-muted");
        breadcrumb.getChildren().addAll(back, sep, current);

        // Header
        VBox header = new VBox(8);
        Label titoloLbl = new Label(exc.titolo);
        titoloLbl.getStyleClass().add("auth-title");
        titoloLbl.setStyle("-fx-font-size: 26px;");
        titoloLbl.setWrapText(true);

        // Luogo dell'escursione: Unisce tutti i luoghi unici separati da " • "
        String posizione = trovaPosizione(exc);
        if (posizione != null) {
            Label posizioneLbl = new Label("📍 " + posizione);
            posizioneLbl.getStyleClass().add("text-muted");
            posizioneLbl.setWrapText(true);
            header.getChildren().add(posizioneLbl);
        }

        HBox badges = new HBox(8);
        badges.getChildren().add(pill(exc.difficolta, "badge-accent"));
        for (String tip : exc.tipologie) {
            badges.getChildren().add(pill(tip, "badge-blue"));
        }
        header.getChildren().addAll(titoloLbl, badges);

        // Info grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(14));
        infoGrid.getStyleClass().add("card");

        addInfoCell(infoGrid, "Guida", exc.guidaNome + " " + exc.guidaCognome, 0, 0);
        addInfoCell(infoGrid, "Posti disponibili", String.valueOf(exc.postiDisponibili), 1, 0);
        addInfoCell(infoGrid, "Costo iscrizione", String.format("€ %.2f", exc.costo), 2, 0);
        addInfoCell(infoGrid, "Apertura iscrizioni",
                exc.dataAperturaEscursione != null ? exc.dataAperturaEscursione.toString() : "—", 0, 1);
        addInfoCell(infoGrid, "Chiusura iscrizioni",
                exc.dataChiusuraEscursione != null ? exc.dataChiusuraEscursione.toString() : "—", 1, 1);
        addInfoCell(infoGrid, "Difficoltà", exc.difficolta, 2, 1);

        // Certificazioni richieste
        VBox certBox = buildSection("🏅 Certificazioni richieste");
        if (exc.certificazioniRichieste == null || exc.certificazioniRichieste.isEmpty()) {
            certBox.getChildren().add(muted("Nessuna certificazione richiesta."));
        } else {
            FlowPane certBadges = new FlowPane(8, 6);
            certBadges.setPrefWrapLength(700);
            for (TipologiaCertificazione c : exc.certificazioniRichieste) {
                certBadges.getChildren().add(pill(c.idCertificazione, "badge-purple"));
            }
            certBox.getChildren().add(certBadges);
        }

        // Programma giornate
        VBox giornateBox = buildSection("📅 Programma");
        if (exc.giornate == null || exc.giornate.isEmpty()) {
            giornateBox.getChildren().add(muted("Nessuna giornata definita."));
        } else {
            for (Giornata g : exc.giornate) {
                giornateBox.getChildren().add(buildGiornataRow(g));
            }
        }

        // Equipaggiamento minimo
        VBox equipBox = buildSection("🎒 Equipaggiamento minimo");
        if (exc.equipaggiamento == null || exc.equipaggiamento.isEmpty()) {
            equipBox.getChildren().add(muted("Nessun equipaggiamento specificato."));
        } else {
            FlowPane equipBadges = new FlowPane(8, 6);
            equipBadges.setPrefWrapLength(700);
            for (Equipaggiamento eq : exc.equipaggiamento) {
                equipBadges.getChildren().add(pill(eq.toString(), "badge-green"));
            }
            equipBox.getChildren().add(equipBadges);
        }

        // Pulsanti azione
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_LEFT);
        Button prenotaBtn = new Button("Prenota questa escursione");
        prenotaBtn.getStyleClass().add("btn-accent");
        prenotaBtn.setOnAction(e -> onPrenota.accept(exc));

        Button indietroBtn = new Button("← Torna all'elenco");
        indietroBtn.getStyleClass().add("btn-ghost");
        indietroBtn.setOnAction(e -> onIndietro.run());

        actions.getChildren().addAll(prenotaBtn, indietroBtn);

        contentBox.getChildren().addAll(breadcrumb, header, infoGrid,
            certBox, giornateBox, equipBox, recensioniBox, actions);
    }

    public void setPostiRimanenti(int posti) {
        // Per semplicità basta richiamare setEscursione con l'escursione aggiornata
    }

    public void setRecensioni(List<Recensione> lista, boolean puoRecensire, String idEscursione, String cfUtente) {
        recensioniBox.getChildren().clear();
        Label lbl = new Label("⭐ Recensioni");
        lbl.getStyleClass().add("card-title");
        recensioniBox.getChildren().add(lbl);

        if (lista == null || lista.isEmpty()) {
            recensioniBox.getChildren().add(muted("Nessuna recensione ancora presente."));
        } else {
            for (Recensione r : lista) {
                recensioniBox.getChildren().add(buildRecensioneRow(r));
            }
        }

        if (puoRecensire) {
            Button scriviBtn = new Button("Scrivi una recensione");
            scriviBtn.getStyleClass().add("btn-accent");
            scriviBtn.setStyle("-fx-background-color: #854D0E; -fx-text-fill: white;"); 
            scriviBtn.setOnAction(e -> mostraPopupRecensione(idEscursione, cfUtente));
            recensioniBox.getChildren().add(scriviBtn);
        }
    }

    public void setOnPrenota(Consumer<Escursione> handler)  { this.onPrenota  = handler; }
    public void setOnIndietro(Runnable handler)              { this.onIndietro = handler; }
    public void setOnInviaRecensione(Consumer<Recensione> h) { this.onInviaRecensione = h; }
    public ScrollPane getRoot()                              { return root; }

    /** Raccoglie tutti i luoghi unici dell'escursione e li unisce con un puntino. */
    private String trovaPosizione(Escursione exc) {
        if (exc.giornate == null) return null;
        
        List<String> itinerario = new ArrayList<>();
        
        for (var g : exc.giornate) {
            if (g.tappe != null) {
                for (var t : g.tappe) {
                    String stringaLuogo = t.nomeLuogo + ", " + t.nomeZona + " (" + t.nomePaese + ")";
                    if (!itinerario.contains(stringaLuogo)) {
                        itinerario.add(stringaLuogo);
                    }
                }
            }
        }
        
        if (itinerario.isEmpty()) {
            return null;
        }
        
        return String.join(" • ", itinerario);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private VBox buildSection(String titolo) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(14));
        box.getStyleClass().add("card");
        Label lbl = new Label(titolo);
        lbl.getStyleClass().add("card-title");
        box.getChildren().add(lbl);
        return box;
    }

    /** Costruisce la riga del programma: Giorno + Descrizione + Lista Tappe */
    private VBox buildGiornataRow(Giornata g) {
        VBox row = new VBox(6);
        row.setPadding(new Insets(6, 0, 12, 0));
        row.setStyle("-fx-border-color: transparent transparent #E0DCD3 transparent; -fx-border-width: 0 0 1 0;");
        
        // Riga superiore: numero giorno e descrizione
        HBox header = new HBox(12);
        Label num = new Label("Giorno " + g.data);
        num.getStyleClass().add("text-accent");
        num.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-min-width: 80px;");
        
        Label desc = new Label(g.programma != null ? g.programma : "");
        desc.getStyleClass().add("sidebar-item-text");
        desc.setWrapText(true);
        header.getChildren().addAll(num, desc);
        
        row.getChildren().add(header);

        // Elenco delle tappe (indentato per grafica)
        if (g.tappe != null && !g.tappe.isEmpty()) {
            VBox tappeBox = new VBox(4);
            tappeBox.setPadding(new Insets(4, 0, 0, 92)); // Indentazione per allinearlo alla descrizione

            for (int i = 0; i < g.tappe.size(); i++) {
                var t = g.tappe.get(i);
                Label tappaLbl = new Label("Tappa " + (i + 1) + ": " + t.nomeLuogo + " (" + t.durata + "h)");
                tappaLbl.setStyle("-fx-text-fill: #807B73; -fx-font-size: 12px;");
                tappeBox.getChildren().add(tappaLbl);
            }
            row.getChildren().add(tappeBox);
        }

        return row;
    }

    private VBox buildRecensioneRow(Recensione r) {
        VBox row = new VBox(8);
        row.setPadding(new Insets(12, 0, 12, 0));
        row.setStyle("-fx-border-color: transparent transparent #E0DCD3 transparent; -fx-border-width: 0 0 1 0;");

        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label(r.titolo);
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C2A26;");
        String testoStelle = "★".repeat(r.voto) + "☆".repeat(5 - r.voto);
        Label stelle = new Label(testoStelle);
        stelle.setStyle("-fx-text-fill: #EAB308; -fx-font-size: 14px;"); 
        top.getChildren().addAll(title, stelle);

        Label desc = new Label(r.descrizione);
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #807B73;");
        row.getChildren().addAll(top, desc);
        if (r.immagini != null && !r.immagini.isBlank()) {
            try {
                File fileFoto = new File(r.immagini);
                if (fileFoto.exists()) {
                    Image img = new Image(fileFoto.toURI().toString());
                    ImageView imgView = new ImageView(img);
                    imgView.setFitHeight(120);
                    imgView.setPreserveRatio(true);
                    VBox boxFoto = new VBox(imgView);
                    boxFoto.setStyle("-fx-border-color: #E0DCD3; -fx-border-radius: 4; -fx-border-width: 1; -fx-padding: 2;");
                    boxFoto.setMaxWidth(Region.USE_PREF_SIZE);
                    row.getChildren().add(boxFoto);
                }
            } catch (Exception e) {
                System.err.println("Immagine non trovata al percorso: " + r.immagini);
            }
        }

        String nomeCompleto = r.nomeAutore + " " + r.cognomeAutore;
        if (nomeCompleto.trim().isEmpty()) {
            nomeCompleto = r.cf;
        }
        Label autore = new Label("Scritta da: " + nomeCompleto);
        autore.setStyle("-fx-text-fill: #A09C96; -fx-font-size: 11px; -fx-font-style: italic;");
        row.getChildren().add(autore);
        return row;
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

    private Label pill(String testo, String badgeColorClass) {
        Label l = new Label(testo);
        l.getStyleClass().addAll("badge", badgeColorClass);
        return l;
    }

    private Label muted(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().add("text-muted");
        return l;
    }

    private void mostraPopupRecensione(String idEscursione, String cfUtente) {
        Dialog<Recensione> dialog = new Dialog<>();
        dialog.setTitle("Scrivi una recensione");
        dialog.setHeaderText("Racconta la tua esperienza!");

        ButtonType inviaButtonType = new ButtonType("Invia Recensione", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(inviaButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField titolo = new TextField();
        titolo.setPromptText("Titolo");
        
        ComboBox<Integer> voto = new ComboBox<>();
        voto.getItems().addAll(1, 2, 3, 4, 5);
        voto.setValue(5);
        
        TextArea descrizione = new TextArea();
        descrizione.setPromptText("Come ti sei trovato?");
        descrizione.setPrefRowCount(3);
        Button btnAllega = new Button("📸 Allega Foto");
        Label lblPercorsoFoto = new Label("Nessuna foto selezionata");
        lblPercorsoFoto.setStyle("-fx-text-fill: #807B73; -fx-font-size: 11px;");
        final String[] percorsoImmagine = {""}; 

        btnAllega.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Scegli una foto");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
            File fileScelto = fileChooser.showOpenDialog(dialog.getOwner());
            if (fileScelto != null) {
                percorsoImmagine[0] = fileScelto.getAbsolutePath();
                lblPercorsoFoto.setText(fileScelto.getName());
            }
        });
        grid.add(new Label("Titolo:"), 0, 0);
        grid.add(titolo, 1, 0);
        grid.add(new Label("Voto:"), 0, 1);
        grid.add(voto, 1, 1);
        grid.add(new Label("Foto:"), 0, 2);
        HBox boxFoto = new HBox(10, btnAllega, lblPercorsoFoto);
        boxFoto.setAlignment(Pos.CENTER_LEFT);
        grid.add(boxFoto, 1, 2);
        grid.add(new Label("Descrizione:"), 0, 3);
        grid.add(descrizione, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == inviaButtonType) {
                return new Recensione(titolo.getText(), cfUtente, voto.getValue(), percorsoImmagine[0], descrizione.getText(), "In attesa", idEscursione);
            }
            return null;
        });
        dialog.showAndWait().ifPresent(r -> onInviaRecensione.accept(r));
    }
}