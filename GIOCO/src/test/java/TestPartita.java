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
        Bot g2 = new Bot("Bot1");
        Bot g3 = new Bot("Bot2");
        t.nuovoPunteggio(g2);
        t.nuovoPunteggio(g1);
        t.nuovoPunteggio(g3);

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
        Scene scene = new Scene(root,900,500);
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
