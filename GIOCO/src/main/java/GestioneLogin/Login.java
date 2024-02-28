package GestioneLogin;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//SCENA NUMERO 2  (Controller: LoginController.java)

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("SPACCA");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // Questo main serve SOLO se vogliamo avviare il programma direttamente da
    // "Login.java"
    public static void main(String[] args) {
        launch(args);
    }
}
