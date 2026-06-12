package it.unibo.destinationbuddy.model;

public class TipologiaEscursione {

    private String idTipologia;

    public TipologiaEscursione(String idTipologia) {
        this.idTipologia = idTipologia;
    }

    public String getIdTipologia() {
        return idTipologia;
    }

    public void setIdTipologia(String iD_tipologia) {
        idTipologia = iD_tipologia;
    }

    @Override
    public String toString() {
        return "TipologiaEscursione [ID_tipologia=" + idTipologia + "]";
    }
}