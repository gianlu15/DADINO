public class TestPartita {
    
    public static void main(String[] args){

        Tavolo t = new Tavolo();
        Giocatore g1 = new Giocatore("CICCIO");
        Giocatore g2 = new Giocatore("PIPPUS");

        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g2);

        Esecuzione e = new Esecuzione(t);

        e.eseguiPartita();
 
    }
}
