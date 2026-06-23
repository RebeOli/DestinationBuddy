package it.unibo.destinationbuddy.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.LuogoEsplorabile;
import it.unibo.destinationbuddy.data.Queries;
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
        
        if (nuovaCertificazione != null) {
            TipologiaCertificazione.DAO.create(connection, nuovaCertificazione);
            certificazioniSelezionate.add(nuovaCertificazione.idCertificazione);
        }

        Escursione.DAO.create(connection, e, descrizione, numeroPartecipanti, guidaCF, tipologie);

        for (String idTipologia : tipologie) {
            for (String idCert : certificazioniSelezionate) {
                TipologiaEscursione.DAO.associaCertificazione(connection, idTipologia, idCert);
            }
        }

        cacheAll = Optional.empty();
        cacheTop5 = Optional.empty(); 
    }

    @Override
    public void aggiungiLuogoEsplorabile(LuogoEsplorabile l) {
        LuogoEsplorabile.DAO.create(connection, l);
    }

    @Override
    public List<String> getPaesi() {
        return LuogoEsplorabile.DAO.listPaesi(connection);
    }

    @Override
    public List<String> getZonePerPaese(String paese) {
        return LuogoEsplorabile.DAO.listZonePerPaese(connection, paese);
    }

    @Override
    public List<String> getCategorieLuoghi() {
        return LuogoEsplorabile.DAO.listCategorie(connection);
    }

    @Override
        public List<EscursionePreview> getEscursioniGuida(String guidaCF) {
        return EscursionePreview.DAO.listByGuida(connection, guidaCF);
    }

    @Override
    public List<String> getLuoghiPerZona(String paese, String zona) {
        var luoghi = new ArrayList<String>();
        try (
            var statement = DAOUtils.prepare(connection, Queries.LIST_LUOGHI_PER_ZONA, paese, zona);
            var resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                luoghi.add(resultSet.getString("nome"));
            }
        } catch (Exception e) {
            throw new it.unibo.destinationbuddy.data.DAOException(e);
        }
        return luoghi;
    }

    @Override
    public void aggiungiPaese(String nomePaese) {
        try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_PAESE, nomePaese)) {
            statement.executeUpdate();
        } catch (Exception e) {
            throw new it.unibo.destinationbuddy.data.DAOException(e);
        }
    }

    @Override
    public void aggiungiZona(String nomePaese, String nomeZona, String descrizione) {
        try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_ZONA, nomePaese, nomeZona, descrizione)) {
            statement.executeUpdate();
        } catch (Exception e) {
            throw new it.unibo.destinationbuddy.data.DAOException(e);
        }
    }
}