package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Giornata {
    public final String idEscursione;
    public final LocalDate data;
    public final String programma;
    public final List<Tappa> tappe;

    public Giornata(String idEscursione, LocalDate data, String programma, List<Tappa> tappe) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.data = data;
        this.programma = programma == null ? "" : programma;
        this.tappe = tappe == null ? new ArrayList<>() : tappe;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEscursione == null) ? 0 : idEscursione.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((programma == null) ? 0 : programma.hashCode());
        result = prime * result + ((tappe == null) ? 0 : tappe.hashCode());
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
        Giornata other = (Giornata) obj;
        if (idEscursione == null) {
            if (other.idEscursione != null)
                return false;
        } else if (!idEscursione.equals(other.idEscursione))
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (programma == null) {
            if (other.programma != null)
                return false;
        } else if (!programma.equals(other.programma))
            return false;
        if (tappe == null) {
            if (other.tappe != null)
                return false;
        } else if (!tappe.equals(other.tappe))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Giornata [idEscursione=" + idEscursione + ", data=" + data + ", programma=" + programma + ", tappe="
                + tappe + "]";
    }

    public static final class DAO {
        public static List<Giornata> listForEscursione(Connection connection, String idEscursione) {
            var giornate = new ArrayList<Giornata>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.GIORNATE_PER_ESCURSIONE, idEscursione);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var id = resultSet.getString("ID_escursione");
                    var data = resultSet.getDate("data").toLocalDate();
                    var programma = resultSet.getString("programma");
                    var tappe = Tappa.DAO.listForGiornata(connection, id, data);

                    giornate.add(new Giornata(id, data, programma, tappe));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return giornate;
        }

        public static void create(Connection connection, Giornata g) {
            try {
                try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_GIORNATA,
                        g.idEscursione, java.sql.Date.valueOf(g.data), g.programma)) {
                    statement.executeUpdate();
                }
                for (Tappa t : g.tappe) {
                    Tappa.DAO.create(connection, t);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }

}
