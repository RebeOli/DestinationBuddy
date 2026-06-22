package it.unibo.destinationbuddy.model.mocked;

import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import it.unibo.destinationbuddy.model.CertificazioniModel;

import java.time.LocalDate;
import java.util.List;

public class MockedCertificazioniModel implements CertificazioniModel {

    @Override
    public List<Certificazione> getCertificazioniUtente(String cf) {
        return List.of(
            new Certificazione(
                new TipologiaCertificazione("TIP-01", "Alpinismo Base"),
                "CERT-001",
                "CAI",
                LocalDate.of(2023, 5, 10),
                LocalDate.of(2028, 5, 10),
                "Valida",
                cf,
                null
            ),
            new Certificazione(
                new TipologiaCertificazione("TIP-02", "Primo Soccorso"), 
                "CERT-002", 
                "Croce Rossa", 
                LocalDate.of(2024, 1, 15), 
                LocalDate.of(2026, 1, 15), 
                "In Attesa", 
                cf, 
                null
            )
        );
    }

    @Override
    public List<Certificazione> getCertificazioniInAttesa() {
        return List.of(
            new Certificazione(
                new TipologiaCertificazione("TIP-03", "Arrampicata Sportiva"), 
                "CERT-099", 
                "Federazione Arrampicata", 
                LocalDate.of(2024, 6, 1), 
                LocalDate.of(2026, 6, 1), 
                "In Attesa", 
                "RSSMRA80A01H501U", 
                null
            )
        );
    }

    @Override
    public List<TipologiaCertificazione> getTipologieDisponibili() {
        return List.of(
            new TipologiaCertificazione("TIP-01", "Alpinismo Base"),
            new TipologiaCertificazione("TIP-02", "Primo Soccorso"),
            new TipologiaCertificazione("TIP-03", "Arrampicata Sportiva")
        );
    }

    @Override
    public void aggiungiCertificazione(Certificazione c) {
        System.out.println("[MOCK] Nuova certificazione aggiunta. In attesa di validazione.");
    }

    @Override
    public void validaCertificazione(String idCertificazione, String nCertificazione) {
        System.out.println("[MOCK] L'amministratore ha validato con successo la certificazione " + idCertificazione + " - " + nCertificazione);
    }
}