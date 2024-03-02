package GestioneGioco;

import java.util.ArrayList;
import java.util.Map;

import java.util.concurrent.TimeUnit;

import GestioneCarte.Carta;

public class Esecuzione {

    Tavolo tavolo;
    ArrayList<Giocatore> giocatori;
    Giocatore giocatoreCorrente;

    GiocoController controller;

    public Esecuzione(Tavolo tavolo, ArrayList<Giocatore> giocatori, GiocoController controller) {
        this.controller = controller;
        this.tavolo = tavolo;
        this.giocatori = giocatori;
        tavolo.setStats(giocatori.size());
    }

    public void eseguiPartita(int turnoCorrente) {
        while (!PartitaTerminata()) {
            eseguiTurno(turnoCorrente);
            attendi();
            turnoCorrente++;
            if (turnoCorrente == giocatori.size())
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
        int punteggio = tavolo.punteggi.get(g) + p;
        if (punteggio >= Regole.PUNTEGGIO_OBIETTIVO) {
            return true;
        }
        return false;
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
                controller.alertVittoria(entry.getKey());
            }
        }
        tavolo.mostraStats();
    }

    public void attendi() {
        try {
            // Attendi 3 secondi (3000 millisecondi)
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // Gestione dell'eccezione (se necessario)
            e.printStackTrace();
        }
    }

    public int giocaTurno(Giocatore giocatore, int indice) {
        int punteggioParziale = tavolo.punteggi.get(giocatore);
        boolean effettoDouble = false;
        giocatore.punteggio = 0;

        System.out.println("Comando passato al giocatore " + giocatore.nome);
        System.out.println("Ricominci da " + punteggioParziale);
        boolean pesca = false;

        controller.inizioTurno(punteggioParziale, giocatore);

        pesca = descisionePesca(giocatore);

        Carta cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
        tavolo.cartePescate[indice]++;
        controller.setImmagine(cartaPescata.getImmagine());
        System.out.println("------> Hai pescato " + cartaPescata.getValore());

        giocatore.punteggio = Regole.gestisciEffetto(cartaPescata, giocatore.punteggio, effettoDouble);
        System.out.println("Il tuo punteggio è ora " + (punteggioParziale + giocatore.punteggio));

        if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
            System.out.println("Punti doppi attivati!");
            effettoDouble = true;
        }

        if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
            System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
            System.out.println("Turno di " + giocatore.nome + " terminato");
            System.out.println("---------------------------------------- \n");
            controller.aggiornaVistaPunteggioParziale(punteggioParziale);
            controller.disabilitaPunteggio();
            tavolo.bombePescate[indice]++;
            tavolo.puntiTotali[indice] = tavolo.puntiTotali[indice] + giocatore.punteggio;
            return punteggioParziale;
        }

        controller.aggiornaVistaPunteggioParziale((punteggioParziale+giocatore.punteggio));

        if (controlloVittoria(giocatore.punteggio, giocatore))
        return punteggioParziale + giocatore.punteggio;

        pesca = descisionePesca(giocatore);

        while (pesca) {
            cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
            tavolo.cartePescate[indice]++;
            controller.setImmagine(cartaPescata.getImmagine());

            System.out.println("------> Hai pescato " + cartaPescata.getValore());

            giocatore.punteggio = Regole.gestisciEffetto(cartaPescata, giocatore.punteggio, effettoDouble);
            System.out.println("Il tuo punteggio è ora " + (punteggioParziale + giocatore.punteggio));
            System.out.println(tavolo.puntiTotali[indice]);

            if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                System.out.println("Punti doppi attivati!");
                effettoDouble = true;
            }

            if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
                System.out.println("Turno di " + giocatore.nome + " terminato");
                System.out.println("---------------------------------------- \n");
                controller.aggiornaVistaPunteggioParziale(punteggioParziale);
                controller.disabilitaPunteggio();
                tavolo.bombePescate[indice]++;
                tavolo.puntiTotali[indice] = tavolo.puntiTotali[indice] + giocatore.punteggio;
                return punteggioParziale;
            }

            controller.aggiornaVistaPunteggioParziale((punteggioParziale+giocatore.punteggio));

            if (controlloVittoria(giocatore.punteggio, giocatore))
            return punteggioParziale + giocatore.punteggio;

            pesca = descisionePesca(giocatore);
        }

        System.out.println("Hai salvato il tuo punteggio: " + (giocatore.punteggio + punteggioParziale));
        System.out.println("Turno di " + giocatore.nome + " terminato");
        System.out.println("---------------------------------------- \n");
        controller.disabilitaPunteggio();
        tavolo.puntiTotali[indice] = tavolo.puntiTotali[indice] + giocatore.punteggio;
        return giocatore.punteggio + punteggioParziale;
    }

    private boolean descisionePesca(Giocatore giocatore){
        boolean p;
        if (giocatore instanceof Bot) {
            p = decisioneBot((Bot) giocatore);
        } else {
            controller.abilitaPulsanti();
            p = controller.decisioneGiocatore().join();
            controller.disabilitaPulsanti();
        }
        return p;
    }

    private boolean decisioneBot(Bot bot) {
        attendi();
        return bot.getPunteggioParziale() < bot.getPunteggioMinimo();
    }

}
