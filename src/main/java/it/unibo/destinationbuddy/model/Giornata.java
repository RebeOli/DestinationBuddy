package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Giornata {

    private String idEscursione;
    private LocalDate data;
    private String programma;

    public Giornata() {
    }

    public Giornata(String idEscursione, LocalDate data, String programma) {
        this.idEscursione = idEscursione;
        this.data = data;
        this.programma = programma;
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

    public String getProgramma() {
        return programma;
    }

    public void setProgramma(String programma) {
        this.programma = programma;
    }

    @Override
    public String toString() {
        return "Giornata {" +
                "Escursione ID='" + idEscursione + '\'' +
                ", Data=" + data +
                ", Programma='" + programma + '\'' +
                '}';
    }
}