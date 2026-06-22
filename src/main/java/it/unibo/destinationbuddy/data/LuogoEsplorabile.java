package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class LuogoEsplorabile {
    public final String nome;
    public final String nomeZona;
    public final String nomePaese;
    public final String nomeCategoria;

    public LuogoEsplorabile(String nome, String nomeZona, String nomePaese, String nomeCategoria) {
        this.nome = nome == null ? "" : nome;
        this.nomeZona = nomeZona == null ? "" : nomeZona;
        this.nomePaese = nomePaese == null ? "" : nomePaese;
        this.nomeCategoria = nomeCategoria == null ? "" : nomeCategoria;
    }

    public static final class DAO {

        public static void create(Connection connection, LuogoEsplorabile l) {
            try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_LUOGO_ESPLORABILE,
                    l.nome, l.nomeZona, l.nomePaese, l.nomeCategoria)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static List<String> listPaesi(Connection connection) {
            var paesi = new ArrayList<String>();
            try (var statement = DAOUtils.prepare(connection, Queries.LIST_PAESI);
                 var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    paesi.add(resultSet.getString("Nome"));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return paesi;
        }

        public static List<String> listZonePerPaese(Connection connection, String nomePaese) {
            var zone = new ArrayList<String>();
            try (var statement = DAOUtils.prepare(connection, Queries.LIST_ZONE_PER_PAESE, nomePaese);
                 var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    zone.add(resultSet.getString("nome"));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return zone;
        }

        public static List<String> listCategorie(Connection connection) {
            var categorie = new ArrayList<String>();
            try (var statement = DAOUtils.prepare(connection, Queries.CATEGORIE_ALL);
                 var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    categorie.add(resultSet.getString("nome_categoria"));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return categorie;
        }
    }
}