
package GestioneGiocoFX;
import java.net.URL;
import java.util.ResourceBundle;

import GestioneGioco.Bot;
import GestioneGioco.Esecuzione;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MyController implements Initializable {

    private static boolean  pescaScelta = false; // Inizializza la variabile per memorizzare la scelta dell'utente

    @FXML
    private void handlePescare(ActionEvent event) {
        System.out.println("Hai scelto pesca");
        pescaScelta = true; // Imposta la variabile su true quando l'utente fa clic sul pulsante "PESCA"
    }

    @FXML
    private void handleFermarsi(ActionEvent event) {
        System.out.println("Hai scelto fermati");
        pescaScelta = false; // Imposta la variabile su false quando l'utente fa clic sul pulsante "FERMATI"
    }

    public static boolean isPescaScelta() {
        return pescaScelta; // Restituisce la scelta dell'utente
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tavolo t = new Tavolo();
        Giocatore g1 = new Giocatore("LORENZO");
        Bot g2 = new Bot("GIANLUCA");

        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g2);


        t.stampaOrdine();

        Esecuzione e = new Esecuzione(t);

        e.eseguiPartita();
    }
}
