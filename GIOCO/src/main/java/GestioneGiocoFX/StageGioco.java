package GestioneGiocoFX;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageGioco extends Application {

    public static void avvio() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            // Configuriamo il file FMXL e specifichiamo il suo nome
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StageGioco.fxml"));

            // Creamo un nodo root caricando il file FXML di prima
            Parent root = (Parent) fxmlLoader.load();

            Scene scene = new Scene(root, 1000, 800);
            String css = this.getClass().getResource("StageGioco.css").toExternalForm();

            scene.getStylesheets().add(css);
            stage.setScene(scene);

            stage.setTitle("Stage DEMO del gioco"); // Settiamo il titolo dello stage

            stage.setResizable(false); // Rendiamo lo stage non ridimensionabile

            stage.show(); // Mostriamo lo stage


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
