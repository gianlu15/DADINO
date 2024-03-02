package GestioneGioco;
import java.util.LinkedHashMap;
import java.util.Map;

import GestioneCarte.Mazzo;

public class Tavolo {

    Mazzo mazzoDiGioco;
    Map<Giocatore, Integer> punteggi; // Memorizza l'associazione Giocatore-punteggio
    int[] cartePescate;
    int[] bombePescate;
    int[] puntiTotali;


    // Costruttore
    public Tavolo() {
        mazzoDiGioco = new Mazzo();
        mazzoDiGioco.inizializzaMazzo();
        mazzoDiGioco.mescolaMazzo();
        punteggi = new LinkedHashMap<>();
    }

    // Aggiungo un nuovo giocatore con il suo punteggio totale (0)
    public void nuovoPunteggio(Giocatore g) {
        punteggi.put(g, 0);
    }

    //Metodo per aggiornare il punteggio a fine del turno
    public void aggiornaPunteggio(Giocatore giocatore, int punteggioTurno) {
        punteggi.put(giocatore, punteggioTurno);
    }

    public void setStats(int grandezza){
        cartePescate = new int[grandezza];
        bombePescate = new int[grandezza];
        puntiTotali = new int [grandezza];
    }


    //------------------- test
    public void mostraStats() {
        System.out.println("Statistiche:");
        System.out.println("Giocatori\tCarte Pescate\tBombe Pescate\tPunti Totali");
    
        for (int i = 0; i < cartePescate.length; i++) {
            System.out.print("Giocatore " + (i + 1) + "\t\t");
            System.out.print(cartePescate[i] + "\t\t");
            System.out.print(bombePescate[i] + "\t\t");
            System.out.println(puntiTotali[i]);
        }
    }
    
    //------------------- test
    public void stampaOrdine(){
        for (Map.Entry<Giocatore, Integer> entry : punteggi.entrySet()) {
            System.out.println("Giocatore: " + entry.getKey().getNome());
        }
    }
}
