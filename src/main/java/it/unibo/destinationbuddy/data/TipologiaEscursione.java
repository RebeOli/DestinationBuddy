package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class TipologiaEscursione {

    public final String idTipologia;
    public final List<String> certificazioniRichieste; // Lista degli ID

    public TipologiaEscursione(String idTipologia, List<String> certificazioniRichieste) {
        this.idTipologia = idTipologia == null ? "" : idTipologia;
        this.certificazioniRichieste = certificazioniRichieste == null ? new ArrayList<>() : certificazioniRichieste;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TipologiaEscursione other = (TipologiaEscursione) obj;
        if (idTipologia == null) {
            if (other.idTipologia != null)
                return false;
        } else if (!idTipologia.equals(other.idTipologia))
            return false;
        if (certificazioniRichieste == null) {
            if (other.certificazioniRichieste != null)
                return false;
        } else if (!certificazioniRichieste.equals(other.certificazioniRichieste))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idTipologia == null) ? 0 : idTipologia.hashCode());
        result = prime * result + ((certificazioniRichieste == null) ? 0 : certificazioniRichieste.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TipologiaEscursione [idTipologia=" + idTipologia + ", certificazioniRichieste="
                + certificazioniRichieste + "]";
    }

    public static final class DAO {
        public static void create(Connection connection, TipologiaEscursione tipologia) {
            try{
                try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_TIPOLOGIA_ESCURSIONE, tipologia.idTipologia)) {
                    statement.executeUpdate();
                }
                // Scorre la lista e inserisce ogni certificazione nella tabella ponte "richiede"
                for (String idCertificazione : tipologia.certificazioniRichieste) {
                    try (var stmt = DAOUtils.prepare(connection, Queries.ASSOCIA_CERTIFICAZIONE_TIPOLOGIA, tipologia.idTipologia, idCertificazione)) {
                        stmt.executeUpdate();
                    }
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static List<String> listForEscursione(Connection connection, String idEscursione) {
            var tipologie = new ArrayList<String>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.TIPOLOGIE_ESCURSIONE, idEscursione);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    tipologie.add(resultSet.getString("ID_tipologia"));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return tipologie;
        }
    }
}
