package it.unibo.destinationbuddy.model;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import java.util.List;

public interface CertificazioniModel {

    List<Certificazione> getCertificazioniUtente(String cf);

    List<Certificazione> getCertificazioniInAttesa();

    List<TipologiaCertificazione> getTipologieDisponibili();

    void aggiungiCertificazione(Certificazione c);

    void validaCertificazione(String idCertificazione, String nCertificazione);
}
