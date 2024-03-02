package GestioneGioco;
//CLASSE provvisoria per il giocatore
import java.io.Serializable;

public class Giocatore implements Serializable {

    String nome;
    int punteggio;

    int carteTotaliPescate;
    int puntiTotaliFatti;
    int bombettePescate;
    int partiteGiocate;
    int vittorie;

    public Giocatore(String nome){
        this.nome = nome;
        punteggio = 0;
    }

    public String getNome(){
        return nome;
    }

    public int getPunteggioParziale() {
        return punteggio;
    }
    
}
