package com.example.GestioneGiocoTorneoFX;

import com.example.GestioneTornei.Torneo;

import java.io.IOException;

import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestionePartite.Partita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VisualizzaTorneo3Controller {

    @FXML
    private Label giocatore0partita0;

    @FXML
    private Label giocatore2partita0;

    @FXML
    private Label giocatore1partita0;

    @FXML
    private Label giocatore3partita0;

    @FXML
    private Label giocatore0partita1;

    @FXML
    private Label giocatore2partita1;

    @FXML
    private Label giocatore1partita1;

    @FXML
    private Label giocatore3partita1;

    @FXML
    private Label giocatore0partita2;

    @FXML
    private Label giocatore1partita2;

    private Torneo torneoAttivo;
    private Partita[] partite;
    private Partita partitaAttiva;

    public void setTorneo(Torneo torneoAttivo) {
        this.torneoAttivo = torneoAttivo;
        this.partite = torneoAttivo.getPartite();

        this.partitaAttiva = torneoAttivo.selezionaPartita();

        mostraNomiSulloSchema();
        coloraNomiSulloSchema();
    }

    @FXML
    public void giocaPartita(ActionEvent event) throws IOException {
        StageGioco sg = new StageGioco(partitaAttiva, torneoAttivo);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sg.start(primaryStage);
    }

    private void mostraNomiSulloSchema() {
        int numeroPartite = partite.length;

        for (int i = 0; i < numeroPartite; i++) {
            if (partite[i] == null)
                break;
            Partita partita = partite[i];
            int numeroGiocatori = partita.getNumGiocatori();

            for (int j = 0; j < numeroGiocatori; j++) {
                Giocatore giocatore = partita.ottieniGiocatore(j);
                switch (i) {
                    case 0:
                        settaNomeGiocatore(giocatore, j, giocatore0partita0, giocatore1partita0, giocatore2partita0,
                                giocatore3partita0);
                        break;
                    case 1:
                        settaNomeGiocatore(giocatore, j, giocatore0partita1, giocatore1partita1, giocatore2partita1,
                                giocatore3partita1);
                        break;
                    case 2:
                        settaNomeGiocatore(giocatore, j, giocatore0partita2, giocatore1partita2);
                        break;
                }
            }
        }
    }

    private void settaNomeGiocatore(Giocatore giocatore, int index, Label... labels) {
        if (index < labels.length) {
            labels[index].setText(giocatore.getNome());
        }
    }

    private void coloraNomiSulloSchema() {

        int numeroPartite = partite.length;

        for (int i = 0; i < numeroPartite; i++) {
            if (partite[i] == null)
                break;
            Partita partita = partite[i];

            if (partita.equals(partitaAttiva)) {
                int numeroGiocatori = partita.getNumGiocatori();

                for (int j = 0; j < numeroGiocatori; j++) {
                    switch (i) {
                        case 0:
                            coloraNomeGiocatore(j, giocatore0partita0, giocatore1partita0, giocatore2partita0,
                                    giocatore3partita0);
                            break;
                        case 1:
                            coloraNomeGiocatore(j, giocatore0partita1, giocatore1partita1, giocatore2partita1,
                                    giocatore3partita1);
                            break;
                        case 2:
                            coloraNomeGiocatore(j, giocatore0partita2, giocatore1partita2);
                            break;
                    }

                }
                break;
            }
        }

        for (int i = 0; i < numeroPartite; i++) {
            if (partite[i] == null)
                break;
            Partita partita = partite[i];

            if (partita.getStatoPartita() == Partita.Stato.Terminata) {
                int numeroGiocatori = partita.getNumGiocatori();

                for (int j = 0; j < numeroGiocatori; j++) {
                    Giocatore giocatore = partita.ottieniGiocatore(j);
                    switch (i) {
                        case 0:
                            coloraNomeGiocatoreTerminata(giocatore, j, giocatore0partita0, giocatore1partita0,
                                    giocatore2partita0,
                                    giocatore3partita0);
                            break;
                        case 1:
                            coloraNomeGiocatoreTerminata(giocatore, j, giocatore0partita1, giocatore1partita1,
                                    giocatore2partita1,
                                    giocatore3partita1);
                            break;
                    }
                }
            }
        }
    }

    private void coloraNomeGiocatore(int index, Label... labels) {
        if (index < labels.length) {
            labels[index].setTextFill(Color.WHITE);
        }
    }

    private void coloraNomeGiocatoreTerminata(Giocatore giocatore, int index, Label... labels) {
        if (index < labels.length) {
            System.out.println(giocatore.getNome());
            if (giocatore.getVincitore() != null) {
                if (giocatore.getVincitore() == true)
                    labels[index].setTextFill(Color.GREEN);
                else
                    labels[index].setTextFill(Color.RED);
            }
        }
    }
}
