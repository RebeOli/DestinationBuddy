package it.unibo.destinationbuddy.model;

public interface PrenotazioniModel {

    boolean haAccessoPrioritario(String cf);

    int getPostiRimanenti(String idEscursione);
    boolean verificaCertificazioni(String idEscursione, String cf);
    boolean confermaPrenotazione(String cf, String idEscursione);

    double getScontoNoleggio(String cf);

    String trovaPezzoDisponibile(String idCategoria);

    boolean noleggiaPezzo(String idPezzo, String idEscursione, String cf, int durataNoleggio);
}
