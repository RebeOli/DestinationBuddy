package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Abbonamento;
import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.Queries;

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

    @Override
    public boolean verificaSeGuida(String cf) {
        if (cf == null || cf.isEmpty()) {
            return false;
        }

        try (
            var statement = DAOUtils.prepare(connection, Queries.VERIFICA_GUIDA_ESISTENTE, cf);
            var resultSet = statement.executeQuery()
        ) {
            return resultSet.next();
        } catch (Exception e) {
            System.err.println("Errore durante la verifica del ruolo Guida: " + e.getMessage());
            return false;
        }
    }
}
