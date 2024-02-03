
import GestioneGioco.Esecuzione;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;
public class TestPartita {

    public static void main(String[] args) {

        Tavolo t = new Tavolo();
        Giocatore g1 = new Giocatore("LORENZO");
        Giocatore g2 = new Giocatore("GIANLUCA");
        Giocatore g3 = new Giocatore("PAPA'");
        Giocatore g4 = new Giocatore("MAMMA");

        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g2);
        t.nuovoPunteggio(g3);
        t.nuovoPunteggio(g4);

        t.stampaOrdine();

        Esecuzione e = new Esecuzione(t);

        e.eseguiPartita();

    }
}
