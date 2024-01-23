import java.util.Random;

public class Mazzo{

    //Attributi
    private Carta[] carte;
    private int carteNelMazzo;

    //Metodi
    public Mazzo(){
        carte = new Carta[54]; //Il mazzo contiene 52 carte + 2 Jolly (Mazzo Francese)
    }

    public void inizializzaMazzo(){

        Carta.Seme[] semiCarte = Carta.Seme.values(); //Copio l'array dei semi
        carteNelMazzo = 0;

        //Per ogni seme inizializzo i 13 valori delle carte
        for (int i = 0; i < semiCarte.length - 1; i++){ //Faccio -1 per non considerare i Jolly

            Carta.Seme seme = semiCarte[i];

            for (int j = 0; j < 13; j++){
                carte[carteNelMazzo] = new Carta(seme, Carta.Valore.getValore(j));
                carteNelMazzo++;
            }
        }

        //Aggiungo i 2 Jolly
        carte[carteNelMazzo] = new Carta(Carta.Seme.Jolly, Carta.Valore.Jolly);
        carteNelMazzo++;
        carte[carteNelMazzo] = new Carta(Carta.Seme.Jolly, Carta.Valore.Jolly);
        carteNelMazzo++;
    }

    public void mescolaMazzo(){
        int n = carte.length;
        Random random = new Random();

        for (int i = n - 1; i > 0; i--){//Algoritmo "Fisher-Yates Shuffle"
            int j = random.nextInt(i + 1);

            //Scambia le carte in posizione i e j
            Carta temp = carte[i];
            carte[i] = carte[j];
            carte[j] = temp;
        }
    }

    public Carta pescaCarta(){
        if (carteNelMazzo > 0){
            Carta cartaPescata = carte[carteNelMazzo - 1];
            carteNelMazzo--;

            return cartaPescata;

        } else{
            //Gestisci l'eccezione se il mazzo è vuoto
            throw new IllegalStateException("Il mazzo è vuoto.");
        }
    }

    public Mazzo unisciMazzi(Mazzo altroMazzo){
        int numeroCarteTotali = this.carteNelMazzo + altroMazzo.getNumeroCarte();
        Carta[] nuoveCarte = new Carta[numeroCarteTotali];
    
        System.arraycopy(this.carte, 0, nuoveCarte, 0, this.carteNelMazzo);
        System.arraycopy(altroMazzo.carte, 0, nuoveCarte, this.carteNelMazzo, altroMazzo.getNumeroCarte());
    
        Mazzo mazzoUnito = new Mazzo();
        mazzoUnito.carte = nuoveCarte;
        mazzoUnito.carteNelMazzo = numeroCarteTotali;
    
        return mazzoUnito;
    }
    
    public int getNumeroCarte(){
        return carteNelMazzo;
    }

    public String toString(){
        //Sfruttiamo il costrutto StringBuilder di Java
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nel mazzo ci sono ").append(carte.length).append(" carte\n");
        for (Carta carta : carte) {
            stringBuilder.append(carta.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}