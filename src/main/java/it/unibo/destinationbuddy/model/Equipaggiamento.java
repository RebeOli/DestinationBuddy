package it.unibo.destinationbuddy.model;

public class Equipaggiamento {

    private String idCategoria;
    private double costoTotaleGiornaliero;

    public Equipaggiamento() {
    }

    public Equipaggiamento(String idCategoria, double costoTotaleGiornaliero) {
        this.idCategoria = idCategoria;
        this.costoTotaleGiornaliero = costoTotaleGiornaliero;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public double getCostoTotaleGiornaliero() {
        return costoTotaleGiornaliero;
    }

    public void setCostoTotaleGiornaliero(double costoTotaleGiornaliero) {
        this.costoTotaleGiornaliero = costoTotaleGiornaliero;
    }

    @Override
    public String toString() {
        return "Equipaggiamento {" +
                "Categoria='" + idCategoria + '\'' +
                ", Costo Giornaliero=" + costoTotaleGiornaliero + "€" +
                '}';
    }
}