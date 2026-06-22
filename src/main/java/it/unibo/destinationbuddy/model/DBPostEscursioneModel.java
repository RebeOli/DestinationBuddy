package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.data.Resoconto;

public class DBPostEscursioneModel implements PostEscursioneModel {

    private final Connection connection;

    public DBPostEscursioneModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }
    @Override
    public boolean inserisciRecensione(Recensione r) {
        return Recensione.DAO.inserisci(connection, r);
    }

    @Override
    public boolean inserisciResoconto(Resoconto r) {
        return Resoconto.DAO.inserisci(connection, r);
    }
    @Override
    public List<Resoconto> getResocontiGuida(String cfGuida) {
        return Resoconto.DAO.listForGuida(connection, cfGuida);
    }
}
