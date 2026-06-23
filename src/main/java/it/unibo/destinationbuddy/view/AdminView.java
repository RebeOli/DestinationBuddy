package it.unibo.destinationbuddy.view;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.Recensione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * AdminView — pannello amministratore: certificazioni in attesa, guide, premi.
 * Aggiornata al Light Theme.
 *
 * UTILIZZO DAL CONTROLLER:
 * AdminView view = new AdminView();
 * view.setCertificazioniInAttesa(lista);
 * view.setGuide(lista);
 * view.setUtentiDaPremiare(lista);
 * // ⚡ MODIFICATO: Ora accetta e passa due parametri (idCert, nCert)
 * view.setOnValidaCert((idCert, nCert) -> controller.valida(idCert, nCert));
 * view.setOnAttivaGuida(p -> controller.attiva(p));
 * view.setOnDisattivaGuida(p -> controller.disattiva(p));
 * root.setCenter(view.getRoot());
 */
public class AdminView {

    private static final String APP_BG     = "#F4EFE6"; 
    private static final String ACCENT     = "#B85D38"; 
    private static final String TEXT_DARK  = "#2C2A26"; 
    private static final String TEXT_MUTED = "#807B73"; 

    private Consumer<Recensione> onEliminaRecensione = r -> {};
    private BiConsumer<String, String> onValidaCert = (id, n) -> {};
    private Consumer<Persona> onAttivaGuida   = p -> {};
    private Consumer<Persona> onDisattivaGuida = p -> {};
    private Runnable onApriGestioneGeo = () -> {};
    private Runnable onVaiComeUtente = () -> {};

    private final ScrollPane root;
    private final VBox certsContainer      = new VBox(10);
    private final VBox guideContainer      = new VBox(10);
    private final VBox premiContainer      = new VBox(10);
    private final VBox recensioniContainer = new VBox(10); 
    private List<Recensione> listaRecensioniCache = new ArrayList<>();
    private boolean recensioniEspanse = false;

    public AdminView() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(20, 24, 24, 24));
        page.setStyle("-fx-background-color: " + APP_BG + ";");

        Label titolo = new Label("Pannello amministratore");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titolo.setTextFill(Color.web(TEXT_DARK));

        Button btnGeografia = new Button("🌍 Gestione Database Geografico (Paesi, Zone, Luoghi)");
        btnGeografia.getStyleClass().add("btn-accent");
        btnGeografia.setMaxWidth(Double.MAX_VALUE);
        btnGeografia.setOnAction(e -> onApriGestioneGeo.run());

        Button btnUtente = new Button("👤 Vai come utente");
        btnUtente.getStyleClass().add("btn-ghost");
        btnUtente.setOnAction(e -> onVaiComeUtente.run());


        page.getChildren().addAll(
                titolo,
                btnGeografia,
                btnUtente,
                buildSection("📋 Certificazioni in attesa", certsContainer),
                buildSection("🚩 Moderazione Recensioni", recensioniContainer),
                buildSection("👤 Gestione guide", guideContainer),
                buildSection("🏆 Utenti da premiare (tutti i paesi)", premiContainer)
        );

        root = new ScrollPane(page);
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: " + APP_BG + "; -fx-background-color: " + APP_BG + "; -fx-border-color: transparent;");
    }

    public void setCertificazioniInAttesa(List<Certificazione> lista) {
        certsContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            certsContainer.getChildren().add(muted("Nessuna certificazione in attesa."));
            return;
        }
        for (Certificazione c : lista) { certsContainer.getChildren().add(buildCertRow(c)); }
    }

    public void setGuide(List<Persona> lista, List<String> cfSospendibili) {
        guideContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            guideContainer.getChildren().add(muted("Nessuna guida registrata."));
            return;
        }
        for (Persona p : lista) {
            // Controlliamo se la guida è nella "lista nera"
            boolean puoSospendere = cfSospendibili != null && cfSospendibili.contains(p.cf);
            
            guideContainer.getChildren().add(buildGuidaRow(p, puoSospendere));
        }
    }

    private HBox buildGuidaRow(Persona p, boolean puoSospendere) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.getStyleClass().add("cert-row"); 
        
        boolean attiva = p.statoAccount;
        String statoVisivo = attiva ? "Attivo" : "Sospeso";

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label("CF: " + p.cf + "  ·  Stato: " + statoVisivo);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(nomeLbl, sub);

        Button toggleBtn = smallBtn(
                attiva ? "Disattiva" : "Attiva",
                ACCENT,
                "#F9EAE1");
        
        if (attiva && !puoSospendere) {
            toggleBtn.setDisable(true); // Spegne il bottone
            toggleBtn.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #A0A0A0; -fx-background-radius: 6; -fx-padding: 5 12;");
            toggleBtn.setTooltip(new Tooltip("Servono >5 recensioni negative per sospendere questa guida."));
        }

        toggleBtn.setOnAction(e -> {
            if (attiva) onDisattivaGuida.accept(p);
            else onAttivaGuida.accept(p);
        });

        row.getChildren().addAll(info, toggleBtn);
        return row;
    }

    public void setUtentiDaPremiare(List<Persona> lista) {
        premiContainer.getChildren().clear();
        if (lista == null || lista.isEmpty()) {
            premiContainer.getChildren().add(muted("Nessun utente ha completato tutti i paesi."));
            return;
        }
        for (Persona p : lista) { premiContainer.getChildren().add(buildPremioRow(p)); }
    }

    public void setRecensioniDaModerare(List<Recensione> lista) {
        this.listaRecensioniCache = (lista == null) ? new ArrayList<>() : lista;
        aggiornaVistaRecensioni();
    }

    private void aggiornaVistaRecensioni() {
        recensioniContainer.getChildren().clear();
        
        if (listaRecensioniCache.isEmpty()) {
            recensioniContainer.getChildren().add(muted("Nessuna recensione nel sistema."));
            return;
        }

        int limite = recensioniEspanse ? listaRecensioniCache.size() : Math.min(5, listaRecensioniCache.size());

        for (int i = 0; i < limite; i++) {
            recensioniContainer.getChildren().add(buildRecensioneAdminRow(listaRecensioniCache.get(i)));
        }

        if (listaRecensioniCache.size() > 5) {
            String testoBottone = recensioniEspanse 
                ? "▲ Mostra meno" 
                : "▼ Mostra di più (" + (listaRecensioniCache.size() - 5) + " nascoste)";
                
            Button btnEspandi = new Button(testoBottone);
            btnEspandi.setStyle("-fx-background-color: transparent; -fx-text-fill: #B85D38; -fx-cursor: hand; -fx-font-weight: bold;");
            
            btnEspandi.setOnAction(e -> {
                recensioniEspanse = !recensioniEspanse;
                aggiornaVistaRecensioni();
            });
            
            HBox boxBottone = new HBox(btnEspandi);
            boxBottone.setAlignment(Pos.CENTER);
            recensioniContainer.getChildren().add(boxBottone);
        }
    }

    public void setOnValidaCert(BiConsumer<String, String> handler) { this.onValidaCert = handler; }
    public void setOnAttivaGuida(Consumer<Persona> handler) { this.onAttivaGuida = handler; }
    public void setOnDisattivaGuida(Consumer<Persona> handler){ this.onDisattivaGuida = handler; }
    public void setOnEliminaRecensione(Consumer<Recensione> handler) { this.onEliminaRecensione = handler; }
    public void setOnApriGestioneGeo(Runnable handler) { this.onApriGestioneGeo = handler; }
    public void setOnVaiComeUtente(Runnable handler) { this.onVaiComeUtente = handler; }

    public ScrollPane getRoot(){ return root; }
    private VBox buildRecensioneAdminRow(Recensione r) {
        VBox row = new VBox(8);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.getStyleClass().add("cert-row"); 
        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        
        Label nomeLbl = new Label("Voto: " + r.voto + "★ — " + r.titolo);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.web(TEXT_DARK));
        
        String nomeCompleto = (r.nomeAutore + " " + r.cognomeAutore).trim();
        if (nomeCompleto.isEmpty()) nomeCompleto = r.cf;
        
        Label sub = new Label("Di: " + nomeCompleto + " (" + r.cf + ") · ID: " + r.idEscursione);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        
        info.getChildren().addAll(nomeLbl, sub);

        Button eliminaBtn = smallBtn("🗑 Elimina", "#721C24", "#F8D7DA");
        eliminaBtn.setOnAction(e -> onEliminaRecensione.accept(r));

        top.getChildren().addAll(info, eliminaBtn);

        Label desc = new Label(r.descrizione);
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #4A4A4A;"); 

        row.getChildren().addAll(top, desc);

        if (r.immagini != null && !r.immagini.isBlank()) {
            try {
                File fileFoto = new File(r.immagini);
                if (fileFoto.exists()) {
                    Image img = new Image(fileFoto.toURI().toString());
                    ImageView imgView = new ImageView(img);
                    imgView.setFitHeight(70);
                    imgView.setPreserveRatio(true);
                    
                    VBox boxFoto = new VBox(imgView);
                    boxFoto.setStyle("-fx-border-color: #E0DCD3; -fx-border-radius: 4; -fx-border-width: 1; -fx-padding: 2;");
                    boxFoto.setMaxWidth(Region.USE_PREF_SIZE);
                    row.getChildren().add(boxFoto);
                }
            } catch (Exception e) {
            }
        }

        return row;
    }

    private HBox buildCertRow(Certificazione c) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.getStyleClass().add("cert-row");
        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        String tipo = c.tipologia != null ? c.tipologia.idCertificazione + " — " + c.tipologia.livello : "—";
        Label tipoLbl = new Label(tipo);
        tipoLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        tipoLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label("Codice: " + c.nCertificazione + "  ·  Ente: " + c.enteRilasciante + "  ·  CF: " + c.cf);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(tipoLbl, sub);

        Button approvaBtn = smallBtn("✓ Valida", "#155724", "#D4EDDA");
        approvaBtn.setOnAction(e -> {
            
            // ⚡ MODIFICA QUI: Estraiamo l'ID e passiamo ENTRAMBI i valori alla lambda function!
            String idCert = (c.tipologia != null) ? c.tipologia.idCertificazione : "";
            onValidaCert.accept(idCert, c.nCertificazione);
            
            approvaBtn.setText("✓ Validata");
            approvaBtn.setDisable(true);
        });

        row.getChildren().addAll(info, approvaBtn);
        return row;
    }

    private HBox buildPremioRow(Persona p) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setStyle("-fx-background-color: #FFF9E6; -fx-background-radius: 8;"
                + "-fx-border-color: #FDE68A; -fx-border-radius: 8; -fx-border-width: 1;");

        Label icon = new Label("🏅");
        icon.setFont(Font.font(22));

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nomeLbl = new Label(p.nome + " " + p.cognome);
        nomeLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.web(TEXT_DARK));
        Label sub = new Label(p.escursioniEffettuate + " escursioni · CF: " + p.cf);
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(nomeLbl, sub);

        row.getChildren().addAll(icon, info);
        return row;
    }

    private VBox buildSection(String titolo, VBox container) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(16));
        section.getStyleClass().add("card"); 
        Label lbl = new Label(titolo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbl.setTextFill(Color.web(TEXT_DARK));
        section.getChildren().addAll(lbl, container);
        return section;
    }

    private Button smallBtn(String text, String textColor, String bgColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.BOLD, 11));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor
                + "; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12;");
        return btn;
    }

    private Label muted(String testo) {
        Label l = new Label(testo);
        l.setFont(Font.font("System", 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        return l;
    }
}