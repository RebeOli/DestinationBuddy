package it.unibo.destinationbuddy.model.mocked;

import it.unibo.destinationbuddy.data.Abbonamento;
import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.model.UtentiModel;

import java.time.LocalDate;
import java.util.Optional;

public class MockedUtentiModel implements UtentiModel {

    @Override
    public Optional<Persona> getPersonaAutenticata(String email, String password) {
        System.out.println("[MOCK] Tentativo di login con email: " + email);
        if (email.contains("admin")) {
            System.out.println("[MOCK] Login effettuato come AMMINISTRATORE.");
            return Optional.of(new Persona(
                "ADMN99Z99Z999Z", "Admin", "Superiore", true, true, // tipoAmministratore = true
                "ACC-ADMIN", 0, LocalDate.of(2020, 1, 1), null, 
                email, password, null
            ));
        }
        
        if (email.contains("guida")) {
            System.out.println("[MOCK] Login effettuato come GUIDA.");
            return Optional.of(new Persona(
                "GDA88Y88Y888Y", "Guida", "Alpina", true, false, 
                "ACC-GUIDA", 50, LocalDate.of(2018, 5, 10), LocalDate.of(2019, 1, 1), // ha la data di assunzione!
                email, password, "Attivo" // ha lo stato dell'account
            ));
        }

        System.out.println("[MOCK] Login effettuato come UTENTE NORMALE.");
        return Optional.of(new Persona(
            "RSSMRA80A01H501U", "Mario", "Rossi", true, false, 
            "ACC-001", 3, LocalDate.now(), null, 
            email, password, null
        ));
    }

    @Override
    public void registraUtente(Persona utente) {
        System.out.println("[MOCK] Registrazione completata con successo per: " + utente.nome + " " + utente.cognome);
    }

    @Override
    public int numeroEscursioniEffettuate(Persona utente) {
        return utente.escursioniEffettuate;
    }

    @Override
    public boolean sottoscriviAbbonamento(double costoMensile, int durata, String cf) {
        System.out.println("[MOCK] Sottoscritto abbonamento per CF: " + cf);
        return true;
    }

    @Override
    public Optional<Abbonamento> getUltimoAbbonamento(Persona utente) {
        return Optional.empty();
    }
}