package GestioneGioco;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import GestioneCarte.Carta;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GiocoController implements Initializable {

    @FXML
    private Button BottonePesca;

    @FXML
    private ImageView CartaCoperta;

    @FXML
    private ImageView CartaScoperta;

    @FXML
    private Button BottoneFermati;

    private Tavolo tavolo;
    ArrayList<Giocatore> giocatori;
    int turnoCorrente;
    Giocatore giocatoreCorrente;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        inizializzaVista();

    }

    private void inizializzaVista() {
        String pathName = "CarteImmagini/Retro.png";

        CartaCoperta.setImage(new Image(Carta.class.getResourceAsStream(pathName)));

    }

    public void setTavolo(Tavolo t) {
        tavolo = t;
        giocatori = new ArrayList<>(tavolo.punteggi.keySet());
        turnoCorrente = 0;
    }

    public void eseguiPartita() {
        while (!PartitaTerminata()) {
            eseguiTurno(turnoCorrente);
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

            // La prima volta il giocatore è obbligato a pescare
            pesca = ControlloGiocatore.decisioneObbligata(giocatore); // restituisce sempre true
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
                return punteggioParziale;
            }

            pesca = ControlloGiocatore.decisione(giocatore);
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
                    return punteggioParziale;
                }

                pesca = ControlloGiocatore.decisione(giocatore);
            }

            System.out.println("Hai salvato il tuo punteggio: " + (giocatore.punteggio + punteggioParziale));
            System.out.println("Turno di " + giocatore.nome + " terminato");
            System.out.println("---------------------------------------- \n");
            return giocatore.punteggio + punteggioParziale;
        }
    }
}


