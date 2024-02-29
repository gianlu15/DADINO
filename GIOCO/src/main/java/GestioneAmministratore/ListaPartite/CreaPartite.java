package GestioneAmministratore.ListaPartite;

import java.io.IOException;

import GestioneAmministratore.ListaUtenti.ListViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

public class CreaPartite extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("creaPartite.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            event.consume(); // Consuma l'evento per impedire la chiusura immediata della finestra

            // Mostra una finestra di conferma
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Uscendo ora non salverai la partita");
            alert.setContentText("Premi OK per confermare ed uscire");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/GestioneAmministratore/alertChiusuraStyle.css").toExternalForm());
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
