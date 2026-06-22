package it.unibo.destinationbuddy.model;

import java.util.List;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.Recensione;

public interface AdminModel {
    List<Persona> getUtentiDaPremiare();
    void disattivaGuida(Persona guida);
    void attivaGuida(Persona guida);
    List<Persona> getTutteLeGuide();
    List<Recensione> getTutteLeRecensioni();
    void eliminaRecensione(String cf, String idEscursione);
}