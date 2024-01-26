import java.util.HashMap;
import java.util.Map;

import GestioneCarte.Mazzo;

public class Tavolo {

     Mazzo mazzoDiGioco;
     Map<Giocatore, Integer> punteggi; //Memorizza l'associazione Giocatore-punteggio
    

    //Costruttore
    public Tavolo(){
        //DEVO GESTIRE IL CASO IN CUI DEVO FARE 2 MAZZI
        mazzoDiGioco = new Mazzo();
        mazzoDiGioco.inizializzaMazzo();
        mazzoDiGioco.mescolaMazzo();
        punteggi = new HashMap<>();
    }

    //Aggiungo un nuovo giocatore con il suo punteggio totale (0)
    public void nuovoPunteggio(Giocatore g){
        punteggi.put(g, 0);
    }

    public void aggiornaPunteggio(Giocatore giocatore, int punteggioTurno){
        
        if(punteggi.containsKey(giocatore)){
            int punteggioAttuale = punteggi.get(giocatore);
            int nuovoPunteggio = punteggioAttuale + punteggioTurno;
            punteggi.put(giocatore, nuovoPunteggio);
    
        } else {
            // Il giocatore non è presente nella mappa dei punteggi, gestisci l'errore o aggiungi il giocatore
            System.out.println("Errore: Il giocatore non è presente nella mappa dei punteggi.");
        }
    }
}
