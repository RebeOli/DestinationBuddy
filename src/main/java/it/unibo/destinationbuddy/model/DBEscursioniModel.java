package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.TipologiaEscursione;

public class DBEscursioniModel implements EscursioniModel {
    private final Connection connection;
    private Optional<List<EscursionePreview>> cacheAll;
    private Optional<List<EscursionePreview>> cacheTop5;


    public DBEscursioniModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.cacheTop5 = Optional.empty();
        this.cacheAll = Optional.empty();
    }

    
    @Override
    public List<EscursionePreview> getTop5() {
        if (cacheTop5.isEmpty()) {
            // Prima volta: chiama il DAO e salva in cache
            var risultato = EscursionePreview.DAO.top5(connection);
            cacheTop5 = Optional.of(risultato);
        }
        // Successive: restituisci direttamente la cache
        return cacheTop5.get();
    }

    @Override
    public List<EscursionePreview> getEscursioniPerMese(int mese) {
        return EscursionePreview.DAO.perMese(connection, mese);
    }

    @Override
    public List<EscursionePreview> getAll() {
        if (cacheAll.isEmpty()) {
            // Prima volta: chiama il DAO e salva in cache
            var risultato = EscursionePreview.DAO.list(connection);
            cacheAll = Optional.of(risultato);
        }
        // Successive: restituisci direttamente la cache
        return cacheAll.get();
    }

    @Override
    public Optional<Escursione> getDettaglio(EscursionePreview escursionePreview) {
        return Escursione.DAO.find(connection, escursionePreview.idEscursione);
    }

    @Override
    public List<EscursionePreview> getEscursionePerTipologia(TipologiaEscursione tipologia) {
        return EscursionePreview.DAO.listByTipologia(connection, tipologia.idTipologia);
    }

    @Override
    public void creaEscursione(Escursione e, String descrizione, int numeroPartecipanti, String guidaCF,
            List<String> tipologie) {
        Escursione.DAO.create(connection, e, descrizione, numeroPartecipanti, guidaCF, tipologie);
        cacheAll = Optional.empty();
        cacheTop5 = Optional.empty(); //resetto la cache in modo da vedere poi la nuova escursione creata. 
    }

    @Override
    public List<TipologiaEscursione> getTipologie() {
        return TipologiaEscursione.DAO.list(connection);
    }
    
}
