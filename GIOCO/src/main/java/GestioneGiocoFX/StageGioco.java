package GestioneGiocoFX;

import java.io.IOException;

import GestionePartite.Partita;
import GestionePartite.Partita.Stato;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class StageGioco extends Application {

    private Partita partitaAttiva;

    public StageGioco(Partita partitaAttiva) {
        this.partitaAttiva = partitaAttiva;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneGiocoFX/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller #1
        GiocoController controller = loader.getController();

        // Imposta il tavolo #2
        if (partitaAttiva.getStatoPartita() == Stato.Pronta)
            controller.creaNuovoTavolo(partitaAttiva);
        else
            controller.reimpostaTavolo(partitaAttiva);

        // Imposta lo stage nel controller #3
        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/GestioneGiocoFX/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);

        // Avvia l'esecuzione della partita in un thread separato #4
        controller.esegui();

        // TODO PUPA sistemare allert

        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // Consuma l'evento per evitare la chiusura immediata della finestra

            // Mostra un Alert per confermare la chiusura
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma chiusura");
            alert.setHeaderText("La partita verrà sospesa");
            alert.getDialogPane().getStylesheets()
                    .add(StageGioco.class.getResource("/Styles/alertStyle.css").toExternalForm());

            // Aggiungi pulsanti al dialogo
            ButtonType btnYes = new ButtonType("OK", ButtonBar.ButtonData.YES);

            alert.getButtonTypes().setAll(btnYes);

            // Mostra e gestisci la risposta dell'utente
            alert.showAndWait().ifPresent(result -> {
                if (result == btnYes) {
                    partitaAttiva.setStatoPartita(Stato.Sospesa);
                    controller.interrompiPartita();
                    primaryStage.close();
                }
            });
        });

    }

}
