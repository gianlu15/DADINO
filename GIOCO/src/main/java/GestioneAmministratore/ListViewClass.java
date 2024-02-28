package GestioneAmministratore;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

public class ListViewClass extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("listView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("listViewStyle.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Modifica Utenti");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            event.consume(); // Consuma l'evento per impedire la chiusura immediata della finestra

            // Mostra una finestra di conferma
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Vuoi salvare le modifiche effettuate?");
            alert.setContentText("Premi OK per confermare, Esci per uscire senza salvare.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("alertChiusuraStyle.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");

            ButtonType esciButton = new ButtonType("Esci");
            alert.getButtonTypes().add(esciButton);

            // Gestione della risposta dell'utente
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ListViewController lw = loader.getController();
                    lw.caricaDati();
                    stage.close();
                } else if (response == esciButton) {
                    stage.close();
                }
            });
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}