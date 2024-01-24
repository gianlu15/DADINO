import java.util.HashMap;
import java.util.Map;

import GestioneCarte.Mazzo;

public class Tavolo {

    Mazzo mazzoDiGioco;
    int punteggioParziale;
    Map<Giocatore, Integer> punteggi = new HashMap<>(); //Memorizza l'associazione Giocatore-punteggio
    

    //Costruttore
    public Tavolo(){
        mazzoDiGioco = new Mazzo();
        punteggioParziale = 0;
        punteggi = new HashMap<>();
    }

    //Aggiungo un nuovo giocatore con il suo punteggio totale (0)
    public void nuovoPunteggio(Giocatore g){
        punteggi.put(g, 0);
    }

    public void aggiornaPunteggio(Giocatore giocatore, int punteggioPartita){
        
        if(punteggi.containsKey(giocatore)){
            int punteggioAttuale = punteggi.get(giocatore);
            int nuovoPunteggio = punteggioAttuale + punteggioPartita;
            punteggi.put(giocatore, nuovoPunteggio);
    
        } else {
            // Il giocatore non è presente nella mappa dei punteggi, gestisci l'errore o aggiungi il giocatore
            System.out.println("Errore: Il giocatore non è presente nella mappa dei punteggi.");
        }
    }
    


    /*QUESTA CLASSE NON VA FATTA QUI */
    public class Giocatore{

    }

    
}
