package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.Objects;

public final class Pezzo {

    public final String idPezzo;
    public final String nome;
    public final double costoGiornaliero;
    public final boolean disponibilita;
    public final String idCategoria;

    public Pezzo(String idPezzo, String nome, double costoGiornaliero, boolean disponibilita, String idCategoria) {
        this.idPezzo = idPezzo == null ? "" : idPezzo;
        this.nome = nome == null ? "" : nome;
        this.costoGiornaliero = costoGiornaliero;
        this.disponibilita = disponibilita;
        this.idCategoria = idCategoria == null ? "" : idCategoria;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Pezzo pezzo = (Pezzo) other;
        return Double.compare(pezzo.costoGiornaliero, costoGiornaliero) == 0 &&
               disponibilita == pezzo.disponibilita &&
               Objects.equals(idPezzo, pezzo.idPezzo) &&
               Objects.equals(nome, pezzo.nome) &&
               Objects.equals(idCategoria, pezzo.idCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPezzo, nome, costoGiornaliero, disponibilita, idCategoria);
    }

    @Override
    public String toString() {
        return "Pezzo [ id='" + idPezzo + "', nome='" + nome + "', disponibile=" + disponibilita + "]";
    }

    public static final class DAO {
        public static double getScontoNoleggio(Connection connection, String cf) {
            try (var stmt = DAOUtils.prepare(connection, Queries.SCONTO_NOLEGGIO, cf);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("sconto_noleggio");
                }
                return 0.0;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static String trovaPezzoDisponibile(Connection connection, String idCategoria) {
            try (var stmt = DAOUtils.prepare(connection, Queries.PEZZO_DISPONIBILE, idCategoria);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ID_pezzo");
                }
                return null;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static boolean noleggiaPezzo(Connection connection, String idPezzo, String idEscursione, String cf, int durataNoleggio) {
            try {
                try (var stmtAssegna = DAOUtils.prepare(connection, Queries.ASSEGNA_PEZZO, idPezzo, idEscursione, cf, durataNoleggio)) {
                    stmtAssegna.executeUpdate();
                }

                try (var stmtAggiorna = DAOUtils.prepare(connection, Queries.AGGIORNA_DISPONIBILITA_PEZZO, idPezzo)) {
                    return stmtAggiorna.executeUpdate() > 0;
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}