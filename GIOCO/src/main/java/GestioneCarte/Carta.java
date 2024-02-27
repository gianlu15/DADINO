package GestioneCarte;
import javafx.scene.image.Image;

public class Carta{

    /*
     * Per rappresentare le il Colore e il Valore delle carte uso delle enumerazioni.
     * All'interno delle enumerazioni viene usato un array statico per associare dei numeri
     * a tutti i valori dell'enumerazione. In questo modo è possibile ottenere un valore dell'
     * enumerazione in base ad un indice numerico.
     */

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
     public final Seme semeCarta;
     public final Valore valoreCarta;

    //Costruttore
    public Carta(Seme semeCarta, Valore valoreCarta){
        this.semeCarta = semeCarta;
        this.valoreCarta = valoreCarta;
    }

    public Seme getSeme(){
        return semeCarta;
    }

    public Valore getValore(){
        return valoreCarta;
    } 

    public String toString(){
        return valoreCarta + " di " + semeCarta;
    }

    public Image getImmagine(){
        String pathName = "CarteImmagini/" + valoreCarta + "_" + semeCarta + ".png";
        return new Image(Carta.class.getResourceAsStream(pathName));
    }
}
