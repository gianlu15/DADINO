package com.example.GestioneTutorial;

import com.example.GestioneCarte.Carta;

public class MazzoTutorial {

    private Carta[] carte;
    private int carteNelMazzo;


    public MazzoTutorial() {
        carte = new Carta[7];
        carteNelMazzo = carte.length;
        carte[0] = new Carta(Carta.Seme.Cuori, Carta.Valore.Quattro);
        carte[1] = new Carta(Carta.Seme.Special, Carta.Valore.DoublePoints);
        carte[2] = new Carta(Carta.Seme.Special, Carta.Valore.Bombetta);
        carte[3] = new Carta(Carta.Seme.Special, Carta.Valore.Malus);
        carte[4] = new Carta(Carta.Seme.Fiori, Carta.Valore.Dieci);
        carte[5] = new Carta(Carta.Seme.Quadri, Carta.Valore.Cinque);
        carte[6] = new Carta(Carta.Seme.Picche, Carta.Valore.Sette);
    }

    public Carta pescaCarta() {
        Carta cartaPescata = carte[carteNelMazzo - 1];
        carteNelMazzo--;
        return cartaPescata;
    }

    public int getCarteNelMazzo() {
        return carteNelMazzo;
    }

    public Carta[] getCarte(){
        return carte;
    }

}