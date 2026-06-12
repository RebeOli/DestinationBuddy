package it.unibo.destinationbuddy.model;

public class Categoria {

    private String nomeCategoria;

    public Categoria() {
    }

    public Categoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @Override
    public String toString() {
        return "Categoria {" +
                "Nome='" + nomeCategoria + '\'' +
                '}';
    }
}