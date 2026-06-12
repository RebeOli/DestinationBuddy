package it.unibo.destinationbuddy.model;

public class Recensione {
    private String titolo;
    private String CF;
    private int voto;
    private String immagini;
    private String descrizione;
    private String statoRecensione;
    private String idEscursione;

    public Recensione() {
    }

    public Recensione(String titolo, String CF, int voto, String immagini,
                      String descrizione, String statoRecensione, String idEscursione) {
        this.titolo = titolo;
        this.CF = CF;
        this.voto = voto;
        this.immagini = immagini;
        this.descrizione = descrizione;
        this.statoRecensione = statoRecensione;
        this.idEscursione = idEscursione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCF() {
        return CF;
    }

    public void setCF(String cF) {
        CF = cF;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public String getImmagini() {
        return immagini;
    }

    public void setImmagini(String immagini) {
        this.immagini = immagini;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getStatoRecensione() {
        return statoRecensione;
    }

    public void setStatoRecensione(String statoRecensione) {
        this.statoRecensione = statoRecensione;
    }

    public String getIdEscursione() {
        return idEscursione;
    }

    public void setIdEscursione(String idEscursione) {
        this.idEscursione = idEscursione;
    }

    @Override
    public String toString() {
        return "Recensione [titolo=" + titolo + ", CF=" + CF + ", voto=" + voto + ", immagini=" + immagini
                + ", descrizione=" + descrizione + ", statoRecensione=" + statoRecensione + ", idEscursione="
                + idEscursione + "]";
    }

}