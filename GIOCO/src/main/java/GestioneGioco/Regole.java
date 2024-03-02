package GestioneGioco;
import GestioneCarte.Carta;

public class Regole {

    final static int PUNTEGGIO_OBIETTIVO = 10;

    public static boolean isBombetta(Carta carta) {
        return carta.getValore() == Carta.Valore.Bombetta;
    }

    public static boolean isMalus(Carta carta) {
        return carta.getValore() == Carta.Valore.Malus;
    }

    public static boolean isDoublePoints(Carta carta) {
        return carta.getValore() == Carta.Valore.DoublePoints;
    }

    public static int gestisciEffetto(Carta carta, int punteggioParziale, boolean effettoDouble) {
        if (isBombetta(carta)) {
            punteggioParziale = 0;
        } else if (isMalus(carta)) {
            punteggioParziale = (punteggioParziale >= 6) ? punteggioParziale - 6 : 0;
        } else {

            switch (carta.getValore()) {
                case Quattro:
                    punteggioParziale = effettoDouble ? punteggioParziale + (4*2) : punteggioParziale + 4;
                    break;

                case Cinque:
                punteggioParziale = effettoDouble ? punteggioParziale + (5*2) : punteggioParziale + 5;
                    break;

                case Sei:
                punteggioParziale = effettoDouble ? punteggioParziale + (6*2) : punteggioParziale + 6;
                    break;

                case Sette:
                punteggioParziale = effettoDouble ? punteggioParziale + (7*2) : punteggioParziale + 7;
                    break;

                case Otto:
                punteggioParziale = effettoDouble ? punteggioParziale + (8*2) : punteggioParziale + 8;
                    break;

                case Nove:
                punteggioParziale = effettoDouble ? punteggioParziale + (9*2) : punteggioParziale + 9;
                    break;

                case Dieci:
                punteggioParziale = effettoDouble ? punteggioParziale + (10*2) : punteggioParziale + 10;
                    break;

                case Jack:
                punteggioParziale = effettoDouble ? punteggioParziale + (11*2) : punteggioParziale + 11;
                    break;

                default:
                    break;
            }

        }

        return punteggioParziale;
    }
}
