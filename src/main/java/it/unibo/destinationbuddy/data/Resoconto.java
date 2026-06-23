package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Resoconto {

    public final String idEscursione;
    public final LocalDate dataInizio;
    public final LocalDate dataFine;
    public final double temperaturaRilevata;
    public final double precipitazioni;
    public final String cfGuida;

    public Resoconto(String idEscursione, LocalDate dataInizio, LocalDate dataFine, 
                     double temperaturaRilevata, double precipitazioni, String cfGuida) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.temperaturaRilevata = temperaturaRilevata;
        this.precipitazioni = precipitazioni;
        this.cfGuida = cfGuida == null ? "" : cfGuida;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Resoconto that = (Resoconto) other;
        return Double.compare(that.temperaturaRilevata, temperaturaRilevata) == 0 &&
               Double.compare(that.precipitazioni, precipitazioni) == 0 &&
               Objects.equals(idEscursione, that.idEscursione) &&
               Objects.equals(dataInizio, that.dataInizio) &&
               Objects.equals(dataFine, that.dataFine) &&
               Objects.equals(cfGuida, that.cfGuida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEscursione, dataInizio, dataFine, temperaturaRilevata, precipitazioni, cfGuida);
    }

    @Override
    public String toString() {
        return "Resoconto [ escursione='" + idEscursione + "', inizio=" + dataInizio + ", fine=" + dataFine + "]";
    }

    public static final class DAO {

        /**
         * Operazione 4: Consente alla guida di inserire i dati reali a conclusione dell'escursione.
         * Sfrutta la stringa SQL centralizzata Queries.INSERISCI_RESOCONTO.
         */
        public static boolean inserisci(Connection connection, Resoconto r) {
            try (
                var stmt = DAOUtils.prepare(connection, Queries.INSERISCI_RESOCONTO, 
                                             r.idEscursione, 
                                             java.sql.Date.valueOf(r.dataInizio), 
                                             java.sql.Date.valueOf(r.dataFine), 
                                             r.temperaturaRilevata, 
                                             r.precipitazioni, 
                                             r.cfGuida)
            ) {
                // Restituisce true se l'inserimento nella tabella RIEPILOGA va a buon fine
                return stmt.executeUpdate() > 0;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        public static List<Resoconto> listForGuida(Connection connection, String cfGuida) {
            var lista = new ArrayList<Resoconto>();
            try (
                var stmt = DAOUtils.prepare(connection, Queries.RESOCONTI_GUIDA, cfGuida);
                var rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    lista.add(new Resoconto(
                        rs.getString("ID_escursione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate(),
                        rs.getDouble("temperatura_rilevata"),
                        rs.getDouble("precipitazioni"),
                        cfGuida
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return lista;
        }
    }
}
