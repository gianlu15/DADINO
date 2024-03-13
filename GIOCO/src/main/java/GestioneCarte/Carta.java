package GestioneCarte;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.image.Image;

public class Carta{

    //Eumerazioni
    enum Seme{
        Cuori, Quadri, Fiori, Picche, Special;       //Aggiungo il seme delle carte speciali(Special)

        private static final Seme[] semi = Seme.values();

        public static Seme getSeme(int i){
            return semi[i];
        }
    }

    //Valori che vanno da 4 a JACK (asso-3: bombetta, regina:-6 re:doublepoints)
    public enum Valore{
        Bombetta, Quattro, Cinque, Sei, Sette, Otto, Nove, Dieci, Jack, Malus, DoublePoints;

        private static final Valore[] valori = Valore.values();

        public static Valore getValore(int i){
            return valori[i];
        }
    }

    //Attributi
     public final Seme seme;
     public final Valore valore;

    //Costruttore
    public Carta(Seme seme, Valore valore){
        this.seme = seme;
        this.valore = valore;
    }

    public Carta(){
        this.seme = null;
        this.valore = null;
    }

    public Seme getSeme(){
        return seme;
    }

    public Valore getValore(){
        return valore;
    } 

    public String toString(){
        return valore + " di " + seme;
    }

    @JsonIgnore
    public Image getImmagine(){
        String pathName = "CarteImmagini/" + valore + "_" + seme + ".png";
        return new Image(Carta.class.getResourceAsStream(pathName));
    }
}
