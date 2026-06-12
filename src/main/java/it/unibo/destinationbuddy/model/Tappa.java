package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Tappa {

    private String idTappa;
    private int durata;
    private String idEscursione;
    private LocalDate data;
    private String nomePaese;
    private String nomeZona;
    private String nomeLuogo;

    public Tappa() {
    }

    public Tappa(String idTappa, int durata, String idEscursione, LocalDate data, String nomePaese, String nomeZona,
        String nomeLuogo) {

        this.idTappa = idTappa;
        this.durata = durata;
        this.idEscursione = idEscursione;
        this.data = data;
        this.nomePaese = nomePaese;
        this.nomeZona = nomeZona;
        this.nomeLuogo = nomeLuogo;
    }

    public String getIdTappa() {
        return idTappa;
    }

    public void setIdTappa(String idTappa) {
        this.idTappa = idTappa;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getIdEscursione() {
        return idEscursione;
    }

    public void setIdEscursione(String idEscursione) {
        this.idEscursione = idEscursione;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNomePaese() {
        return nomePaese;
    }

    public void setNomePaese(String nomePaese) {
        this.nomePaese = nomePaese;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }

    public String getNomeLuogo() {
        return nomeLuogo;
    }

    public void setNomeLuogo(String nomeLuogo) {
        this.nomeLuogo = nomeLuogo;
    }

    @Override
    public String toString() {
        return "Tappa [idTappa=" + idTappa + ", durata=" + durata + ", idEscursione=" + idEscursione + ", data=" + data
                + ", nomePaese=" + nomePaese + ", nomeZona=" + nomeZona + ", nomeLuogo=" + nomeLuogo + "]";
    }
}