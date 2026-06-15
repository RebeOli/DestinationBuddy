package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Certificazione {
    public final TipologiaCertificazione tipologia;
    public final String nCertificazione;
    public final String enteRilasciante;
    public final LocalDate dataRilascio;
    public final LocalDate dataScadenza;
    public final String statoValidazione;
    public final String cf;
    public final String guidaCF;
    public Certificazione(TipologiaCertificazione tipologia, String nCertificazione, String enteRilasciante,
            LocalDate dataRilascio, LocalDate dataScadenza, String statoValidazione, String cf, String guidaCF) {
        this.tipologia = tipologia;
        this.nCertificazione = nCertificazione == null ? "" : nCertificazione;
        this.enteRilasciante = enteRilasciante == null ? "" : enteRilasciante;
        this.dataRilascio = dataRilascio;
        this.dataScadenza = dataScadenza;
        this.statoValidazione = statoValidazione == null ? "" : statoValidazione;
        this.cf = cf == null ? "" : cf;
        this.guidaCF = guidaCF;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tipologia == null) ? 0 : tipologia.hashCode());
        result = prime * result + ((nCertificazione == null) ? 0 : nCertificazione.hashCode());
        result = prime * result + ((enteRilasciante == null) ? 0 : enteRilasciante.hashCode());
        result = prime * result + ((dataRilascio == null) ? 0 : dataRilascio.hashCode());
        result = prime * result + ((dataScadenza == null) ? 0 : dataScadenza.hashCode());
        result = prime * result + ((statoValidazione == null) ? 0 : statoValidazione.hashCode());
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
        result = prime * result + ((guidaCF == null) ? 0 : guidaCF.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Certificazione other = (Certificazione) obj;
        if (tipologia == null) {
            if (other.tipologia != null)
                return false;
        } else if (!tipologia.equals(other.tipologia))
            return false;
        if (nCertificazione == null) {
            if (other.nCertificazione != null)
                return false;
        } else if (!nCertificazione.equals(other.nCertificazione))
            return false;
        if (enteRilasciante == null) {
            if (other.enteRilasciante != null)
                return false;
        } else if (!enteRilasciante.equals(other.enteRilasciante))
            return false;
        if (dataRilascio == null) {
            if (other.dataRilascio != null)
                return false;
        } else if (!dataRilascio.equals(other.dataRilascio))
            return false;
        if (dataScadenza == null) {
            if (other.dataScadenza != null)
                return false;
        } else if (!dataScadenza.equals(other.dataScadenza))
            return false;
        if (statoValidazione == null) {
            if (other.statoValidazione != null)
                return false;
        } else if (!statoValidazione.equals(other.statoValidazione))
            return false;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        if (guidaCF == null) {
            if (other.guidaCF != null)
                return false;
        } else if (!guidaCF.equals(other.guidaCF))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Certificazione [tipologia=" + tipologia + ", nCertificazione=" + nCertificazione + ", enteRilasciante="
                + enteRilasciante + ", dataRilascio=" + dataRilascio + ", dataScadenza=" + dataScadenza
                + ", statoValidazione=" + statoValidazione + ", cf=" + cf + ", guidaCF=" + guidaCF + "]";
    }


    public static final class DAO {
        // Metodo privato per leggere una certificazione dal ResultSet (riduce ripetizione)
            private static Certificazione readCertificazione(ResultSet rs) throws SQLException {
                var tipologia = new TipologiaCertificazione(
                    rs.getString("ID_certificazione"),
                    rs.getString("livello")
                );
    
                return new Certificazione(
                    tipologia,
                    rs.getString("n_certificazione"),
                    rs.getString("ente_rilasciante"),
                    rs.getDate("data_rilascio").toLocalDate(),
                    rs.getDate("data_scadenza").toLocalDate(),
                    rs.getString("stato_validazione"),
                    rs.getString("CF"),
                    rs.getString("Guida_CF")
                );
            }
        // tutte le certificzioni di un utente, da visualizzare sul profilo
        public static List<Certificazione> listForUtente(Connection connection, String cf ) {
            var certificazioni = new ArrayList<Certificazione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.CERTIFICAZIONI_UTENTE, cf);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    certificazioni.add(readCertificazione(resultSet));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return certificazioni;
        }
        //Lista di certificazioni in attesa, serve per l'operazione 9
        public static List<Certificazione> listInAttesa(Connection connection) {
            var certificazioni = new ArrayList<Certificazione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.CERTIFICAZIONI_IN_ATTESA);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    certificazioni.add(readCertificazione(resultSet));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return certificazioni;

        }

        //Per l'aggiunta da parte di un utente di una certificazione nuova -> operazione 1
        public static void create(Connection connection, Certificazione c) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.INSERISCI_CERTIFICAZIONE,
                    c.tipologia.idCertificazione, c.nCertificazione, c.enteRilasciante, java.sql.Date.valueOf(c.dataRilascio),
                    java.sql.Date.valueOf(c.dataScadenza), c.cf);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        //per cambiare lo stato della certificazione, sempre operazione 9
        public static void valida(Connection connection, String nCertificazione) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.VALIDA_CERTIFICAZIONE, nCertificazione);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

    }

}
