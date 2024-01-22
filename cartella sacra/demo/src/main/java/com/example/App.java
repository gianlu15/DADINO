package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Vediamo come realizzare un interfaccia reattiva al movimento sfruttando
 * lo Scene Builder, un file FXML e la classe Controller.
 * Realizziamo un cerchio che si muove in base ai tasti SU-GIU-DESTRA-SINISTRA
 * che vengono premuti.
 */

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            stage.setTitle("Muovi il cerchio");  // Settiamo il titolo dello stage
            stage.setScene(new Scene(root));                // Creamo e settiamo la scena nello stage
            stage.show();                                   // Mostriamo lo stage

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
