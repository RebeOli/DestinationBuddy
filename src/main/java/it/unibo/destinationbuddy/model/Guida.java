package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Guida extends Persona {

    private boolean statoAccount;

    public Guida() {
        super();
    }

    public Guida(String cf, String nome, String cognome, boolean tipoUtente, boolean tipoAmministratore, 
                 String idAccount, int escursioniEffettuate, LocalDate dataIscrizione, 
                 LocalDate dataAssunzione, String email, String password, String ruolo, 
                 boolean statoAccount) {

        super(cf, nome, cognome, tipoUtente, tipoAmministratore, idAccount, 
              escursioniEffettuate, dataIscrizione, dataAssunzione, email, password, ruolo);

        this.statoAccount = statoAccount;
    }

    public boolean isStatoAccount() {
        return statoAccount;
    }

    public void setStatoAccount(boolean statoAccount) {
        this.statoAccount = statoAccount;
    }

    @Override
    public String toString() {
        return "Guida {" +
                "CF='" + getCf() + '\'' +
                ", Nome Completo='" + getNome() + " " + getCognome() + '\'' +
                ", Account Attivo=" + statoAccount +
                '}';
    }
}