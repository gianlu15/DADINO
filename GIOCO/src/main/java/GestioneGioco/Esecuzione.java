package GestioneGioco;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;

import GestioneCarte.Carta;

public class Esecuzione implements Serializable {

    Tavolo tavolo;
    ArrayList<Giocatore> giocatori;
    Giocatore giocatoreCorrente;
    int codice;
    int turnoCorrente;

    @JsonIgnore
    GiocoController controller;

    public Esecuzione(Tavolo tavolo, ArrayList<Giocatore> giocatori) {
        this.tavolo = tavolo;
        this.giocatori = giocatori;
        this.codice = tavolo.getCodice();
        this.turnoCorrente = 0;
        // tavolo.setStats(giocatori.size());
    }

    public Esecuzione() {
    }

    public void setGiocatori(ArrayList<Giocatore> giocatori){
        this.giocatori = giocatori;
    }

    public void setController(GiocoController controller) {
        this.controller = controller;
    }

    public void eseguiPartita() {
        Thread.currentThread();
        while (!PartitaTerminata() || Thread.interrupted()) {
            // tavolo.turniTotali++;
            eseguiTurno(turnoCorrente);
            turnoCorrente++;
            attendi();
            if (turnoCorrente >= giocatori.size())
                turnoCorrente = 0;
        }
        dichiaraVincitore();
    }

    public void eseguiTurno(int indiceGiocatore) {
        giocatoreCorrente = giocatori.get(indiceGiocatore);
        int punteggioTurno = giocaTurno(giocatoreCorrente, indiceGiocatore);
        tavolo.aggiornaPunteggio(giocatoreCorrente, punteggioTurno);
        controller.aggiornaVistaPunteggio(giocatoreCorrente, punteggioTurno);
    }

    private boolean controlloVittoria(int p, Giocatore g) {
        int punteggio = tavolo.getPunteggi().get(g) + p;
        if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
            return true;
        }
        return false;
    }

    private boolean PartitaTerminata() {
        for (Entry<Giocatore, Integer> entry : tavolo.getPunteggi().entrySet()) {
            int punteggio = entry.getValue();
            if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
                return true;
            }
        }
        return false;
    }

    public void dichiaraVincitore() {
        for (Entry<Giocatore, Integer> entry : tavolo.getPunteggi().entrySet()) {
            int punteggio = entry.getValue();
            if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
                System.out.println("-------------------------");
                System.out.println("Il giocatore  " + entry.getKey() + " ha vinto!!!");
                System.out.println("-------------------------");
                controller.alertVittoria(entry.getKey());
            }
        }
        // tavolo.mostraStats();
        // tavolo.finePartita();
    }

    public void attendi() {
        try {
            // Attendi 3 secondi (3000 millisecondi)
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.err.println("Il thread è stato interrotto durante l'attesa.");
            System.exit(1);
        }
    }

    public int giocaTurno(Giocatore giocatore, int indice) {
        int punteggioParziale = tavolo.getPunteggi().get(giocatore);
        boolean effettoDouble = false;
        int punteggioTurno = 0;

        System.out.println("Comando passato al giocatore " + giocatore.getNome());
        System.out.println("Ricominci da " + punteggioParziale);
        boolean pesca = false;

        controller.inizioTurno(punteggioParziale, giocatore);

        pesca = descisionePescaObbligata(giocatore, punteggioTurno); // Dovrebbe essere obbligata

        Carta cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
        // tavolo.cartePescate[indice]++;
        controller.setImmagine(cartaPescata.getImmagine());
        System.out.println("------> Hai pescato " + cartaPescata.getValore());

        punteggioTurno = Regole.gestisciEffetto(cartaPescata, punteggioTurno, effettoDouble);
        System.out.println("Il tuo punteggio è ora " + (punteggioParziale + punteggioTurno));

        if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
            System.out.println("Punti doppi attivati!");
            effettoDouble = true;
        }

        if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
            System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
            System.out.println("Turno di " + giocatore.getNome() + " terminato");
            System.out.println("---------------------------------------- \n");
            controller.aggiornaVistaPunteggioParziale(punteggioParziale);
            controller.disabilitaPunteggio();
            // tavolo.bombePescate[indice]++;
            // tavolo.puntiTotali[indice] += giocatore.getPunteggio();
            return punteggioParziale;
        }

        controller.aggiornaVistaPunteggioParziale((punteggioParziale + punteggioTurno));

        if (controlloVittoria(punteggioTurno, giocatore)) {
            // tavolo.puntiTotali[indice] += giocatore.getPunteggio();
            return punteggioParziale + punteggioTurno;
        }

        pesca = descisionePesca(giocatore, punteggioTurno);

        while (pesca) {
            cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
            // tavolo.cartePescate[indice]++;
            controller.setImmagine(cartaPescata.getImmagine());

            System.out.println("------> Hai pescato " + cartaPescata.getValore());

            punteggioTurno = Regole.gestisciEffetto(cartaPescata, punteggioTurno, effettoDouble);
            System.out.println("Il tuo punteggio è ora " + (punteggioParziale + punteggioTurno));
            // System.out.println(tavolo.puntiTotali[indice]);

            if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                System.out.println("Punti doppi attivati!");
                effettoDouble = true;
            }

            if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
                System.out.println("Turno di " + giocatore.getNome() + " terminato");
                System.out.println("---------------------------------------- \n");
                controller.aggiornaVistaPunteggioParziale(punteggioParziale);
                controller.disabilitaPunteggio();
                // tavolo.bombePescate[indice]++;
                // tavolo.puntiTotali[indice] += giocatore.getPunteggio();
                return punteggioParziale;
            }

            controller.aggiornaVistaPunteggioParziale((punteggioParziale + punteggioTurno));

            if (controlloVittoria(punteggioTurno, giocatore)) {
                // tavolo.puntiTotali[indice] += giocatore.getPunteggio();
                return punteggioParziale + punteggioTurno;
            }

            pesca = descisionePesca(giocatore, punteggioTurno);
        }

        System.out.println("Hai salvato il tuo punteggio: " + (punteggioTurno + punteggioParziale));
        System.out.println("Turno di " + giocatore.getNome() + " terminato");
        System.out.println("---------------------------------------- \n");
        controller.disabilitaPunteggio();
        // tavolo.puntiTotali[indice] += giocatore.getPunteggio();
        return punteggioTurno + punteggioParziale;
    }

    private boolean descisionePesca(Giocatore giocatore, int punteggioTurno) {
        boolean p;
        if (giocatore instanceof Bot) {
            p = decisioneBot((Bot) giocatore, punteggioTurno);
        } else {
            controller.abilitaPulsanti();
            p = controller.decisioneGiocatore().join();
            controller.disabilitaPulsanti();
        }
        return p;
    }

    private boolean descisionePescaObbligata(Giocatore giocatore, int punteggioTurno) {
        boolean p;
        if (giocatore instanceof Bot) {
            p = decisioneBot((Bot) giocatore, punteggioTurno);
        } else {
            controller.abilitaPesca();
            p = controller.decisioneObbligataGiocatore().join();
            controller.disabilitaPesca();
        }
        return p;
    }

    private boolean decisioneBot(Bot bot, int punteggioTurno) {
        attendi();
        return punteggioTurno < bot.getPunteggioMinimo();
    }

    public int getCodice() {
        return codice;
    }

    public int getTurnoCorrente() {
        return turnoCorrente;
    }

    public Tavolo getTavolo(){
        return tavolo;
    }
}
