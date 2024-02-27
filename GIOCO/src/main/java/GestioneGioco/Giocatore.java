package GestioneGioco;
//CLASSE provvisoria per il giocatore

public class Giocatore {

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
