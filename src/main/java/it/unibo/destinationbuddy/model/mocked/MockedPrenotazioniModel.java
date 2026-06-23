package it.unibo.destinationbuddy.model.mocked;

import java.util.ArrayList;
import java.util.List;

import it.unibo.destinationbuddy.data.Prenotazione;
import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.model.PrenotazioniModel;

public class MockedPrenotazioniModel implements PrenotazioniModel {

    @Override
    public boolean haAccessoPrioritario(String cf) {
        return true;
    }

    @Override
    public int getPostiRimanenti(String idEscursione) {
        return 10;
    }

    @Override
    public boolean verificaCertificazioni(String idEscursione, String cf) {
        return true;
    }

    @Override
    public boolean confermaPrenotazione(String cf, String idEscursione) {
        System.out.println("[MOCK] Prenotazione confermata con successo per CF: " + cf + " - Escursione: " + idEscursione);
        return true;
    }

    @Override
    public double getScontoNoleggio(String cf) {
        return 0.20;
    }

    @Override
    public String trovaPezzoDisponibile(String idCategoria) {
        return "PZ-MOCK-" + idCategoria;
    }

    @Override
    public boolean noleggiaPezzo(String idPezzo, String idEscursione, String cf, int durataNoleggio) {
        System.out.println("[MOCK] Noleggio pezzo " + idPezzo + " confermato per CF: " + cf + " (Durata: " + durataNoleggio + " gg)");
        return true;
    }

    @Override
    public List<Prenotazione> getPrenotazioniUtente(String cf) {
        return new ArrayList<>();
    }

    @Override
    public List<Recensione> getRecensioniPerEscursione(String idEscursione) {
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean inserisciRecensione(Recensione r) {
        return true;
    }


}