import java.util.ArrayList;
import java.util.List;
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
        giocatori = new ArrayList<>(tavolo.punteggi.keySet());
        turnoCorrente = 0;
        giocatoriTotali = giocatori.size();
    }

    public void eseguiPartita() {
       while (turnoCorrente < giocatoriTotali) {
            eseguiTurno(turnoCorrente);
            turnoCorrente++;
            if (turnoCorrente == giocatoriTotali)
                turnoCorrente = 0;
        }
        // dichiaraVincitore();
    }

    public void eseguiTurno(int indiceGiocatore) {
        giocatoreCorrente = giocatori.get(indiceGiocatore);
        Turno nuovoTurno = new Turno(giocatoreCorrente);
        nuovoTurno.giocaTurno();
        // tavolo.aggiornaPunteggio(giocatoreCorrente, punteggioTurno);
    }

    // private boolean PartitaTerminata() {
    //     for (Map.Entry<Giocatore, Integer> entry : tavolo.punteggi.entrySet()) {
    //         int punteggio = entry.getValue();
    //         if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    public void dichiaraVincitore(){
        //..... 
    }

    public class Turno {

        private Giocatore giocatore;
        private int punteggioParziale;

        public Turno(Giocatore giocatore) {
            this.giocatore = giocatore;
            this.punteggioParziale = 0;
        }

        public void giocaTurno() {
            System.out.println("Comando passato al giocatore " + giocatore.nome);
            boolean pesca = true;

            while(pesca){

                pesca = ControlloGiocatore.decisone();
                if(pesca == false)
                break;
                Carta cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
                System.out.println("Hai pescato " + cartaPescata.getValore());
            } 
            System.out.println("Turno di " + giocatore.nome + " terminato");
        }
    }
}
