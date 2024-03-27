package com.example.GestioneTutorial;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.GestioneCarte.Carta;
import com.example.GestioneGiocatori.Bot;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGioco.Regole;

public class TutorialController {

    @FXML
    private Label Giocatore0;

    @FXML
    private Label punteggioGiocatore0;

    @FXML
    private Label Giocatore1;

    @FXML
    private Label punteggioGiocatore1;

    @FXML
    private Label fraseTurno;

    @FXML
    private Label giocatoreTurno;

    @FXML
    private Button BottonePesca;

    @FXML
    private ImageView CartaCoperta;

    @FXML
    private ImageView CartaScoperta;

    @FXML
    private Button BottoneFermati;

    @FXML
    private Pane visualizzazionePiccola;

    @FXML
    private Label testoDialogo1;

    @FXML
    private Label PunteggioParziale;

    @FXML
    private Pane visualizzazioneGrande;

    @FXML
    private Label testoDialogo;

    @FXML
    private Button avantiDialogoButton;

    private int indiceDialogo;
    private List<Giocatore> giocatori;
    private List<Integer> punteggi;
    private MazzoTutorial mazzo;
    private int punteggioTurno;

    @FXML
    public void initialize() {
        disabilitaPulsanti();

        preparaPartita();

        TestiDialogo.nuoviDialoghi();
        indiceDialogo = 0;
    }

    @FXML
    public void avantiDialogo(ActionEvent event) {
        if (indiceDialogo < TestiDialogo.getSize())
            testoDialogo.setText(TestiDialogo.getPassaggiDialogo(indiceDialogo));
        indiceDialogo++;

        if (indiceDialogo == 7) {
            cambiaVisualizzazione(false);
            giocaPartita();
        }
    }

    public void cambiaVisualizzazione(boolean valore) {
        if (!valore) {
            visualizzazioneGrande.setVisible(false);
            visualizzazionePiccola.setVisible(true);
        }
    }

    public void disabilitaPulsanti() {
        BottonePesca.setDisable(true);
        BottoneFermati.setDisable(true);
    }

    public void preparaPartita() {
        mazzo = new MazzoTutorial();
        CartaScoperta.setImage(null);
        giocatori = new ArrayList<>();
        punteggi = new ArrayList<>();
        giocatori.add(new Giocatore("Utente"));
        giocatori.add(new Bot("Bot"));
        punteggi.add(0);
        punteggi.add(0);

        Giocatore0.setText(giocatori.get(0).getNome());
        Giocatore1.setText(giocatori.get(1).getNome());
        punteggioGiocatore0.setText("0");
        punteggioGiocatore1.setText("0");
    }

    public void giocaPartita() {
        giocatoreTurno.setText(giocatori.get(0).getNome());
        testoDialogo1.setText("Pesca una carta!");
        BottonePesca.setDisable(false);
        punteggioTurno = 0;

        BottonePesca.setOnAction(event -> {

            punteggioTurno = cartaPescata(punteggioTurno);

            BottonePesca.setDisable(true);
            testoDialogo1.setText("Tenta la fortuna, pesca un'altra carta!");
            BottonePesca.setDisable(false);

            BottonePesca.setOnAction(event2 -> {

                punteggioTurno = cartaPescata(punteggioTurno);

                BottonePesca.setDisable(true);
                testoDialogo1.setText("Ora fermati per salvare il tuo punteggio!");
                BottoneFermati.setDisable(false);

                BottoneFermati.setOnAction(event3 -> {
                    punteggi.set(0, punteggioTurno);
                    punteggioGiocatore0.setText(Integer.toString(punteggi.get(0)));
                    punteggioTurno = 0;
                    BottoneFermati.setDisable(true);
                    attendi();
                    giocaBot();
                });
            });
        });

    }

    public void giocaBot() {
        testoDialogo1.setText("Ora è il turno del Bot!");
        giocatoreTurno.setText(giocatori.get(1).getNome());
    
        giocaBotRicorsivo();
    }
    
    private void giocaBotRicorsivo() {
        attendi();
        Carta cartaPescata = mazzo.pescaCarta();
        punteggioTurno = cartaPescataBot(cartaPescata, punteggioTurno);
    
        if (cartaPescata.getValore() != Carta.Valore.Bombetta && punteggioTurno <= ((Bot) giocatori.get(1)).getPunteggioMinimo()) {
            giocaBotRicorsivo(); // Chiamata ricorsiva se la condizione è soddisfatta
        }
    }
    
    public int cartaPescataBot(Carta pescata, int punteggioTurno) {
        boolean effettoDouble = false;
    
        punteggioTurno = Regole.gestisciEffetto(pescata, punteggioTurno, effettoDouble);
        int punteggio = punteggioTurno + punteggi.get(1);
    
        Platform.runLater(() -> {
            setImmagine(pescata.getImmagine());
            PunteggioParziale.setText(Integer.toString(punteggio));
        });
    
        return punteggioTurno;
    }
    

    public int cartaPescata(int punteggioTurno) {
        boolean effettoDouble = false;
        Carta cartaPescata = mazzo.pescaCarta();
        setImmagine(cartaPescata.getImmagine());

        punteggioTurno = Regole.gestisciEffetto(cartaPescata, punteggioTurno, effettoDouble);

        PunteggioParziale.setText(Integer.toString(punteggioTurno + punteggi.get(0)));
        return punteggioTurno;
    }



    public void setImmagine(Image immagine) {
        CartaScoperta.setImage(immagine);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(CartaScoperta);
        rotate.setDuration(Duration.millis(500));
        rotate.setByAngle(360);
        rotate.play();
    }

    public void attendi() {
        try {
            // Attendi 2 secondi
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.err.println("#5 Il thread è stato interrotto durante l'attesa.\n");
            System.exit(1);
        }
    }

}
