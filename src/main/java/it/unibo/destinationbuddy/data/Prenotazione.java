package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Objects;

public final class Prenotazione {

    public final String cf;
    public final String idEscursione;
    public final LocalDate dataPrenotazione;
    public final String stato;

    public Prenotazione(String cf, String idEscursione, LocalDate dataPrenotazione, String stato) {
        this.cf = cf == null ? "" : cf;
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.dataPrenotazione = dataPrenotazione;
        this.stato = stato == null ? "" : stato;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        
        Prenotazione that = (Prenotazione) other;
        return Objects.equals(cf, that.cf) &&
               Objects.equals(idEscursione, that.idEscursione) &&
               Objects.equals(dataPrenotazione, that.dataPrenotazione) &&
               Objects.equals(stato, that.stato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cf, idEscursione, dataPrenotazione, stato);
    }

    @Override
    public String toString() {
        return "Prenotazione [ cf='" + cf + "', escursione='" + idEscursione + "', stato='" + stato + "']";
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
                // executeUpdate restituisce il numero di righe modificate
                return stmt.executeUpdate() > 0;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}