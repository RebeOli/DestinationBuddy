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
            private static Certificazione readCertificazione(ResultSet rs) throws SQLException {
                var tipologia = new TipologiaCertificazione(
                    rs.getString("ID_certificazione"),
                    rs.getString("livello")
                );

                java.sql.Date sqlRilascio = rs.getDate("data_rilascio");
                LocalDate dataRilascio = (sqlRilascio != null) ? sqlRilascio.toLocalDate() : null;

                java.sql.Date sqlScadenza = rs.getDate("data_scadenza");
                LocalDate dataScadenza = (sqlScadenza != null) ? sqlScadenza.toLocalDate() : null;
    
                return new Certificazione(
                    tipologia,
                    rs.getString("n_certificazione"),
                    rs.getString("ente_rilasciante"),
                    dataRilascio,
                    dataScadenza,
                    rs.getString("stato_validazione"),
                    rs.getString("CF"),
                    rs.getString("Guida_CF")
                );
            }
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
        public static void valida(Connection connection, String idCertificazione, String nCertificazione) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.VALIDA_CERTIFICAZIONE, idCertificazione, nCertificazione);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }

            promuoviAGuidaSeNecessario(connection, idCertificazione, nCertificazione);
        }

        private static void promuoviAGuidaSeNecessario(Connection connection, String idCertificazione, String nCertificazione) {
            try {
                String cf = null;

                try (var stmt = DAOUtils.prepare(connection, Queries.TROVA_CF_CERTIFICAZIONE, idCertificazione, nCertificazione);
                    var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        cf = rs.getString("CF");
                    }
                }
                if (cf == null) return;

                String livello = null;
                try (var stmt = DAOUtils.prepare(connection, Queries.TROVA_LIVELLO_TIPOLOGIA, idCertificazione);
                    var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        livello = rs.getString("livello");
                    }
                }
                if (!"Guida".equalsIgnoreCase(livello)) return;

                boolean giaGuida;
                try (var stmt = DAOUtils.prepare(connection, Queries.VERIFICA_GUIDA_ESISTENTE, cf);
                    var rs = stmt.executeQuery()) {
                    giaGuida = rs.next();
                }
                if (giaGuida) return;

                try (var stmt = DAOUtils.prepare(connection, Queries.INSERISCI_GUIDA, cf)) {
                    stmt.executeUpdate();
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

    }

}
