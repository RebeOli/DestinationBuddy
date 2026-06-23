package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.DAOException;
import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.data.Queries;
import it.unibo.destinationbuddy.data.Recensione;

public class DBAdminModel implements AdminModel {

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
            var risultato = Persona.DAO.getUtentiDaPremiare(connection);
            cacheUtentiDaPremiare = Optional.of(risultato);
        }
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

    @Override
    public List<Recensione> getTutteLeRecensioni() {
        final List<Recensione> recensioni = new ArrayList<>();
        try (var stmt = DAOUtils.prepare(connection, Queries.GET_TUTTE_RECENSIONI);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                recensioni.add(new Recensione(
                    rs.getString("titolo"), rs.getString("CF"), rs.getInt("voto"), 
                    rs.getString("immagini"), rs.getString("descrizione"), 
                    rs.getString("stato_recensione"), rs.getString("ID_escursione"),
                    rs.getString("nome"), rs.getString("cognome")
                ));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return recensioni;
    }

    @Override
    public void eliminaRecensione(String cf, String idEscursione) {
        try (var stmt = DAOUtils.prepare(connection, Queries.ELIMINA_RECENSIONE, cf, idEscursione)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<String> getGuideSospendibili() {
        return Persona.DAO.getGuideSospendibili(connection);
    }
}
