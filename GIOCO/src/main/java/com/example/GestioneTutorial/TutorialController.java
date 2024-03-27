package com.example.GestioneTutorial;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private Button avanti2;

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
    private boolean effettoDouble;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        disabilitaPulsanti();
        avanti2.setVisible(false);

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

            punteggioTurno = cartaPescata(punteggioTurno, 0);

            BottonePesca.setDisable(true);
            testoDialogo1.setText("Tenta la fortuna, pesca un'altra carta!");
            BottonePesca.setDisable(false);

            BottonePesca.setOnAction(event2 -> {

                punteggioTurno = cartaPescata(punteggioTurno, 0);

                BottonePesca.setDisable(true);
                testoDialogo1.setText("Ora fermati per salvare il tuo punteggio!");
                BottoneFermati.setDisable(false);

                BottoneFermati.setOnAction(event3 -> {
                    punteggi.set(0, punteggioTurno);
                    punteggioGiocatore0.setText(Integer.toString(punteggi.get(0)));
                    BottoneFermati.setDisable(true);
                    giocaBot();
                });
            });
        });
    }

    public void giocaBot() {
        testoDialogo1.setText("Ora è il turno del Bot!");
        setImmagine(null);
        giocatoreTurno.setText(giocatori.get(1).getNome());
        punteggioTurno = 0;
        avanti2.setVisible(true);

        PunteggioParziale.setText(Integer.toString(punteggioTurno + punteggi.get(1)));
        avanti2.setOnAction(event -> {

            punteggioTurno = cartaPescata(punteggioTurno, 1);

            testoDialogo1.setText("Il Bot ha pescato un 10!");

            avanti2.setDisable(false);
            avanti2.setOnAction(event1 -> {

                punteggioTurno = cartaPescata(punteggioTurno, 1);

                avanti2.setDisable(true);
                testoDialogo1.setText("Il Bot ha perso 6 punti!");
                avanti2.setDisable(false);

                avanti2.setOnAction(event2 -> {

                    punteggioTurno = cartaPescata(punteggioTurno, 1);

                    avanti2.setDisable(true);
                    testoDialogo1.setText("Il Bot ha perso tutti i punti!");
                    avanti2.setDisable(false);

                    avanti2.setOnAction(event3 -> {

                        testoDialogo1.setText("Ora tocca di nuovo a te!");
                        avanti2.setDisable(true);
                        avanti2.setVisible(false);
                        giocaPartita2();
                    });
                });
            });
        });
    }

    public void giocaPartita2() {
        giocatoreTurno.setText(giocatori.get(0).getNome());
        PunteggioParziale.setText(Integer.toString(punteggi.get(1)));
        setImmagine(null);

        BottonePesca.setDisable(false);
        punteggioTurno = 0;

        BottonePesca.setOnAction(event -> {

            punteggioTurno = cartaPescata(punteggioTurno, 0);

            BottonePesca.setDisable(true);
            testoDialogo1.setText("Hai attivato i punti doppi!");
            BottonePesca.setDisable(false);

            BottonePesca.setOnAction(event2 -> {

                punteggioTurno = cartaPescata(punteggioTurno, 0);

                BottonePesca.setDisable(true);
                testoDialogo1.setText("Hai vinto!");

                avanti2.setDisable(false);
                avanti2.setVisible(true);

                avanti2.setOnAction(event3 -> {

                    alertVittoria();
                    avanti2.setDisable(true);
                    avanti2.setVisible(false);

                });
            });
        });
    }

    public int cartaPescata(int punteggioTurno, int indice) {
        Carta cartaPescata = mazzo.pescaCarta();
        setImmagine(cartaPescata.getImmagine());

        punteggioTurno = Regole.gestisciEffetto(cartaPescata, punteggioTurno, effettoDouble);

        if (cartaPescata.getValore() == Carta.Valore.DoublePoints)
            effettoDouble = true;

        PunteggioParziale.setText(Integer.toString(punteggioTurno + punteggi.get(indice)));

        if (cartaPescata.getValore() == Carta.Valore.Bombetta)
            PunteggioParziale.setText(Integer.toString(punteggi.get(indice)));
        else
            PunteggioParziale.setText(Integer.toString(punteggioTurno + punteggi.get(indice)));

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

    @FXML
    public void alertVittoria() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Vittoria!");
        alert.setContentText("L'utente ha vinto!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(getClass().getResource("/com/example/Styles/alertStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        alert.setTitle("Tutorial terminato");

        // Rimuovi i button types esistenti
        alert.getButtonTypes().clear();

        // Aggiungi il pulsante "Esci"
        ButtonType esci = new ButtonType("Esci");
        alert.getButtonTypes().add(esci);

        // Imposta l'azione per il pulsante "Esci"
        Button exitButton = (Button) alert.getDialogPane().lookupButton(esci);
        exitButton.setOnAction(e -> {
            // Codice per cambiare scena
            try {
                Parent root = FXMLLoader
                        .load(getClass().getResource("/com/example/GestioneLoginUtente/UtenteBoard.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleSP.css").toExternalForm());

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                primaryStage.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Mostra l'alert
        alert.showAndWait();
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
