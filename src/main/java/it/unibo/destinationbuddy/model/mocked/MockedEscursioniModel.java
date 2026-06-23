package it.unibo.destinationbuddy.model.mocked;

import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.LuogoEsplorabile;
import it.unibo.destinationbuddy.data.TipologiaEscursione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione; 
import it.unibo.destinationbuddy.model.EscursioniModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockedEscursioniModel implements EscursioniModel {

    private final List<EscursionePreview> fintePreview = List.of(
        new EscursionePreview("ESC-001", "Ciaspolata sul Monte Bianco", "Difficile", 45.00),
        new EscursionePreview("ESC-002", "Passeggiata nel bosco", "Facile", 15.00),
        new EscursionePreview("ESC-003", "Ferrata degli Angeli", "Esperto", 80.00)
    );

    @Override
    public List<EscursionePreview> getTop5() {
        return fintePreview;
    }

    @Override
    public List<EscursionePreview> getEscursioniPerMese(int mese) {
        return fintePreview;
    }

    @Override
    public List<EscursionePreview> getAll() {
        return fintePreview;
    }

    @Override
    public Optional<Escursione> getDettaglio(EscursionePreview preview) {
        Escursione fintaCompleta = new Escursione(
            preview.idEscursione,
            preview.titolo,
            preview.difficolta,
            preview.costo,
            10,
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            new ArrayList<>(),
            "GuidaMario",
            "GuidaRossi",
            new ArrayList<>(),
            new ArrayList<>(),
            List.of("Neve", "Montagna")
        );
        
        return Optional.of(fintaCompleta);
    }

    @Override
    public List<EscursionePreview> getEscursionePerTipologia(TipologiaEscursione tipologia) {
        return fintePreview;
    }

    @Override
    public void creaEscursione(Escursione e, String descrizione, int numeroPartecipanti, String guidaCF, 
                               List<String> tipologie, List<String> certificazioniSelezionate, 
                               TipologiaCertificazione nuovaCertificazione) {
        System.out.println("[MOCK] Creata nuova escursione: " + e.titolo + " (Guida CF: " + guidaCF + ")");
        System.out.println("[MOCK] Certificazioni richieste selezionate: " + certificazioniSelezionate.size());
        if (nuovaCertificazione != null) {
            System.out.println("[MOCK] È stata inventata e creata anche la nuova certificazione: " + nuovaCertificazione.idCertificazione);
        }
    }

    @Override
    public List<TipologiaEscursione> getTipologie() {
        List<TipologiaEscursione> lista = new ArrayList<>();

        lista.add(new TipologiaEscursione("Trekking", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Snorkeling", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Hiking", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Alpinismo", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Trekking notturno", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Speleologia", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Mountain bike", new ArrayList<>()));
        lista.add(new TipologiaEscursione("Ferrata", new ArrayList<>()));
        
        return lista;
    }

    @Override
    public void aggiungiLuogoEsplorabile(LuogoEsplorabile l) {
        System.out.println("[MOCK DB] Salvato con successo il nuovo luogo: " + l.nome + " (" + l.nomeZona + ", " + l.nomePaese + ") - Cat: " + l.nomeCategoria);
    }

    @Override
    public List<String> getPaesi() {
        return List.of("Italia", "Francia", "Svizzera", "Austria");
    }

    @Override
    public List<String> getZonePerPaese(String paese) {
        if ("Italia".equalsIgnoreCase(paese)) {
            return List.of("Valle d'Aosta", "Piemonte", "Trentino-Alto Adige", "Toscana", "Abruzzo");
        } else if ("Francia".equalsIgnoreCase(paese)) {
            return List.of("Alta Savoia", "Alpi Marittime", "Provenza");
        } else if ("Svizzera".equalsIgnoreCase(paese)) {
            return List.of("Vallese", "Canton Ticino", "Grigioni");
        }
        return List.of("Zona Alpina Generica", "Zona di Confine");
    }

    // ⚡ IL METODO CHE MANCAVA PER COMPILARE!
    @Override
    public List<String> getLuoghiPerZona(String paese, String zona) {
        if ("Abruzzo".equalsIgnoreCase(zona)) {
            return List.of("Campo Imperatore", "Corno Grande");
        } else if ("Toscana".equalsIgnoreCase(zona)) {
            return List.of("Val d'Orcia Trail", "Monte Amiata");
        } else if ("Alta Savoia".equalsIgnoreCase(zona)) {
            return List.of("Aiguille du Midi", "Chamonix");
        }
        return List.of("Luogo di prova 1", "Luogo di prova 2");
    }

    @Override
    public List<String> getCategorieLuoghi() {
        return List.of("Rifugio", "Bivacco", "Sentiero", "Via Ferrata", "Ghiacciaio", "Sito d'Arrampicata", "Grotta");
    }

    @Override
    public List<EscursionePreview> getEscursioniGuida(String guidaCF) { 
        return List.of();
    }

    @Override
    public void aggiungiPaese(String nomePaese) {
        System.out.println("[MOCK DB] Paese aggiunto: " + nomePaese);
    }

    @Override
    public void aggiungiZona(String nomePaese, String nomeZona, String descrizione) {
        System.out.println("[MOCK DB] Zona aggiunta: " + nomeZona + " in " + nomePaese);
    }
}