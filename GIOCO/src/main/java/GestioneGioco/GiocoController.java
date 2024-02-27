package GestioneGioco;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import GestioneCarte.Carta;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GiocoController implements Initializable {

    @FXML
    private Label Giocatore0;

    @FXML
    private Label punteggioGiocatore0;

    @FXML
    private Label Giocatore1;

    @FXML
    private Label punteggioGiocatore1;

    @FXML
    private Label Giocatore2;

    @FXML
    private Label punteggioGiocatore2;

    @FXML
    private Label Giocatore3;

    @FXML
    private Label punteggioGiocatore3;

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
    Esecuzione e;

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

        e = new Esecuzione(tavolo, giocatori, this);

        setLeaderboard();
    }

    public void setLeaderboard() {

        Giocatore0.setText(giocatori.get(0).getNome());
        punteggioGiocatore0.setText("0");

        Giocatore1.setText(giocatori.get(1).getNome());
        punteggioGiocatore1.setText("0");

        Giocatore2.setText("");
        punteggioGiocatore2.setText("");

        Giocatore3.setText("");
        punteggioGiocatore3.setText("");

        if (giocatori.size() == 3) {
            Giocatore2.setText(giocatori.get(2).getNome());
            punteggioGiocatore2.setText("0");
        }

        if (giocatori.size() == 4) {
            Giocatore3.setText(giocatori.get(3).getNome());
            punteggioGiocatore3.setText("0");
        }
    }

    public void disabilitaPulsanti() {
        BottonePesca.setDisable(true);
        BottoneFermati.setDisable(true);
    }

    public void abilitaPulsanti() {
        BottonePesca.setDisable(false);
        BottoneFermati.setDisable(false);
    }

    public void esegui() {
        e.eseguiPartita(turnoCorrente);
    }

    public void inizioTurno(int punteggioParziale, Giocatore giocatore) {
        CartaScoperta.setImage(null);
        fraseTurno.setVisible(true);
        giocatoreTurno.setVisible(true);
        PunteggioParziale.setVisible(true);
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
            giocatoreTurno.setText(giocatore.getNome());
        });
    }

    public void disabilitaPesca() {
        BottonePesca.setDisable(true);
    }

    public void abilitaPesca() {
        BottonePesca.setDisable(false);
    }

    CompletableFuture<Boolean> decisioneGiocatore() {

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

        e.attendi();

        return future; // Restituisci il CompletableFuture per attendere l'azione del giocatore
    }

    CompletableFuture<Boolean> decisioneObbligataGiocatore() {

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

        e.attendi();

        return future; // Restituisci il CompletableFuture per attendere l'azione del giocatore
    }

    public void setImmagine(Image immagine) {
        CartaScoperta.setImage(immagine);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(CartaScoperta);
        rotate.setDuration(Duration.millis(500));
        rotate.setByAngle(360);
        rotate.play();
    }

    public void disabilitaPunteggio() {
        PunteggioParziale.setVisible(false);
    }

    public void aggiornaVistaPunteggioParziale(int punteggioParziale) {
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
        });
    }

    public void aggiornaVistaPunteggio(Giocatore giocatoreCorrente, int punteggioTurno) {
        int indice = giocatori.indexOf(giocatoreCorrente);

        switch (indice) {
            case 0:
                Platform.runLater(() -> {
                    punteggioGiocatore0.setText(Integer.toString(punteggioTurno));
                });
                break;

            case 1:
                Platform.runLater(() -> {
                    punteggioGiocatore1.setText(Integer.toString(punteggioTurno));
                });
                break;
            case 2:
                Platform.runLater(() -> {
                    punteggioGiocatore2.setText(Integer.toString(punteggioTurno));
                });
                break;
            case 3:
                Platform.runLater(() -> {
                    punteggioGiocatore3.setText(Integer.toString(punteggioTurno));
                });
                break;
            default:
                System.err.println("Giocatore non trovato nella leaderboard");
                break;
        }
    }

    @FXML
    public void alertVittoria(Giocatore vincitore){
        Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partita terminata");
        alert.setContentText("Il giocatore " + vincitore.getNome() + " ha vinto!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("alertStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        alert.showAndWait();
        });
        
    }
}
