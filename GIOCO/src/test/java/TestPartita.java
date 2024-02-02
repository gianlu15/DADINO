import GestioneGioco.Esecuzione;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;

public class TestPartita {
    
    public static void main(String[] args){

        Tavolo t = new Tavolo();
        Giocatore g1 = new Giocatore("PUPA");
        Giocatore g2 = new Giocatore("GIANLUCA");

        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g2);

        Esecuzione e = new Esecuzione(t);

        e.eseguiPartita();
 
    }
}
