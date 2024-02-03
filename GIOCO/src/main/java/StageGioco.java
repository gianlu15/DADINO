import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StageGioco extends Application {

    public static void avvio(){
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
         /* 
         * Creamo un tipo di root nodo base: Group 
         */
        Group root = new Group();

        /*
         * La scena si trova al di sopra dello stage, e per costruirla dobbiamo 
         * passare come argomento un root node.
         */
        Scene scene = new Scene(root, Color.GREEN);     //Impostiamo anche il colore della scena

        /*
         * Lo stage rappresenta la "cornice" dell'interfaccia
         */

        stage.setTitle("Stage DEMO di gioco");       //Settiamo il titolo dello stage
        
        stage.setWidth(420);                    //Settiamo una largehzza fissa dello stage
        stage.setHeight(420);                   //Settiamo una altezza fissa dello stage
        stage.setResizable(false);              //Rendiamo lo stage non ridimensionabile

        stage.setScene(scene);      //Settiamo la scena nello stage
        stage.show();               //Mostriamo lo stage
    }
    
}
