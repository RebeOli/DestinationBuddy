package it.unibo.destinationbuddy.data;

import java.time.LocalDate;
import java.util.List;

public final class Escursione {

    public final String idEscursione;
    public final String titolo;
    public final String difficolta;
    public final double costo;
    public final int postiDisponibili;
    public final LocalDate dataAperturaEscursione;
    public final LocalDate dataChiusuraEscursione;
    public final List<Certificazione> certificazioniRichieste;
    public final String guidaNome;
    public final String guidaCognome;

    public Escursione(String idEscursione, String titolo, String difficolta, double costo, int postiDisponibili,
            LocalDate dataAperturaEscursione, LocalDate dataChiusuraEscursione, List<Certificazione> certificazioniRichieste, String guidaNome, String guidaCognome) {
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
        return true;
    }

    public static final class DAO {
        
    }





}
