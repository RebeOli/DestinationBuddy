package it.unibo.destinationbuddy.model.mocked;

import java.util.List;

import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.data.Resoconto;
import it.unibo.destinationbuddy.model.PostEscursioneModel;

public class MockedPostEscursioneModel implements PostEscursioneModel {

    @Override
    public boolean inserisciRecensione(Recensione r) {
        System.out.println("[MOCK] Recensione inserita con successo per l'escursione " + r.idEscursione + " (Voto: " + r.voto + "/5)");
        return true;
    }

    @Override
    public boolean inserisciResoconto(Resoconto r) {
        System.out.println("[MOCK] Resoconto meteo inserito con successo per l'escursione " + r.idEscursione);
        return true;
    }

    @Override
    public List<Resoconto> getResocontiGuida(String cfGuida) {
        return List.of();
    }
}