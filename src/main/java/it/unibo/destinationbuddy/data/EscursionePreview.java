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
    public final int postiDisponibili;

    public EscursionePreview(String idEscursione, String titolo, String difficolta,
                              double costo, int postiDisponibili) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.titolo = titolo == null ? "" : titolo;
        this.difficolta = difficolta == null ? "" : difficolta;
        this.costo = costo;
        this.postiDisponibili = postiDisponibili;
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
                && e.costo == this.costo
                && e.postiDisponibili == this.postiDisponibili;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idEscursione, this.titolo, this.difficolta,
                            this.costo, this.postiDisponibili);
    }

    @Override
    public String toString() {
        return "EscursionePreview{idEscursione=" + idEscursione
            + ", titolo=" + titolo
            + ", difficolta=" + difficolta
            + ", costo=" + costo
            + ", postiDisponibili=" + postiDisponibili + "}";
    }

    public static final class DAO {

        public static List<EscursionePreview> list(Connection connection) {
            var previews = new ArrayList<EscursionePreview>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_ESCURSIONI);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var idEscursione = resultSet.getString("ID_escursione");
                    var titolo = resultSet.getString("titolo");
                    var difficolta = resultSet.getString("difficolta");
                    var costo = resultSet.getDouble("costo");
                    var postiDisponibili = resultSet.getInt("posti_disponibili");
                    var preview = new EscursionePreview(idEscursione, titolo, difficolta,
                                                         costo, postiDisponibili);
                    previews.add(preview);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return previews;
        }
    }
}