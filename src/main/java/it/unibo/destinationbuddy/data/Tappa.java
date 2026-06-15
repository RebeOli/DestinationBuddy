package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Tappa {

    public final String idTappa;
    public final int durata;
    public final String idEscursione;
    public final LocalDate data;
    public final String nomePaese;
    public final String nomeZona;
    public final String nomeLuogo;

    public Tappa(String idTappa, int durata, String idEscursione, LocalDate data,
                 String nomePaese, String nomeZona, String nomeLuogo) {
        this.idTappa = idTappa == null ? "" : idTappa;
        this.durata = durata;
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.data = data;
        this.nomePaese = nomePaese == null ? "" : nomePaese;
        this.nomeZona = nomeZona == null ? "" : nomeZona;
        this.nomeLuogo = nomeLuogo == null ? "" : nomeLuogo;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idTappa == null) ? 0 : idTappa.hashCode());
        result = prime * result + durata;
        result = prime * result + ((idEscursione == null) ? 0 : idEscursione.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((nomePaese == null) ? 0 : nomePaese.hashCode());
        result = prime * result + ((nomeZona == null) ? 0 : nomeZona.hashCode());
        result = prime * result + ((nomeLuogo == null) ? 0 : nomeLuogo.hashCode());
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
        Tappa other = (Tappa) obj;
        if (idTappa == null) {
            if (other.idTappa != null)
                return false;
        } else if (!idTappa.equals(other.idTappa))
            return false;
        if (durata != other.durata)
            return false;
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
        if (nomePaese == null) {
            if (other.nomePaese != null)
                return false;
        } else if (!nomePaese.equals(other.nomePaese))
            return false;
        if (nomeZona == null) {
            if (other.nomeZona != null)
                return false;
        } else if (!nomeZona.equals(other.nomeZona))
            return false;
        if (nomeLuogo == null) {
            if (other.nomeLuogo != null)
                return false;
        } else if (!nomeLuogo.equals(other.nomeLuogo))
            return false;
        return true;
    }

    



    @Override
    public String toString() {
        return "Tappa [idTappa=" + idTappa + ", durata=" + durata + ", idEscursione=" + idEscursione + ", data=" + data
                + ", nomePaese=" + nomePaese + ", nomeZona=" + nomeZona + ", nomeLuogo=" + nomeLuogo + "]";
    }





    public static final class DAO {

        // Lista delle tappe di una specifica giornata di una specifica escursione
        public static List<Tappa> listForGiornata(Connection connection, String idEscursione, LocalDate data) {
            var tappe = new ArrayList<Tappa>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.TAPPE_PER_GIORNATA, idEscursione, data);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    tappe.add(new Tappa(
                        resultSet.getString("ID_tappa"),
                        resultSet.getInt("durata"),
                        resultSet.getString("ID_escursione"),
                        resultSet.getDate("data").toLocalDate(),
                        resultSet.getString("nome_paese"),
                        resultSet.getString("nome_zona"),
                        resultSet.getString("nome_luogo")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return tappe;
        }

        // Inserisce una nuova tappa
        public static void create(Connection connection, Tappa t) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.INSERISCI_TAPPA,
                    t.idTappa, t.durata, t.idEscursione,
                    java.sql.Date.valueOf(t.data), //da formato LocalDate a formato SQL
                    t.nomePaese, t.nomeZona, t.nomeLuogo);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
