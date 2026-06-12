package it.unibo.destinationbuddy.model;

public class LuogoEsplorabile {
    private String nomePaese;
    private String nomeZona;
    private String nome;
    private Double quota;
    private String difficoltaAccesso;
    private String periodoConsigliato;
    private String nomeCategoria;

    public LuogoEsplorabile(){

    }

    public LuogoEsplorabile(String nomePaese, String nomeZona, String nome, Double quota, String difficoltaAccesso,
            String periodoConsigliato, String nomeCategoria) {
        this.nomePaese = nomePaese;
        this.nomeZona = nomeZona;
        this.nome = nome;
        this.quota = quota;
        this.difficoltaAccesso = difficoltaAccesso;
        this.periodoConsigliato = periodoConsigliato;
        this.nomeCategoria = nomeCategoria;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public String getDifficoltaAccesso() {
        return difficoltaAccesso;
    }

    public void setDifficoltaAccesso(String difficoltaAccesso) {
        this.difficoltaAccesso = difficoltaAccesso;
    }

    public String getPeriodoConsigliato() {
        return periodoConsigliato;
    }

    public void setPeriodoConsigliato(String periodoConsigliato) {
        this.periodoConsigliato = periodoConsigliato;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @Override
    public String toString() {
        return "LuogoEsplorabile [nomePaese=" + nomePaese + ", nomeZona=" + nomeZona + ", nome=" + nome + ", quota="
                + quota + ", difficoltaAccesso=" + difficoltaAccesso + ", periodoConsigliato=" + periodoConsigliato
                + ", nomeCategoria=" + nomeCategoria + "]";
    }

    

    

}