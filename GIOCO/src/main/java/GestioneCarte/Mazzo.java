package GestioneCarte;

import java.util.Random;

public class Mazzo {

    // Attributi
    private Carta[] carte;
    private int carteNelMazzo;
    private int bombetteNelMazzo;

    // Metodi
    public Mazzo() {
        carte = new Carta[52]; // Il mazzo contiene 52 carte
    }

    public void inizializzaMazzo() {

        Carta.Seme[] semiCarte = Carta.Seme.values(); // Copio l'array dei semi
        carteNelMazzo = 0;

        // Per ogni seme inizializzo i 13 valori delle carte
        for (int i = 0; i < semiCarte.length - 1; i++) { // Faccio -1 per non considerare il seme Special

            Carta.Seme seme = semiCarte[i];

            for (int j = 1; j < 9; j++) {
                carte[carteNelMazzo] = new Carta(seme, Carta.Valore.getValore(j));
                carteNelMazzo++;
            }
        }

        // Aggiungo le 12 bombette
        for (int i = 0; i < 12; i++) {
            carte[carteNelMazzo] = new Carta(Carta.Seme.Special, Carta.Valore.Bombetta);
            carteNelMazzo++;
            bombetteNelMazzo++;
        }

        // Aggiungo i 4 Malus
        for (int i = 0; i < 4; i++) {
            carte[carteNelMazzo] = new Carta(Carta.Seme.Special, Carta.Valore.Malus);
            carteNelMazzo++;
        }

        // Aggiungo i 4 DoublePoints
        for (int i = 0; i < 4; i++) {
            carte[carteNelMazzo] = new Carta(Carta.Seme.Special, Carta.Valore.DoublePoints);
            carteNelMazzo++;
        }
    }

    public void mescolaMazzo() {
        int n = carte.length;
        Random random = new Random();

        for (int i = n - 1; i > 0; i--) {// Algoritmo "Fisher-Yates Shuffle"
            int j = random.nextInt(i + 1);

            // Scambia le carte in posizione i e j
            Carta temp = carte[i];
            carte[i] = carte[j];
            carte[j] = temp;
        }
    }

    public Carta pescaCarta() {

        Carta cartaPescata = carte[carteNelMazzo - 1];
        carteNelMazzo--;

        if (cartaPescata.valore == Carta.Valore.Bombetta)
            bombetteNelMazzo--;

        if (bombetteNelMazzo == 0) {
            System.out.println("MAZZO RIFORMATO");
            inizializzaMazzo();
            mescolaMazzo();
        }
        return cartaPescata;
    }

    public int getCarteNelMazzo() {
        return carteNelMazzo;
    }

    public Carta[] getCarte(){
        return carte;
    }

    //////// -------------------- test
    public void mostraMazzo() {
        for (int i = 0; i < carte.length; i++) {
            cartaIn(i);
        }
    }

    public void cartaIn(int indice) {
        System.out.println("La carta in " + indice + " ha valore " + carte[indice].getValore() + " e seme "
                + carte[indice].getSeme());
    }

}