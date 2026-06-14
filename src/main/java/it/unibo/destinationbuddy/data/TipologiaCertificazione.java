package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class TipologiaCertificazione {
    public final String idCertificazione;
    public final String livello;

    public TipologiaCertificazione(String idCertificazione, String livello) {
        this.idCertificazione = idCertificazione == null ? "" : idCertificazione;
        this.livello = livello == null ? "" : livello;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCertificazione == null) ? 0 : idCertificazione.hashCode());
        result = prime * result + ((livello == null) ? 0 : livello.hashCode());
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
        TipologiaCertificazione other = (TipologiaCertificazione) obj;
        if (idCertificazione == null) {
            if (other.idCertificazione != null)
                return false;
        } else if (!idCertificazione.equals(other.idCertificazione))
            return false;
        if (livello == null) {
            if (other.livello != null)
                return false;
        } else if (!livello.equals(other.livello))
            return false;
        return true;
    }

    public static final class DAO {

        public static List<TipologiaCertificazione> listForEscursione(Connection connection, String idEscursione) {
            var tipologie = new ArrayList<TipologiaCertificazione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.CERTIFICAZIONI_RICHIESTE_ESCURSIONE, idEscursione);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var idCertificazione = resultSet.getString("ID_certificazione");
                    var livello = resultSet.getString("livello");
                    var tipologia = new TipologiaCertificazione(idCertificazione, livello);
                    tipologie.add(tipologia);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return tipologie;
        }
        //elenco tutte le tipologie di certificaizoni disponibili, es: Alpinismo-Livello1, tra cui l'utente può scegliere. 
        public static List<TipologiaCertificazione> listAll(Connection connection) {
            var tipologie = new ArrayList<TipologiaCertificazione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.TIPOLOGIE_CERTIFICAZIONE_ALL);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    tipologie.add(new TipologiaCertificazione(
                        resultSet.getString("ID_certificazione"),
                        resultSet.getString("livello")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return tipologie;
        }
    }
}
