package it.unibo.destinationbuddy.model;

public class Pezzo {
    private String idPezzo;
    private Double costoGiornaliero;
    private boolean disponibilità;
    private String idCategoria;

    public Pezzo(){
    }

    public Pezzo(String idPezzo, Double costoGiornaliero, boolean disponibilità, String idCategoria) {
        this.idPezzo = idPezzo;
        this.costoGiornaliero = costoGiornaliero;
        this.disponibilità = disponibilità;
        this.idCategoria = idCategoria;
    }

    public String getIdPezzo() {
        return idPezzo;
    }

    public void setIdPezzo(String idPezzo) {
        this.idPezzo = idPezzo;
    }

    public Double getCostoGiornaliero() {
        return costoGiornaliero;
    }

    public void setCostoGiornaliero(Double costoGiornaliero) {
        this.costoGiornaliero = costoGiornaliero;
    }

    public boolean isDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilità(boolean disponibilità) {
        this.disponibilità = disponibilità;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "Pezzo [idPezzo=" + idPezzo + ", costoGiornaliero=" + costoGiornaliero + ", disponibilità="
                + disponibilità + ", idCategoria=" + idCategoria + "]";
    }

    

    

}