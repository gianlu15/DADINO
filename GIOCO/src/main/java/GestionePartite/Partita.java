package GestionePartite;

import GestioneGioco.Bot;
import GestioneGioco.Giocatore;
import GestioneGioco.GiocoController;
import GestioneGioco.Tavolo;
import GestioneUtenti.Utente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.Serializable;

public class Partita extends Application implements Serializable {

    public enum Stato {
        Pronta, Sospesa, Terminata;
    }

    String nomePartita;
    int numGiocatori;
    int codice;
    Stato statoPartita;

    static Tavolo t;

    public Partita(String nomePartita, int numGiocatori, int codice) {
        t = new Tavolo();
        this.nomePartita = nomePartita;
        this.numGiocatori = numGiocatori;
        this.codice = codice;
        statoPartita = Stato.Pronta;
    }

    public String getNome() {
        return nomePartita;
    }

    public int getNumGiocatori() {
        return numGiocatori;
    }

    public int getCodice() {
        return codice;
    }

    public Stato getStato() {
        return statoPartita;
    }

    public void aggiungiGiocatore(Utente u) {
        Giocatore g = new Giocatore(u.getNome());
        t.nuovoPunteggio(g);
    }

    public void aggiungiBot(int numeroBot) {
        Bot b = new Bot("Bot-" + numeroBot);
        t.nuovoPunteggio(b);
    }

    public static void avvio() {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        ;

        // Carica il file FXML e crea il controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestioneGioco/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller
        GiocoController controller = loader.getController();

        // Imposta il tavolo nel controller
        controller.setTavolo(t);

        // Mostra la scena
        Scene scene = new Scene(root, 900, 500);
        scene.getStylesheets().add(getClass().getResource("GestioneGioco/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Avvia l'esecuzione della partita in un thread separato
        Thread partitaThread = new Thread(() -> {
            controller.esegui();
        });
        partitaThread.start();
    }
}
