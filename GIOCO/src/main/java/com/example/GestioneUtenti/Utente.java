package com.example.GestioneUtenti;

public class Utente {

    private String nome;

    public Utente(String nome) {
        this.nome = nome;
    }

    public Utente(){}

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
