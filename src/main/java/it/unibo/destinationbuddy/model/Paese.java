package it.unibo.destinationbuddy.model;

public class Paese {
    private String nome;

    public Paese() {
    }

    public Paese(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Paese [nome=" + nome + "]";
    }

    
}