package GestioneGioco;
import java.util.ArrayList;
import java.util.Map;
import GestioneCarte.Carta;

public class Esecuzione {

    Tavolo tavolo;
    ArrayList<Giocatore> giocatori;
    int turnoCorrente;
    int giocatoriTotali;
    Giocatore giocatoreCorrente;

    public Esecuzione(Tavolo tavolo) {
        this.tavolo = tavolo;
        giocatori = new ArrayList<>(tavolo.punteggi.keySet());  //copiamo le chiavi di punteggi nell'arraylist dei giocatori
        turnoCorrente = 0;
        giocatoriTotali = giocatori.size();
    }

    public void eseguiPartita() {
        while (!PartitaTerminata()) {
            eseguiTurno(turnoCorrente);
            turnoCorrente++;
            if (turnoCorrente == giocatoriTotali)
                turnoCorrente = 0;
        }
        dichiaraVincitore();
    }

    public void eseguiTurno(int indiceGiocatore) {
        giocatoreCorrente = giocatori.get(indiceGiocatore);
        Turno nuovoTurno = new Turno(giocatoreCorrente);
        int punteggioTurno = nuovoTurno.giocaTurno();
        tavolo.aggiornaPunteggio(giocatoreCorrente, punteggioTurno);
    }

    private boolean PartitaTerminata() {
        for (Map.Entry<Giocatore, Integer> entry : tavolo.punteggi.entrySet()) {
            int punteggio = entry.getValue();
            if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
                return true;
            }
        }
        return false;
    }

    public void dichiaraVincitore() {
        for (Map.Entry<Giocatore, Integer> entry : tavolo.punteggi.entrySet()) {
            int punteggio = entry.getValue();
            if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
                System.out.println("-------------------------");
                System.out.println("Il giocatore  " + entry.getKey().getNome() + " ha vinto!!!");
                System.out.println("-------------------------");
            }
        }

    }

    public class Turno {

        private Giocatore giocatore;
        private int punteggioParziale;
        private boolean effettoDouble;

        public Turno(Giocatore giocatore) {
            this.giocatore = giocatore;
            this.punteggioParziale = 0;
            this.effettoDouble = false;
        }

        public int giocaTurno() {
            System.out.println("Comando passato al giocatore " + giocatore.nome);
            boolean pesca = false;

            // La prima volta il giocatore è obbligato a pescare
            pesca = ControlloGiocatore.decisoneObbligata(); // restituisce sempre true
            Carta cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
            System.out.println("Hai pescato " + cartaPescata.getValore());

            punteggioParziale = Regole.gestisciEffetto(cartaPescata, punteggioParziale, effettoDouble);
            System.out.println("Il tuo punteggio parziale è " + punteggioParziale);

            if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                System.out.println("Punti doppi attivati!");
                effettoDouble = true;
            }

            if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                System.out.println("Turno di " + giocatore.nome + " terminato");
                System.out.println("---------------------------------------- \n");
                return punteggioParziale;
            }

            pesca = ControlloGiocatore.decisone();
            while (pesca) {
                cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
                System.out.println("Hai pescato " + cartaPescata.getValore());

                punteggioParziale = Regole.gestisciEffetto(cartaPescata, punteggioParziale, effettoDouble);
                System.out.println("Il tuo punteggio parziale è " + punteggioParziale);

                if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                    System.out.println("Punti doppi attivati!");
                    effettoDouble = true;
                }

                if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                    System.out.println("Turno di " + giocatore.nome + " terminato");
                    System.out.println("---------------------------------------- \n");
                    return punteggioParziale;
                }

                pesca = ControlloGiocatore.decisone();
            }

            System.out.println("Hai salvato il tuo punteggio parziale: " + punteggioParziale);
            System.out.println("Turno di " + giocatore.nome + " terminato");
            System.out.println("---------------------------------------- \n");
            return punteggioParziale;
        }
    }
}
