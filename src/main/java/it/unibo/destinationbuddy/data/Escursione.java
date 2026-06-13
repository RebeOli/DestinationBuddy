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

    public Escursione(String idEscursione, String titolo, String difficolta, double costo, int postiDisponibili,
            LocalDate dataAperturaEscursione, LocalDate dataChiusuraEscursione, List<Certificazione> certificazioniRichieste) {
        this.idEscursione = idEscursione == null ? "" : idEscursione;
        this.titolo = titolo == null ? "" : titolo;
        this.difficolta = difficolta == null ? "" : difficolta;
        this.costo = costo;
        this.postiDisponibili = postiDisponibili;
        this.dataAperturaEscursione = dataAperturaEscursione;
        this.dataChiusuraEscursione = dataChiusuraEscursione;
        this.certificazioniRichieste = certificazioniRichieste;
    }




}
