package com.example.GestioneTutorial;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageGiocoTutorial extends Application {

    public static void avvio(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/GestioneTutorial/StageGiocoTutorial.fxml"));
        Parent root = loader.load();

        TutorialController controller = loader.getController();

        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets()
                .add(getClass().getResource("/com/example/GestioneGiocoFX/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
