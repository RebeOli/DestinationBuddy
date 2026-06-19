package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class Escursione {

    public final String idEscursione;
    public final String titolo;
    public final String difficolta;
    public final double costo;
    public final int postiDisponibili;
    public final LocalDate dataAperturaEscursione;
    public final LocalDate dataChiusuraEscursione;
    public final List<TipologiaCertificazione> certificazioniRichieste;
    public final String guidaNome;
    public final String guidaCognome;
    public final List<Giornata> giornate;
    public final List<Equipaggiamento> equipaggiamento; //non indica i singoli pezzi
    public final List<String> tipologie;


    public Escursione(String idEscursione, String titolo, String difficolta, double costo, int postiDisponibili,
            LocalDate dataAperturaEscursione, LocalDate dataChiusuraEscursione, List<TipologiaCertificazione> certificazioniRichieste, 
            String guidaNome, String guidaCognome, List<Giornata> giornate, List<Equipaggiamento> equipaggiamento, List<String> tipologie) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.titolo = titolo == null ? "" : titolo;
        this.difficolta = difficolta == null ? "" : difficolta;
        this.guidaNome = guidaNome == null ? "" : guidaNome;
        this.guidaCognome = guidaCognome == null ? "" : guidaCognome;
        this.costo = costo;
        this.postiDisponibili = postiDisponibili;
        this.dataAperturaEscursione = dataAperturaEscursione;
        this.dataChiusuraEscursione = dataChiusuraEscursione;
        this.certificazioniRichieste = certificazioniRichieste;
        this.giornate = giornate;
        this.equipaggiamento = equipaggiamento;
        this.tipologie = tipologie;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEscursione == null) ? 0 : idEscursione.hashCode());
        result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
        result = prime * result + ((difficolta == null) ? 0 : difficolta.hashCode());
        long temp;
        temp = Double.doubleToLongBits(costo);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + postiDisponibili;
        result = prime * result + ((dataAperturaEscursione == null) ? 0 : dataAperturaEscursione.hashCode());
        result = prime * result + ((dataChiusuraEscursione == null) ? 0 : dataChiusuraEscursione.hashCode());
        result = prime * result + ((certificazioniRichieste == null) ? 0 : certificazioniRichieste.hashCode());
        result = prime * result + ((guidaNome == null) ? 0 : guidaNome.hashCode());
        result = prime * result + ((guidaCognome == null) ? 0 : guidaCognome.hashCode());
        result = prime * result + ((giornate == null) ? 0 : giornate.hashCode());
        result = prime * result + ((equipaggiamento == null) ? 0 : equipaggiamento.hashCode());
        result = prime * result + ((tipologie == null) ? 0 : tipologie.hashCode());
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
        Escursione other = (Escursione) obj;
        if (idEscursione == null) {
            if (other.idEscursione != null)
                return false;
        } else if (!idEscursione.equals(other.idEscursione))
            return false;
        if (titolo == null) {
            if (other.titolo != null)
                return false;
        } else if (!titolo.equals(other.titolo))
            return false;
        if (difficolta == null) {
            if (other.difficolta != null)
                return false;
        } else if (!difficolta.equals(other.difficolta))
            return false;
        if (Double.doubleToLongBits(costo) != Double.doubleToLongBits(other.costo))
            return false;
        if (postiDisponibili != other.postiDisponibili)
            return false;
        if (dataAperturaEscursione == null) {
            if (other.dataAperturaEscursione != null)
                return false;
        } else if (!dataAperturaEscursione.equals(other.dataAperturaEscursione))
            return false;
        if (dataChiusuraEscursione == null) {
            if (other.dataChiusuraEscursione != null)
                return false;
        } else if (!dataChiusuraEscursione.equals(other.dataChiusuraEscursione))
            return false;
        if (certificazioniRichieste == null) {
            if (other.certificazioniRichieste != null)
                return false;
        } else if (!certificazioniRichieste.equals(other.certificazioniRichieste))
            return false;
        if (guidaNome == null) {
            if (other.guidaNome != null)
                return false;
        } else if (!guidaNome.equals(other.guidaNome))
            return false;
        if (guidaCognome == null) {
            if (other.guidaCognome != null)
                return false;
        } else if (!guidaCognome.equals(other.guidaCognome))
            return false;
        if (giornate == null) {
            if (other.giornate != null)
                return false;
        } else if (!giornate.equals(other.giornate))
            return false;
        if (equipaggiamento == null) {
            if (other.equipaggiamento != null)
                return false;
        } else if (!equipaggiamento.equals(other.equipaggiamento))
            return false;
        if (tipologie == null) {
            if (other.tipologie != null)
                return false;
        } else if (!tipologie.equals(other.tipologie))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Escursione [idEscursione=" + idEscursione + ", titolo=" + titolo + ", difficolta=" + difficolta
                + ", costo=" + costo + ", postiDisponibili=" + postiDisponibili + ", dataAperturaEscursione="
                + dataAperturaEscursione + ", dataChiusuraEscursione=" + dataChiusuraEscursione
                + ", certificazioniRichieste=" + certificazioniRichieste + ", guidaNome=" + guidaNome
                + ", guidaCognome=" + guidaCognome + ", giornate=" + giornate + ", equipaggiamento=" + equipaggiamento
                + ", Tipologie=" + tipologie + "]";
    }


    public static final class DAO {
        public static Optional<Escursione> find(Connection connection, String idEscursione) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.FIND_ESCURSIONE, idEscursione);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var id = resultSet.getString("ID_escursione");
                    var titolo = resultSet.getString("titolo");
                    var difficolta = resultSet.getString("difficolta");
                    var costo = resultSet.getDouble("costo");
                    var postiDisponibili = resultSet.getInt("posti_disponibili");
                    var dataApertura = resultSet.getDate("data_apertura_iscrizione").toLocalDate();
                    var dataChiusura = resultSet.getDate("data_chiusura_iscrizione").toLocalDate();
                    var guidaNome = resultSet.getString("guida_nome");
                    var guidaCognome = resultSet.getString("guida_cognome");

                    var certificazioni = TipologiaCertificazione.DAO.listForEscursione(connection, idEscursione);
                    var giornate = Giornata.DAO.listForEscursione(connection, idEscursione);
                    var equipaggiamenti = Equipaggiamento.DAO.listForEscursione(connection, idEscursione);
                    var tipologie = TipologiaEscursione.DAO.listForEscursione(connection, idEscursione);
                    return Optional.of(new Escursione(id, titolo, difficolta,
                                                         costo, postiDisponibili,dataApertura, 
                                                         dataChiusura, certificazioni, guidaNome, 
                                                         guidaCognome, giornate, equipaggiamenti, tipologie));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return Optional.empty();
        }

        public static void create(Connection connection, Escursione e, String descrizione, int numeroPartecipanti, String guidaCF, List<String> tipologieDaAssociare) {
            try {
                // 1. Inserisce l'escursione base
                try (var statement = DAOUtils.prepare(connection, Queries.INSERISCI_ESCURSIONE,
                    e.idEscursione, e.titolo, descrizione, e.difficolta, numeroPartecipanti,
                    e.costo, java.sql.Date.valueOf(e.dataAperturaEscursione),
                    java.sql.Date.valueOf(e.dataChiusuraEscursione), guidaCF)) {
                    statement.executeUpdate();
                }
                for (String idTipologia : tipologieDaAssociare) {
                    try (var stmtAssume = DAOUtils.prepare(connection, Queries.ASSOCIA_ESCURSIONE_TIPOLOGIA, 
                                                           e.idEscursione, idTipologia)) {
                        stmtAssume.executeUpdate();
                    }
                }
                for (Giornata giornata : e.giornate) {
                    Giornata.DAO.create(connection, giornata); 
                }

            } catch (Exception ex) {
                throw new DAOException(ex);
            }
        }
        
    }
}

