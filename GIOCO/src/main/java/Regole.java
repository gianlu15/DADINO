import GestioneCarte.Carta;

public class Regole {

    public static boolean isBombetta(Carta carta) {
        return carta.getValore() == Carta.Valore.Bombetta;
    }

    public static boolean isMalus(Carta carta) {
        return carta.getValore() == Carta.Valore.Malus;
    }

    public static boolean isDoublePoints(Carta carta) {
        return carta.getValore() == Carta.Valore.DoublePoints;
    }

    public static int gestisciEffetto(Carta carta, Giocatore giocatore, Tavolo tavolo, int punteggioParziale) {
        if (isBombetta(carta)) {
            punteggioParziale = 0;
        } else if (isMalus(carta)) {
            punteggioParziale = (punteggioParziale >= 6) ? punteggioParziale - 6 : 0;
        } else if (isDoublePoints(carta)) {
            // Gestisci l'effetto dei double points
        } else {

            switch (carta.getValore()) {
                case Quattro:
                    punteggioParziale = punteggioParziale + 4;
                    break;

                case Cinque:
                    punteggioParziale = punteggioParziale + 5;
                    break;

                case Sei:
                    punteggioParziale = punteggioParziale + 6;
                    break;

                case Sette:
                    punteggioParziale = punteggioParziale + 7;
                    break;

                case Otto:
                    punteggioParziale = punteggioParziale + 8;
                    break;

                case Nove:
                    punteggioParziale = punteggioParziale + 9;
                    break;

                case Dieci:
                    punteggioParziale = punteggioParziale + 10;
                    break;

                case Jack:
                    punteggioParziale = punteggioParziale + 11;
                    break;

                default:
                    break;
            }

        }

        return punteggioParziale;
    }
}
