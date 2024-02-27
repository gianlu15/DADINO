package GestioneGioco;

public class Bot extends Giocatore {


    private static final int PUNTEGGIO_MINIMO_BOT = 17;

    public Bot(String nome) {
        super(nome);
    }

    public int getPunteggioMinimo(){
        return PUNTEGGIO_MINIMO_BOT;
    }
    
}
