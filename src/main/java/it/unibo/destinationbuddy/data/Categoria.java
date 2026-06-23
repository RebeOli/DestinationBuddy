package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class Categoria {

    public final String nomeCategoria;

    public Categoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria == null ? "" : nomeCategoria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nomeCategoria == null) ? 0 : nomeCategoria.hashCode());
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
        Categoria other = (Categoria) obj;
        if (nomeCategoria == null) {
            if (other.nomeCategoria != null)
                return false;
        } else if (!nomeCategoria.equals(other.nomeCategoria))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Categoria [nomeCategoria=" + nomeCategoria + "]";
    }

    public static final class DAO {
        public static List<Categoria> listAll(Connection connection) {
            var categorie = new ArrayList<Categoria>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.CATEGORIE_ALL);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    categorie.add(new Categoria(resultSet.getString("nome_categoria")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return categorie;
        }
    }
}
