package GestioneGioco;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class ControlloGiocatore {

    private static final int PUNTEGGIO_MINIMO_BOT = 17;

    public static boolean decisione(Giocatore giocatore) {
        if (giocatore instanceof Bot) {
            return decisioneBot(giocatore);
        } else {
            return decisioneUtente();
        }
    }

    private static boolean decisioneUtente() {
        System.out.println("\nScegli cosa fare: ");
        System.out.println("Inserisci 'p' per pescare e 'f' per fermarti");

        Scanner tastiera = new Scanner(System.in);
        String operazione = tastiera.next();

        switch (operazione) {
            case "p":
                return true;

            case "f":
                System.out.println("Nella prima mano devi per forza pescare!");
                return false;

            default:
                System.out.println("Operazione non riconosciuta");
                return decisoneUtenteObbligata(); // Richiama ricorsivamente per ottenere una risposta valida
        }
    }

    private static boolean decisioneBot(Giocatore bot) {
        try {
            // Attendi 3 secondi (3000 millisecondi)
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // Gestione dell'eccezione (se necessario)
            e.printStackTrace();
        }
        return bot.getPunteggioParziale() < PUNTEGGIO_MINIMO_BOT;
    }

    public static boolean decisioneObbligata(Giocatore giocatore) {
        if (giocatore instanceof Bot) {
            return decisioneBot(giocatore);
        } else {
            return decisoneUtenteObbligata();
        }
    }

    public static boolean decisoneUtenteObbligata() {

        System.out.println("\nScegli cosa fare: ");
        System.out.println("Inserisci 'p' per pescare e 'f' per fermarti");

        Scanner tastiera = new Scanner(System.in);
        String operazione = tastiera.next();

        switch (operazione) {
            case "p":
                return true;

            case "f":
                System.out.println("Nella prima mano devi per forza pescare!");
                return decisoneUtenteObbligata();

            default:
                System.out.println("Operazione non riconosciuta");
                return decisoneUtenteObbligata(); // Richiama ricorsivamente per ottenere una risposta valida
        }
    }
}
