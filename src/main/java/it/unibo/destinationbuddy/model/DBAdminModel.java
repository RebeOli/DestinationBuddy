package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Persona;

public class DBAdminModel implements AdminModel{

    public Connection connection;
    public Optional<List<Persona>> cacheUtentiDaPremiare;

    public DBAdminModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.cacheUtentiDaPremiare = Optional.empty();
    }

    @Override
    public List<Persona> getUtentiDaPremiare() {
        if (cacheUtentiDaPremiare.isEmpty()) {
            // Prima volta: chiama il DAO e salva in cache
            var risultato = Persona.DAO.getUtentiDaPremiare(connection);
            cacheUtentiDaPremiare = Optional.of(risultato);
        }
        // Successive: restituisci direttamente la cache
        return cacheUtentiDaPremiare.get();
    }

    @Override
    public void disattivaGuida(Persona guida) {
        Persona.DAO.disattivaGuida(connection, guida);
    }

    @Override
    public void attivaGuida(Persona guida) {
        Persona.DAO.attivaGuida(connection, guida);
    }

    @Override
    public List<Persona> getTutteLeGuide() {
        return Persona.DAO.getTutteLeGuide(connection);
    }
}
