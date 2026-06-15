package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class Equipaggiamento {
    public final String idCategoria;
    public final double costoTotaleGiornaliero;

    public Equipaggiamento(String idCategoria, double costoTotaleGiornaliero) {
        this.idCategoria = idCategoria == null ? "" : idCategoria;
        this.costoTotaleGiornaliero = costoTotaleGiornaliero;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCategoria == null) ? 0 : idCategoria.hashCode());
        long temp;
        temp = Double.doubleToLongBits(costoTotaleGiornaliero);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Equipaggiamento other = (Equipaggiamento) obj;
        if (idCategoria == null) {
            if (other.idCategoria != null)
                return false;
        } else if (!idCategoria.equals(other.idCategoria))
            return false;
        if (Double.doubleToLongBits(costoTotaleGiornaliero) != Double.doubleToLongBits(other.costoTotaleGiornaliero))
            return false;
        return true;
    }

    

    @Override
    public String toString() {
        return "Equipaggiamento [idCategoria=" + idCategoria + ", costoTotaleGiornaliero=" + costoTotaleGiornaliero
                + "]";
    }



    public static final class DAO {
    // Lista delle giornate di una specifica escursione di una specifica escursione
        public static List<Equipaggiamento> listForEscursione(Connection connection, String idEscursione) {
            var equipaggiamenti = new ArrayList<Equipaggiamento>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.EQUIPAGGIAMENTI_RICHIESTI_ESCURSIONE, idEscursione);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var idCategoria = resultSet.getString("ID_categoria");
                    var costo = resultSet.getDouble("costo_totale_giornaliero");
                    equipaggiamenti.add(new Equipaggiamento(idCategoria, costo));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return equipaggiamenti;
        }

    }

}
