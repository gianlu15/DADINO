package GestioneAmministratore;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PartiteView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("partiteView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("listViewStyle.css").toExternalForm());

        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            PartiteViewController pw = loader.getController();
            pw.caricaDati();
            stage.close();
        });

        stage.setScene(scene);
        stage.show();
    }
    
}
