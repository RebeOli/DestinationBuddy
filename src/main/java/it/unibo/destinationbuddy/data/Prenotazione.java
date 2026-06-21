package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Prenotazione {

    public final String cf;
    public final String idEscursione;
    public final LocalDate dataPrenotazione;
    public final String stato;
    public final String titoloEscursione;

    public Prenotazione(String cf, String idEscursione, String titoloEscursione, 
                    LocalDate dataPrenotazione, String stato) {
    this.cf = cf == null ? "" : cf;
    this.idEscursione = idEscursione == null ? "" : idEscursione;
    this.titoloEscursione = titoloEscursione == null ? "" : titoloEscursione;
    this.dataPrenotazione = dataPrenotazione;
    this.stato = stato == null ? "" : stato;
}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Prenotazione other = (Prenotazione) obj;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        if (idEscursione == null) {
            if (other.idEscursione != null)
                return false;
        } else if (!idEscursione.equals(other.idEscursione))
            return false;
        if (dataPrenotazione == null) {
            if (other.dataPrenotazione != null)
                return false;
        } else if (!dataPrenotazione.equals(other.dataPrenotazione))
            return false;
        if (stato == null) {
            if (other.stato != null)
                return false;
        } else if (!stato.equals(other.stato))
            return false;
        if (titoloEscursione == null) {
            if (other.titoloEscursione != null)
                return false;
        } else if (!titoloEscursione.equals(other.titoloEscursione))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
        result = prime * result + ((idEscursione == null) ? 0 : idEscursione.hashCode());
        result = prime * result + ((dataPrenotazione == null) ? 0 : dataPrenotazione.hashCode());
        result = prime * result + ((stato == null) ? 0 : stato.hashCode());
        result = prime * result + ((titoloEscursione == null) ? 0 : titoloEscursione.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Prenotazione [cf=" + cf + ", idEscursione=" + idEscursione + ", dataPrenotazione=" + dataPrenotazione
                + ", stato=" + stato + ", titoloEscursione=" + titoloEscursione + "]";
    }

    public static final class DAO {

        /**
         * Verifica se l'utente ha il vantaggio "accesso prioritario" attivo.
         */
        public static boolean haAccessoPrioritario(Connection connection, String cf) {
            try (var stmt = DAOUtils.prepare(connection, Queries.CONTROLLA_ACCESSO_PRIORITARIO, cf);
                 var rs = stmt.executeQuery()) {
                // Se la query restituisce un risultato, significa che ha i vantaggi attivi
                return rs.next();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        /**
         * Verifica i posti.
         */
        public static int getPostiRimanenti(Connection connection, String idEscursione) {
            try (
                var stmt = DAOUtils.prepare(connection, Queries.POSTI_RIMANENTI, idEscursione); // DAOUtils.prepare inserisce idEscursione al posto del "?" nella stringa SQL
                var rs = stmt.executeQuery()
            ) {
                if (rs.next()) {
                    return rs.getInt("posti_rimanenti");
                }
                return 0; // Se non trova nulla, restituisce 0
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        /**
         * Verifica che l'utente possieda le certificazioni richieste dall'escursione.
         */
        public static boolean verificaCertificazioni(Connection connection, String idEscursione, String cf) {
            try (var stmt = DAOUtils.prepare(connection, Queries.VERIFICA_CERTIFICAZIONI, idEscursione, cf, idEscursione);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int richieste = rs.getInt("cert_richieste");
                    int possedute = rs.getInt("cert_possedute_valide");
                    return possedute >= richieste; // True se ha tutte le carte in regola!
                }
                return false;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        /**
         * Se tutti i controlli sopra vanno a buon fine, inserisce la prenotazione.
         */
        public static boolean confermaPrenotazione(Connection connection, String cf, String idEscursione) {
            try (
                var stmt = DAOUtils.prepare(connection, Queries.CONFERMA_PRENOTAZIONE, cf, idEscursione)
            ) {
                return stmt.executeUpdate() > 0;
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                // Utente ha già una prenotazione per questa escursione
                return false;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static List<Prenotazione> getPrenotazioniUtente(Connection connection, String cf) {
            var prenotazioni = new ArrayList<Prenotazione>();
            try (
                var stmt = DAOUtils.prepare(connection, Queries.PRENOTAZIONI_UTENTE, cf);
                var rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    prenotazioni.add(new Prenotazione(
                        rs.getString("CF"),
                        rs.getString("ID_escursione"),
                        rs.getString("titolo"),
                        rs.getDate("data_prenotazione").toLocalDate(),
                        rs.getString("stato")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return prenotazioni;
        }
    }
}