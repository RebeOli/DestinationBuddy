package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.Objects;

import it.unibo.destinationbuddy.data.Pezzo;
import it.unibo.destinationbuddy.data.Prenotazione;

public class DBPrenotazioniModel implements PrenotazioniModel {

    private final Connection connection;

    public DBPrenotazioniModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    @Override
    public boolean haAccessoPrioritario(String cf) {
        return Prenotazione.DAO.haAccessoPrioritario(connection, cf);
    }

    @Override
    public int getPostiRimanenti(String idEscursione) {
        return Prenotazione.DAO.getPostiRimanenti(connection, idEscursione);
    }

    @Override
    public boolean verificaCertificazioni(String idEscursione, String cf) {
        return Prenotazione.DAO.verificaCertificazioni(connection, idEscursione, cf);
    }

    @Override
    public boolean confermaPrenotazione(String cf, String idEscursione) {
        return Prenotazione.DAO.confermaPrenotazione(connection, cf, idEscursione);
    }

    @Override
    public double getScontoNoleggio(String cf) {
        return Pezzo.DAO.getScontoNoleggio(connection, cf);
    }

    @Override
    public String trovaPezzoDisponibile(String idCategoria) {
        return Pezzo.DAO.trovaPezzoDisponibile(connection, idCategoria);
    }

    @Override
    public boolean noleggiaPezzo(String idPezzo, String idEscursione, String cf, int durataNoleggio) {
        return Pezzo.DAO.noleggiaPezzo(connection, idPezzo, idEscursione, cf, durataNoleggio);
    }
}
