package it.unibo.destinationbuddy.model;

import java.util.Optional;

import it.unibo.destinationbuddy.data.Abbonamento;
import it.unibo.destinationbuddy.data.Persona;

public interface UtentiModel {
    Optional<Persona> getPersonaAutenticata(String email, String password);
    // void getCertificazioni(Persona utente);
    void registraUtente(Persona utente);
    int numeroEscursioniEffettuate(Persona utente);
    boolean sottoscriviAbbonamento(double costoMensile, int durata, String cf);
    Optional<Abbonamento> getUltimoAbbonamento(Persona utente);
    boolean verificaSeGuida(String cf);
}
