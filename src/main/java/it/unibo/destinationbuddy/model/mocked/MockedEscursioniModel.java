package it.unibo.destinationbuddy.model.mocked;

import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.TipologiaEscursione;
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
    public void creaEscursione(Escursione e, String descrizione, int numeroPartecipanti, String guidaCF, List<String> tipologie) {
        System.out.println("[MOCK] Creata nuova escursione: " + e.titolo + " (Guida CF: " + guidaCF + ")");
    }

    @Override
    public List<TipologiaEscursione> getTipologie() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTipologieDisponibili'");
    }



}