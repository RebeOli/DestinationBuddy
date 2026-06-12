package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Certificazione {

    private String idCertificazione;
    private String nCertificazione;
    private String enteRilasciante;
    private LocalDate dataRilascio;
    private LocalDate dataScadenza;
    private String statoValidazione;
    private String cf;
    private String guidaCf;

    public Certificazione() {
    }

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