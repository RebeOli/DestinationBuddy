package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Utente extends Persona {

    public Utente() {
        super(); // Chiama il costruttore vuoto di Persona
        this.setTipoUtente(true);
        this.setTipoAmministratore(false);
    }

    public Utente(String cf, String nome, String cognome, String idAccount, 
                  int escursioniEffettuate, LocalDate dataIscrizione, 
                  String email, String password) {
        
        super(
            cf, 
            nome, 
            cognome, 
            true,
            false,
            idAccount, 
            escursioniEffettuate, 
            dataIscrizione, 
            null, // dataAssunzione: null (un utente normale non ha data di assunzione)
            email, 
            password, 
            null // ruolo: null (un utente normale non ha un ruolo aziendale)
        );
    }

    // METODO TOSTRING OVERRIDE (Utilissimo per i vostri test in console)
    @Override
    public String toString() {
        return "Utente {" +
                "CF='" + getCf() + '\'' +
                ", Nome='" + getNome() + '\'' +
                ", Cognome='" + getCognome() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Escursioni Effettuate=" + getEscursioniEffettuate() +
                '}';
    }
}