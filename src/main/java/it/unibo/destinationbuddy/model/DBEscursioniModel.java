package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.TipologiaEscursione;
// IMPORTANTE: Aggiunto l'import per TipologiaCertificazione
import it.unibo.destinationbuddy.data.TipologiaCertificazione; 

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
            var risultato = EscursionePreview.DAO.top5(connection);
            cacheTop5 = Optional.of(risultato);
        }
        return cacheTop5.get();
    }

    @Override
    public List<EscursionePreview> getEscursioniPerMese(int mese) {
        return EscursionePreview.DAO.perMese(connection, mese);
    }

    @Override
    public List<EscursionePreview> getAll() {
        if (cacheAll.isEmpty()) {
            var risultato = EscursionePreview.DAO.list(connection);
            cacheAll = Optional.of(risultato);
        }
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
    public List<TipologiaEscursione> getTipologie() {
        return TipologiaEscursione.DAO.list(connection);
    }

    @Override
    public void creaEscursione(Escursione e, String descrizione, int numeroPartecipanti, String guidaCF,
            List<String> tipologie, List<String> certificazioniSelezionate, TipologiaCertificazione nuovaCertificazione) {
        
        // 1. Se la guida ha inventato un brevetto, lo salviamo nel DB e lo aggiungiamo a quelli selezionati
        if (nuovaCertificazione != null) {
            TipologiaCertificazione.DAO.create(connection, nuovaCertificazione);
            certificazioniSelezionate.add(nuovaCertificazione.idCertificazione);
        }

        // 2. Creiamo l'escursione base (questo metodo del DAO lega già l'escursione alle Tipologie in 'assume')
        Escursione.DAO.create(connection, e, descrizione, numeroPartecipanti, guidaCF, tipologie);

        // 3. Leghiamo le certificazioni alle Tipologie di Escursione (nella tabella 'richiede')
        for (String idTipologia : tipologie) {
            for (String idCert : certificazioniSelezionate) {
                TipologiaEscursione.DAO.associaCertificazione(connection, idTipologia, idCert);
            }
        }

        // 4. Resettiamo la cache per far apparire la nuova escursione
        cacheAll = Optional.empty();
        cacheTop5 = Optional.empty(); 
    }
}