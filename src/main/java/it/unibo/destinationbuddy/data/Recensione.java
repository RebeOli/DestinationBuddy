package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Recensione {

    public final String titolo;
    public final String cf;
    public final int voto;
    public final String immagini;
    public final String descrizione;
    public final String statoRecensione; // 'In attesa', 'Approvata', 'Rifiutata'
    public final String idEscursione;

    public Recensione(String titolo, String cf, int voto, String immagini, 
                      String descrizione, String statoRecensione, String idEscursione) {
        this.titolo = titolo == null ? "" : titolo;
        this.cf = cf == null ? "" : cf;
        this.voto = voto;
        this.immagini = immagini == null ? "" : immagini;
        this.descrizione = descrizione == null ? "" : descrizione;
        this.statoRecensione = statoRecensione == null ? "" : statoRecensione;
        this.idEscursione = idEscursione == null ? "" : idEscursione;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Recensione that = (Recensione) other;
        return voto == that.voto &&
               Objects.equals(titolo, that.titolo) &&
               Objects.equals(cf, that.cf) &&
               Objects.equals(immagini, that.immagini) &&
               Objects.equals(descrizione, that.descrizione) &&
               Objects.equals(statoRecensione, that.statoRecensione) &&
               Objects.equals(idEscursione, that.idEscursione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, cf, voto, immagini, descrizione, statoRecensione, idEscursione);
    }

    @Override
    public String toString() {
        return "Recensione [ titolo='" + titolo + "', voto=" + voto + "★, autore='" + cf + "']";
    }

    public static final class DAO {

        /**
         * Operazione 4: Inserimento di una nuova recensione al termine dell'escursione.
         */
        public static boolean inserisci(Connection connection, Recensione r) {
            try (
                var stmt = DAOUtils.prepare(connection, Queries.INSERISCI_RECENSIONE, 
                                                 r.titolo, r.voto, r.immagini, r.descrizione, r.statoRecensione, r.cf, r.idEscursione)
            ) {
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
                
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static Optional<List<Recensione>> getRecensioniPerUtente(Connection connection, Persona utente) {
            final List<Recensione> recensioni = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.RECENSIONI_SCRITTE, utente.cf);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var titolo = resultSet.getString("titolo");
                    var voto = resultSet.getInt("voto");
                    var immagini = resultSet.getString("immagini");
                    var descrizione = resultSet.getString("descrizione");
                    var statoRecensione = resultSet.getString("statoRecensione");
                    var idEscursione = resultSet.getString("idEscursione");
                    var recensione = new Recensione(titolo, utente.cf, voto, immagini, descrizione, statoRecensione, idEscursione);
                    recensioni.add(recensione);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }

            if (recensioni.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(recensioni);
        }
    }
}
