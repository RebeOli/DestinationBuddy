package it.unibo.destinationbuddy.model;

import java.util.List;

import it.unibo.destinationbuddy.data.Persona;

public interface AdminModel {
    List<Persona> getUtentiDaPremiare();
    void disattivaGuida(Persona guida);
    void attivaGuida(Persona guida);
}
