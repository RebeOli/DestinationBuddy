package it.unibo.destinationbuddy.model;

public class TipologiaAbbonamento {

    private double costoMensile;
    private int durata;
    private double scontoNoleggio;
    private String vantaggiAccesso;

    public TipologiaAbbonamento() {
    }

    public TipologiaAbbonamento(double costoMensile, int durata, double scontoNoleggio, String vantaggiAccesso) {
        this.costoMensile = costoMensile;
        this.durata = durata;
        this.scontoNoleggio = scontoNoleggio;
        this.vantaggiAccesso = vantaggiAccesso;
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

    public double getScontoNoleggio() {
        return scontoNoleggio;
    }

    public void setScontoNoleggio(double scontoNoleggio) {
        this.scontoNoleggio = scontoNoleggio;
    }

    public String getVantaggiAccesso() {
        return vantaggiAccesso;
    }

    public void setVantaggiAccesso(String vantaggiAccesso) {
        this.vantaggiAccesso = vantaggiAccesso;
    }

    @Override
    public String toString() {
        return "TipologiaAbbonamento [costoMensile=" + costoMensile + ", durata=" + durata + ", scontoNoleggio="
                + scontoNoleggio + ", vantaggiAccesso=" + vantaggiAccesso + "]";
    }
}