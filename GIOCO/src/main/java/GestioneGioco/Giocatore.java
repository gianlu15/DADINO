package GestioneGioco;
//CLASSE provvisoria per il giocatore

public class Giocatore {

    String nome;
    int punteggio;

    public Giocatore(String nome){
        this.nome = nome;
        punteggio = 0;
    }

    public String getNome(){
        return nome;
    }
    
}
