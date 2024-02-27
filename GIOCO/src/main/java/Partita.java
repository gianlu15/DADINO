import GestioneGioco.Giocatore;
import GestioneGioco.GiocoController;
import GestioneGioco.Tavolo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Partita extends Application {

    public static void main (String[] args){
        launch();
    }

    public void start(Stage primaryStage) throws Exception {

        Tavolo t = new Tavolo();

        Giocatore g1 = new Giocatore("GIANLUCA");
        Giocatore g2 = new Giocatore ("ANA");
        t.nuovoPunteggio(g2);
        t.nuovoPunteggio(g1);



        // Carica il file FXML e crea il controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestioneGiocoFX/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller
        GiocoController controller = loader.getController();

        // Imposta il tavolo nel controller
        controller.setTavolo(t);

        // Mostra la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.esegui();
    }
}
