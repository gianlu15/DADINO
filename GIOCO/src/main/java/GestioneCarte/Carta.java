package GestioneCarte;

public class Carta{

    /*
     * Per rappresentare le il Colore e il Valore delle carte uso delle enumerazioni.
     * All'interno delle enumerazioni viene usato un array statico per associare dei numeri
     * a tutti i valori dell'enumerazione. In questo modo è possibile ottenere un valore dell'
     * enumerazione in base ad un indice numerico.
     */

    //Eumerazioni
    enum Seme{
        Cuori, Quadri, Fiori, Picche, Jolly;

        private static final Seme[] semi = Seme.values();

        public static Seme getSeme(int i){
            return semi[i];
        }
    }

    enum Valore{
        Asso, Due, Tre, Quattro, Cinque, Sei, Sette, Otto, Nove, Dieci, Jack, Regina, Re, Jolly;

        private static final Valore[] valori = Valore.values();

        public static Valore getValore(int i){
            return valori[i];
        }
    }

    //Attributi
    private final Seme semeCarta;
    private final Valore valoreCarta;

    //Metodi
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

    /* #############################################
    A cosa serve questo metodo?
    public void setValore(Valore valoreScelto){
        this.valoreScelto = valoreScelto;
    }
    ############################################# */ 

    public String toString(){
        return valoreCarta + " di " + semeCarta;
    }
}
