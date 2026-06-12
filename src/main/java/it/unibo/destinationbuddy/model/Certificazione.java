package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Certificazione {

    private String idCertificazione; // Fa parte della PK e FK verso TIPOLOGIE_CERTIFICAZIONE
    private String nCertificazione;  // Fa parte della PK
    private String enteRilasciante;
    private LocalDate dataRilascio;
    private LocalDate dataScadenza;
    private String statoValidazione;
    private String cf;               // FK verso PERSONE
    private String guidaCf;          // FK verso GUIDE

    // 2. COSTRUTTORE VUOTO (Obbligatorio per JDBC)
    public Certificazione() {
    }

    // 3. COSTRUTTORE COMPLETO
    public Certificazione(String idCertificazione, String nCertificazione, String enteRilasciante, 
                          LocalDate dataRilascio, LocalDate dataScadenza, String statoValidazione, 
                          String cf, String guidaCf) {
        this.idCertificazione = idCertificazione;
        this.nCertificazione = nCertificazione;
        this.enteRilasciante = enteRilasciante;
        this.dataRilascio = dataRilascio;
        this.dataScadenza = dataScadenza;
        this.statoValidazione = statoValidazione;
        this.cf = cf;
        this.guidaCf = guidaCf;
    }

    // 4. METODI GETTER E SETTER
    
    public String getIdCertificazione() {
        return idCertificazione;
    }

    public void setIdCertificazione(String idCertificazione) {
        this.idCertificazione = idCertificazione;
    }

    public String getnCertificazione() {
        return nCertificazione;
    }

    public void setnCertificazione(String nCertificazione) {
        this.nCertificazione = nCertificazione;
    }

    public String getEnteRilasciante() {
        return enteRilasciante;
    }

    public void setEnteRilasciante(String enteRilasciante) {
        this.enteRilasciante = enteRilasciante;
    }

    public LocalDate getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(LocalDate dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getStatoValidazione() {
        return statoValidazione;
    }

    public void setStatoValidazione(String statoValidazione) {
        this.statoValidazione = statoValidazione;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getGuidaCf() {
        return guidaCf;
    }

    public void setGuidaCf(String guidaCf) {
        this.guidaCf = guidaCf;
    }

    // 5. METODO TOSTRING
    @Override
    public String toString() {
        return "Certificazione {" +
                "ID='" + idCertificazione + '\'' +
                ", N°='" + nCertificazione + '\'' +
                ", Ente='" + enteRilasciante + '\'' +
                ", Scadenza=" + dataScadenza +
                ", Validazione='" + statoValidazione + '\'' +
                ", CF Titolare='" + cf + '\'' +
                '}';
    }
}