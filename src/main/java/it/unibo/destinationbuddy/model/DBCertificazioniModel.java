package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;

public class DBCertificazioniModel implements CertificazioniModel {

    private final Connection connection;
    private Optional<List<Certificazione>> cacheInAttesa;
    private Optional<List<TipologiaCertificazione>> cacheTipologie;



    public DBCertificazioniModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.cacheInAttesa = Optional.empty();
        this.cacheTipologie = Optional.empty();

    }

    @Override
    public List<Certificazione> getCertificazioniUtente(String cf) {
        return Certificazione.DAO.listForUtente(connection, cf);
    }

    @Override
    public List<Certificazione> getCertificazioniInAttesa() {
        if (cacheInAttesa.isEmpty()) {
            var risultato = Certificazione.DAO.listInAttesa(connection);
            cacheInAttesa = Optional.of(risultato);
        }
        return cacheInAttesa.get();
    }

    @Override
    public List<TipologiaCertificazione> getTipologieDisponibili() {
        if (cacheTipologie.isEmpty()) {
            var risultato = TipologiaCertificazione.DAO.listAll(connection);
            cacheTipologie = Optional.of(risultato);
        }
        return cacheTipologie.get();
    }

    @Override
    public void aggiungiCertificazione(Certificazione c) {
        Certificazione.DAO.create(connection, c);
        cacheInAttesa = Optional.empty();

    }

    @Override
    public void validaCertificazione(String idCertificazione, String nCertificazione) {
        Certificazione.DAO.valida(connection, idCertificazione, nCertificazione);
        cacheInAttesa = Optional.empty();
    }
    
}
