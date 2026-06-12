package it.unibo.destinationbuddy.model;

public class Zona {

    private String nomePaese;
    private String nome;
    private String descrizione;

    public Zona() {
    }

    public Zona(String nomePaese, String nome, String descrizione) {
        this.nomePaese = nomePaese;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNomePaese() {
        return nomePaese;
    }

    public void setNomePaese(String nomePaese) {
        this.nomePaese = nomePaese;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}