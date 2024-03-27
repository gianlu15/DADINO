package com.example.GestioneTutorial;

import java.util.ArrayList;
import java.util.List;

public class TestiDialogo {

        private static List<String> passaggiDialogo;

        public static void nuoviDialoghi() {
                passaggiDialogo = new ArrayList<>();

                passaggiDialogo.add(
                                "Esistono 4 tipi di carte in SPACCA: \nle carte NORMALI il cui valore numerico viene sommato al punteggio (ad esempio: 7 di quadri).");
                passaggiDialogo.add(
                                "La carta REGINA sottrae 6 punti dal punteggio del turno attuale (nota: se il punteggio attuale è minore di 6 allora il nuovo punteggio sarà 0).");
                passaggiDialogo.add(
                                "La carta RE raddopia il valore delle future carte normali che verranno pescate nel turno attuale.");
                passaggiDialogo
                                .add("Le carte BOMBA azzerano il punteggio del turno e passano la mano al giocatore successivo.");
                passaggiDialogo.add(
                                "Puoi interrompere il tuo turno di pesca cliccando sul bottone FERMATI: il tuo punteggio verrà salvato per essere ripreso al turno succesivo e la mano di gioco passerà al prossimo giocatore.");
                passaggiDialogo.add(
                                "Facciamo una prova! Realizza un totale di 20 punti.");

        }

        public static String getPassaggiDialogo(int indice) {
                return passaggiDialogo.get(indice);
        }

        public static int getSize() {
                return passaggiDialogo.size();
        }
}
