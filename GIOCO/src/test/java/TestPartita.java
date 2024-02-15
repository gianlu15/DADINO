import GestioneGioco.Bot;
import GestioneGioco.Giocatore;
import GestioneGioco.GiocoController;
import GestioneGioco.Tavolo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestPartita extends Application {

    static Tavolo t;

    public static void avvio(){
        t = new Tavolo();

        Giocatore g1 = new Giocatore("GIANLUCA");
        Giocatore g2 = new Giocatore("ANA");
        t.nuovoPunteggio(g2);
        t.nuovoPunteggio(g1);

        launch();
    }

    public void start(Stage primaryStage) throws Exception {;

        // Carica il file FXML e crea il controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestioneGioco/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller
        GiocoController controller = loader.getController();

        // Imposta il tavolo nel controller
        controller.setTavolo(t);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("GestioneGioco/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

       // Avvia l'esecuzione della partita in un thread separato
        Thread partitaThread = new Thread(() -> {
            controller.esegui();
        });
        partitaThread.start();
    }
}
