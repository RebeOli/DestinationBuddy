package it.unibo.destinationbuddy.model;

public class CondizioneMeteo {

    private String nomePaese;
    private String nomeZona;
    private String nomeLuogo;
    private String stagione;

    private double temperaturaMedia;
    private double precipitazioni;

    public CondizioneMeteo() {
    }

    public CondizioneMeteo(String nomePaese, String nomeZona, String nomeLuogo, String stagione, 
                           double temperaturaMedia, double precipitazioni) {
        this.nomePaese = nomePaese;
        this.nomeZona = nomeZona;
        this.nomeLuogo = nomeLuogo;
        this.stagione = stagione;
        this.temperaturaMedia = temperaturaMedia;
        this.precipitazioni = precipitazioni;
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

    public String getStagione() {
        return stagione;
    }

    public void setStagione(String stagione) {
        this.stagione = stagione;
    }

    public double getTemperaturaMedia() {
        return temperaturaMedia;
    }

    public void setTemperaturaMedia(double temperaturaMedia) {
        this.temperaturaMedia = temperaturaMedia;
    }

    public double getPrecipitazioni() {
        return precipitazioni;
    }

    public void setPrecipitazioni(double precipitazioni) {
        this.precipitazioni = precipitazioni;
    }

    @Override
    public String toString() {
        return "CondizioneMeteo {" +
                "Luogo='" + nomeLuogo + " (" + nomeZona + ", " + nomePaese + ")'" +
                ", Stagione='" + stagione + '\'' +
                ", Temp. Media=" + temperaturaMedia + "°C" +
                ", Precipitazioni=" + precipitazioni + " mm" +
                '}';
    }
}