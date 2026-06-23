package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//in questa classe inseriamo solo i dati che deovno essere visibili nella preview di un'escursione 
//nella pagina home. Quindi certi dati più specifici andranno indicati in EscursioneDettagio che 
//mostra esattamente come un'entità escursione è realizzata. 
public final class EscursionePreview {

    public final String idEscursione;
    public final String titolo;
    public final String difficolta;
    public final double costo;

    public EscursionePreview(String idEscursione, String titolo, String difficolta,
                              double costo) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.titolo = titolo == null ? "" : titolo;
        this.difficolta = difficolta == null ? "" : difficolta;
        this.costo = costo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof EscursionePreview) {
            var e = (EscursionePreview) other;
            return e.idEscursione.equals(this.idEscursione)
                && e.titolo.equals(this.titolo)
                && e.difficolta.equals(this.difficolta)
                && e.costo == this.costo;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idEscursione, this.titolo, this.difficolta,
                            this.costo);
    }

    @Override
    public String toString() {
        return "EscursionePreview{idEscursione=" + idEscursione
            + ", titolo=" + titolo
            + ", difficolta=" + difficolta
            + ", costo=" + costo
            + "}";
    }

    public static final class DAO {

        public static List<EscursionePreview> list(Connection connection) {
            return execute(connection, Queries.LIST_ESCURSIONI);
        }

        public static List<EscursionePreview> top5(Connection connection) {
            return execute(connection, Queries.MIGLIORI_ESCURSIONI, 2);
        }

        public static List<EscursionePreview> perMese (Connection connection, int mese) {
            return execute(connection, Queries.ESCURSIONI_PER_MESE, mese);
        }

        public static List<EscursionePreview> listByTipologia(Connection connection, String idTipologia) {
            return execute(connection, Queries.ESCURSIONI_PER_TIPOLOGIA, idTipologia);
        }

        private static List<EscursionePreview> execute(Connection connection, String query, Object... params) {
            var previews = new ArrayList<EscursionePreview>();
            try (
                var statement = DAOUtils.prepare(connection, query, params);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var idEscursione = resultSet.getString("ID_escursione");
                    var titolo = resultSet.getString("titolo");
                    var difficolta = resultSet.getString("difficolta");
                    var costo = resultSet.getDouble("costo");
                    var preview = new EscursionePreview(idEscursione, titolo, difficolta,
                                                         costo);
                    previews.add(preview);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return previews;
        }
        public static List<EscursionePreview> listByGuida(Connection connection, String guidaCF) {
            var lista = new ArrayList<EscursionePreview>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.ESCURSIONI_PER_GUIDA, guidaCF);
                var resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    lista.add(new EscursionePreview(
                        resultSet.getString("ID_escursione"),
                        resultSet.getString("titolo"),
                        resultSet.getString("difficolta"),
                        resultSet.getDouble("costo")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return lista;
        }
    }
}
