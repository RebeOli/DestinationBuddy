package it.unibo.destinationbuddy.model;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import java.util.List;

public interface CertificazioniModel {

    List<Certificazione> getCertificazioniUtente(String cf);

    List<Certificazione> getCertificazioniInAttesa(); //per amministratore

    List<TipologiaCertificazione> getTipologieDisponibili(); //per guida quando crea escursione

    void aggiungiCertificazione(Certificazione c);

    void validaCertificazione(String nCertificazione); //per amministratore
}
