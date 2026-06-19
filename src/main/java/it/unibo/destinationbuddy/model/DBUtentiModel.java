package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Abbonamento;
import it.unibo.destinationbuddy.data.Persona;

public class DBUtentiModel implements UtentiModel{

    private final Connection connection;

    public DBUtentiModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    @Override
    public Optional<Persona> getPersonaAutenticata(String email, String password) {
        return Persona.DAO.autentica(connection, email, password);
    }

    @Override
    public void registraUtente(Persona utente) {
        Persona.DAO.registraUtente(connection, utente);
    }

    @Override
    public int numeroEscursioniEffettuate(Persona utente) {
        return utente.escursioniEffettuate;
    }

    @Override
    public boolean sottoscriviAbbonamento(double costoMensile, int durata, String cf) {
        return Abbonamento.DAO.acquistaAbbonamento(connection, costoMensile, durata, cf);
    }

    @Override
    public Optional<Abbonamento> getUltimoAbbonamento(Persona utente) {
        return Abbonamento.DAO.trovaUltimoAbbonamento(connection, utente);
    }

}
