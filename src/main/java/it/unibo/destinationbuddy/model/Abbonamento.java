package it.unibo.destinationbuddy.model;

import java.time.LocalDate;

public class Abbonamento {

    private LocalDate dataAbbonamento;
    private LocalDate dataPagamento;
    private String cf;
    private double costoMensile;
    private int durata;

    public Abbonamento() {
    }

    public Abbonamento(LocalDate dataAbbonamento, LocalDate dataPagamento, String cf, double costoMensile, int durata) {
        this.dataAbbonamento = dataAbbonamento;
        this.dataPagamento = dataPagamento;
        this.cf = cf;
        this.costoMensile = costoMensile;
        this.durata = durata;
    }

    public LocalDate getDataAbbonamento() {
        return dataAbbonamento;
    }

    public void setDataAbbonamento(LocalDate dataAbbonamento) {
        this.dataAbbonamento = dataAbbonamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public double getCostoMensile() {
        return costoMensile;
    }

    public void setCostoMensile(double costoMensile) {
        this.costoMensile = costoMensile;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    // METODO TOSTRING (Utilissimo per stampare i dati in console durante i test)
    @Override
    public String toString() {
        return "Abbonamento{" +
                "CF='" + cf + '\'' +
                ", dataAbbonamento=" + dataAbbonamento +
                ", dataPagamento=" + dataPagamento +
                ", costoMensile=" + costoMensile + "€" +
                ", durata=" + durata + " mesi" +
                '}';
    }
}