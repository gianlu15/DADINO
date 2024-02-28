package GestioneLogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//SCENA NUMERO 1 (Controller: HomeLoginController.java)

public class HomeLogin extends Application {

    public static void avvio() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        String css = this.getClass().getResource("/Styles/StyleSP.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setTitle("SPACCA");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }
}