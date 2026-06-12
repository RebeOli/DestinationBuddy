package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Amministratore extends Persona {

    public Amministratore() {
        super(); // Chiama il costruttore vuoto di Persona
        this.setTipoAmministratore(true);
        this.setTipoUtente(false);
    }

    public Amministratore(String cf, String nome, String cognome, String idAccount, 
                          int escursioniEffettuate, LocalDate dataIscrizione, 
                          LocalDate dataAssunzione, String email, String password, String ruolo) {
        
        // Il metodo super() deve essere SEMPRE la prima riga del costruttore
        super(
            cf, 
            nome, 
            cognome, 
            false,
            true,
            idAccount, 
            escursioniEffettuate, 
            dataIscrizione, 
            dataAssunzione,
            email, 
            password, 
            ruolo
        );
    }

    // METODI SPECIFICI (Opzionali)
    // Qui dentro potete aggiungere funzioni che solo un amministratore può fare nell'applicazione
    public void stampaLogAdmin() {
        System.out.println("🛡️ Accesso Amministratore - Registro di sicurezza per: " + this.getNome() + " " + this.getCognome());
    }

    @Override
    public String toString() {
        return "Amministratore {" +
                "CF='" + getCf() + '\'' +
                ", Nome='" + getNome() + '\'' +
                ", Cognome='" + getCognome() + '\'' +
                ", Ruolo='" + getRuolo() + '\'' +
                ", Email='" + getEmail() + '\'' +
                '}';
    }
}