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
    public final String statoRecensione;
    public final String idEscursione;
    public final String nomeAutore;
    public final String cognomeAutore;

    public Recensione(String titolo, String cf, int voto, String immagini, 
                      String descrizione, String statoRecensione, String idEscursione,
                      String nomeAutore, String cognomeAutore) {
        this.titolo = titolo == null ? "" : titolo;
        this.cf = cf == null ? "" : cf;
        this.voto = voto;
        this.immagini = immagini == null ? "" : immagini;
        this.descrizione = descrizione == null ? "" : descrizione;
        this.statoRecensione = statoRecensione == null ? "" : statoRecensione;
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.nomeAutore = nomeAutore == null ? "" : nomeAutore;
        this.cognomeAutore = cognomeAutore == null ? "" : cognomeAutore;
    }

    public Recensione(String titolo, String cf, int voto, String immagini, 
                      String descrizione, String statoRecensione, String idEscursione) {
        this(titolo, cf, voto, immagini, descrizione, statoRecensione, idEscursione, "", "");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Recensione that = (Recensione) other;
        return voto == that.voto &&
               Objects.equals(titolo, that.titolo) &&
               Objects.equals(cf, that.cf) &&
               Objects.equals(idEscursione, that.idEscursione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, cf, voto, idEscursione);
    }

    public static final class DAO {

        public static boolean inserisci(Connection connection, Recensione r) {
            try (
                var stmt = DAOUtils.prepare(connection, Queries.INSERISCI_RECENSIONE, 
                                                 r.titolo, r.voto, r.immagini, r.descrizione, r.statoRecensione, r.cf, r.idEscursione)
            ) {
                return stmt.executeUpdate() > 0;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static Optional<List<Recensione>> getRecensioniPerUtente(Connection connection, Persona utente) {
            final List<Recensione> recensioni = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.RECENSIONI_SCRITTE, utente.cf);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recensioni.add(new Recensione(
                        rs.getString("titolo"), utente.cf, rs.getInt("voto"), 
                        rs.getString("immagini"), rs.getString("descrizione"), 
                        rs.getString("stato_recensione"), rs.getString("ID_escursione")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return recensioni.isEmpty() ? Optional.empty() : Optional.of(recensioni);
        }

        public static Optional<List<Recensione>> getRecensioniPerEscursione(Connection connection, String idEscursione) {
            final List<Recensione> recensioni = new ArrayList<>();
            try (var stmt = DAOUtils.prepare(connection, Queries.RECENSIONI_ESCURSIONE, idEscursione);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recensioni.add(new Recensione(
                        rs.getString("titolo"), 
                        rs.getString("CF"), 
                        rs.getInt("voto"), 
                        rs.getString("immagini"), 
                        rs.getString("descrizione"), 
                        rs.getString("stato_recensione"), 
                        idEscursione,
                        rs.getString("nome"),
                        rs.getString("cognome")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return recensioni.isEmpty() ? Optional.empty() : Optional.of(recensioni);
        }
    }
}