package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Persona {
    
    private String cf;
    private String nome;
    private String cognome;
    private boolean tipoUtente;
    private boolean tipoAmministratore;
    private String idAccount;
    private int escursioniEffettuate;
    private LocalDate dataIscrizione;
    private LocalDate dataAssunzione;
    private String email;
    private String password;
    private String ruolo;

    public Persona() {
    }

    public Persona(String cf, String nome, String cognome, boolean tipoUtente, boolean tipoAmministratore, 
                   String idAccount, int escursioniEffettuate, LocalDate dataIscrizione, 
                   LocalDate dataAssunzione, String email, String password, String ruolo) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.tipoUtente = tipoUtente;
        this.tipoAmministratore = tipoAmministratore;
        this.idAccount = idAccount;
        this.escursioniEffettuate = escursioniEffettuate;
        this.dataIscrizione = dataIscrizione;
        this.dataAssunzione = dataAssunzione;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getCf() {
        return cf;
    }
    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public boolean isTipoUtente() {
        return tipoUtente;
    }
    public void setTipoUtente(boolean tipoUtente) {
        this.tipoUtente = tipoUtente;
    }

    public boolean isTipoAmministratore() {
        return tipoAmministratore;
    }
    public void setTipoAmministratore(boolean tipoAmministratore) {
        this.tipoAmministratore = tipoAmministratore;
    }

    public String getIdAccount() {
        return idAccount;
    }
    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public int getEscursioniEffettuate() {
        return escursioniEffettuate;
    }
    public void setEscursioniEffettuate(int escursioniEffettuate) {
        this.escursioniEffettuate = escursioniEffettuate;
    }

    public java.time.LocalDate getDataIscrizione() {
        return dataIscrizione;
    }
    public void setDataIscrizione(java.time.LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public java.time.LocalDate getDataAssunzione() {
        return dataAssunzione;
    }
    public void setDataAssunzione(java.time.LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    public String getRuolo() {
        return ruolo;
    }
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}