package com.example.GestioneAmministratore.ListaTornei;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TorneiListView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("torneiListView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/Style2.css").toExternalForm());

        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setScene(scene);

        stage.show();
    }
}
