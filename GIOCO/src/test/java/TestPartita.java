
import GestioneGioco.Bot;
import GestioneGioco.Esecuzione;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;
import GestioneGiocoFX.StageGioco;
public class TestPartita {

    public static void main(String[] args) {

        Tavolo t = new Tavolo();
        Giocatore g1 = new Giocatore("LORENZO");
        Bot g2 = new Bot("GIANLUCA");

        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g2);


        t.stampaOrdine();

        Esecuzione e = new Esecuzione(t);
        StageGioco.avvio();

        e.eseguiPartita();

    }
}
