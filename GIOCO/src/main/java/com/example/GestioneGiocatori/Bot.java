package com.example.GestioneGiocatori;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Bot extends Giocatore {


    private static final int PUNTEGGIO_MINIMO_BOT = 17;

    public Bot(String nome) {
        super(nome);
    }

    public Bot(){
        super();
    }

    @JsonIgnore
    public int getPunteggioMinimo(){
        return PUNTEGGIO_MINIMO_BOT;
    }
    
}
