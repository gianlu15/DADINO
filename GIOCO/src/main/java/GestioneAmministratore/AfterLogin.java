package GestioneAmministratore;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//SCENA 3 (Controller: AfterLoginController)

public class AfterLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AfterLogin.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("SPACCA");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Questo main serve SOLO se vogliamo avviare il programma direttamente da
    // "AfterLogin.java"
    public static void main(String[] args) {
        launch(args);
    }

}
