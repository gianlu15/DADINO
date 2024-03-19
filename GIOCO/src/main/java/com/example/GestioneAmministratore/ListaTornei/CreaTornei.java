package com.example.GestioneAmministratore.ListaTornei;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

public class CreaTornei extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("creaTornei.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/Style2.css").toExternalForm());

        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            event.consume(); // Consuma l'evento per impedire la chiusura immediata della finestra

            // Mostra una finestra di conferma
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Uscendo ora non salverai il torneo");
            alert.setContentText("Premi OK per confermare ed uscire");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/Styles/alertStyle.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");

            // Gestione della risposta dell'utente
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    stage.close();
                }
            });
        });

        stage.show();
    }

}
