package GestioneGioco;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

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

    public void inizioTurno(int punteggioParziale, Giocatore giocatore){
        CartaScoperta.setImage(null);
        fraseTurno.setVisible(true);
        giocatoreTurno.setVisible(true);
        PunteggioParziale.setVisible(true);
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
            giocatoreTurno.setText(giocatore.getNome());
        });
    }

    public void disabilitaPesca(){
        BottonePesca.setDisable(true);
    }

    public void abilitaPesca(){
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
    }

    public void disabilitaPunteggio(){
        PunteggioParziale.setVisible(false);
    }

    public void aggiornaVistaPunteggio(int punteggioParziale){
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
        });
    }
}
