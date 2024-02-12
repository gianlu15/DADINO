import GestioneCarte.Carta;
import GestioneCarte.Mazzo;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestMazzo extends Application {

    public static void avvio() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Mazzo maz = new Mazzo();

        maz.inizializzaMazzo();

        System.out.println("carte nel mazzo " + maz.getNumeroCarte());

        maz.mostraMazzo();

        maz.mescolaMazzo();

        maz.mostraMazzo();

        Carta c = maz.pescaCarta();


        //Visualizzazione della carta pescata
        try {
            Group root = new Group();

            Scene scene = new Scene(root, 900, 500, Color.GREEN);

            Image immagine = c.getImmagine();
            ImageView vista = new ImageView(immagine);
            vista.setX(10);
            vista.setY(60);
            vista.setFitWidth(250);
            vista.setFitHeight(360);
            root.getChildren().add(vista);

            stage.setScene(scene);

            stage.setTitle("Stage DEMO del gioco"); // Settiamo il titolo dello stage

            stage.setResizable(false); // Rendiamo lo stage non ridimensionabile

            stage.show(); // Mostriamo lo stage

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
