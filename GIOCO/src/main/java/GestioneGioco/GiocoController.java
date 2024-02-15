package GestioneGioco;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import GestioneCarte.Carta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GiocoController implements Initializable {

    @FXML
    private Button BottonePesca;

    @FXML
    private Label fraseTurno;

    @FXML
    private Label giocatoreTurno;

    @FXML
    private ImageView CartaCoperta;

    @FXML
    private ImageView CartaScoperta;

    @FXML
    private Button BottoneFermati;

    @FXML
    private Label PunteggioParziale;

    private Tavolo tavolo;
    ArrayList<Giocatore> giocatori;
    int turnoCorrente;
    Giocatore giocatoreCorrente;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        String pathName = "CarteImmagini/Retro.png";

        CartaCoperta.setImage(new Image(Carta.class.getResourceAsStream(pathName)));

        disabilitaPulsanti();
        PunteggioParziale.setVisible(false);
        fraseTurno.setVisible(false);
        giocatoreTurno.setVisible(false);
    }

    public void setTavolo(Tavolo t) {
        tavolo = t;
        giocatori = new ArrayList<>(tavolo.punteggi.keySet());
        turnoCorrente = 0;
    }

    public void disabilitaPulsanti() {
        BottonePesca.setDisable(true);
        BottoneFermati.setDisable(true);
    }

    public void abilitaPulsanti() {
        BottonePesca.setDisable(false);
        BottoneFermati.setDisable(false);
    }

    public void eseguiPartita() {
        while (!PartitaTerminata()) {
            CartaScoperta.setImage(null);
            eseguiTurno(turnoCorrente);
            try {
                // Attendi 3 secondi (3000 millisecondi)
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // Gestione dell'eccezione (se necessario)
                e.printStackTrace();
            }
            turnoCorrente++;
            if (turnoCorrente == giocatori.size())
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
            }
        }

    }

    public class Turno {

        private Giocatore giocatore;
        private int punteggioParziale;
        private boolean effettoDouble;

        public Turno(Giocatore giocatore) {
            this.giocatore = giocatore;
            this.punteggioParziale = tavolo.punteggi.get(giocatore);
            this.effettoDouble = false;
            giocatore.punteggio = 0;
        }

        public int giocaTurno() {
            System.out.println("Comando passato al giocatore " + giocatore.nome);
            System.out.println("Ricominci da " + punteggioParziale);
            boolean pesca = false;

            fraseTurno.setVisible(true);
            giocatoreTurno.setVisible(true);
            PunteggioParziale.setVisible(true);
            Platform.runLater(() -> {
                PunteggioParziale.setText(Integer.toString(punteggioParziale));
                giocatoreTurno.setText(giocatore.getNome());
            });

            if (giocatore instanceof Bot) {
                pesca = decisioneBot((Bot) giocatore);
            } else {
                BottonePesca.setDisable(false);
                pesca = decisioneObbligataGiocatore().join();
                BottonePesca.setDisable(true);
            }

            Carta cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
            CartaScoperta.setImage(cartaPescata.getImmagine());
            System.out.println("------> Hai pescato " + cartaPescata.getValore());

            giocatore.punteggio = Regole.gestisciEffetto(cartaPescata, giocatore.punteggio, effettoDouble);
            System.out.println("Il tuo punteggio è ora " + (punteggioParziale + giocatore.punteggio));

            if (controlloVittoria(giocatore.punteggio, giocatore))
                return punteggioParziale + giocatore.punteggio;

            if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                System.out.println("Punti doppi attivati!");
                effettoDouble = true;
            }

            if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
                System.out.println("Turno di " + giocatore.nome + " terminato");
                System.out.println("---------------------------------------- \n");
                PunteggioParziale.setVisible(false);
                return punteggioParziale;
            }

            Platform.runLater(() -> {
                PunteggioParziale.setText(Integer.toString(punteggioParziale + giocatore.punteggio));
            });

            if (giocatore instanceof Bot) {
                pesca = decisioneBot((Bot) giocatore);
            } else {
                abilitaPulsanti();
                pesca = decisioneGiocatore().join();
                disabilitaPulsanti();
            }

            while (pesca) {
                cartaPescata = tavolo.mazzoDiGioco.pescaCarta();
                CartaScoperta.setImage(cartaPescata.getImmagine());

                System.out.println("------> Hai pescato " + cartaPescata.getValore());

                giocatore.punteggio = Regole.gestisciEffetto(cartaPescata, giocatore.punteggio, effettoDouble);
                System.out.println("Il tuo punteggio è ora " + (punteggioParziale + giocatore.punteggio));

                if (controlloVittoria(giocatore.punteggio, giocatore))
                    return punteggioParziale + giocatore.punteggio;

                if (cartaPescata.getValore() == Carta.Valore.DoublePoints) {
                    System.out.println("Punti doppi attivati!");
                    effettoDouble = true;
                }

                if (cartaPescata.getValore() == Carta.Valore.Bombetta) {
                    System.out.println("Il tuo punteggio è tornato a " + punteggioParziale);
                    System.out.println("Turno di " + giocatore.nome + " terminato");
                    System.out.println("---------------------------------------- \n");
                    Platform.runLater(() -> {
                        PunteggioParziale.setText(Integer.toString(punteggioParziale));
                    });
                    PunteggioParziale.setVisible(false);
                    return punteggioParziale;
                }

                Platform.runLater(() -> {
                    PunteggioParziale.setText(Integer.toString(punteggioParziale + giocatore.punteggio));
                });

                if (giocatore instanceof Bot) {
                    pesca = decisioneBot((Bot) giocatore);
                } else {
                    abilitaPulsanti();
                    pesca = decisioneGiocatore().join();
                    disabilitaPulsanti();

                }
            }

            System.out.println("Hai salvato il tuo punteggio: " + (giocatore.punteggio + punteggioParziale));
            System.out.println("Turno di " + giocatore.nome + " terminato");
            System.out.println("---------------------------------------- \n");
            PunteggioParziale.setVisible(false);
            return giocatore.punteggio + punteggioParziale;
        }

        private boolean decisioneBot(Bot bot) {
            try {
                // Attendi 3 secondi (3000 millisecondi)
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                // Gestione dell'eccezione (se necessario)
                e.printStackTrace();
            }
            return bot.getPunteggioParziale() < bot.getPunteggioMinimo();
        }

        private CompletableFuture<Boolean> decisioneGiocatore() {

            // Crea un CompletableFuture che attende l'azione del giocatore
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            // Azione quando viene premuto il pulsante per pescare
            BottonePesca.setOnAction(event -> {
                future.complete(true); // Completa il futuro con true per indicare che il giocatore vuole pescare
            });

            // Azione quando viene premuto il pulsante per fermarsi
            BottoneFermati.setOnAction(event -> {
                future.complete(false); // Completa il futuro con false per indicare che il giocatore si ferma
            });

            try {
                // Attendi 3 secondi (3000 millisecondi)
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // Gestione dell'eccezione (se necessario)
                e.printStackTrace();
            }

            return future; // Restituisci il CompletableFuture per attendere l'azione del giocatore
        }

        private CompletableFuture<Boolean> decisioneObbligataGiocatore() {

            // Crea un CompletableFuture che attende l'azione del giocatore
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            // Azione quando viene premuto il pulsante per pescare
            BottonePesca.setOnAction(event -> {
                future.complete(true); // Completa il futuro con true per indicare che il giocatore vuole pescare
            });

            // Azione quando viene premuto il pulsante per fermarsi
            BottoneFermati.setOnAction(event -> {
                future.complete(false); // Completa il futuro con false per indicare che il giocatore si ferma
            });

            try {
                // Attendi 3 secondi (3000 millisecondi)
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // Gestione dell'eccezione (se necessario)
                e.printStackTrace();
            }

            return future; // Restituisci il CompletableFuture per attendere l'azione del giocatore
        }
    }
}