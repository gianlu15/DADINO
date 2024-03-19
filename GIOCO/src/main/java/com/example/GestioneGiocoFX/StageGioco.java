package com.example.GestioneGiocoFX;

import java.io.IOException;

import com.example.GestionePartite.Partita;
import com.example.GestionePartite.Partita.Stato;
import com.example.GestioneTornei.Torneo;

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
    private Torneo torneoAttivo;
    private boolean partitaTorneo;

    public StageGioco(Partita partitaAttiva) {
        this.partitaAttiva = partitaAttiva;
        this.partitaTorneo = false;
    }

    public StageGioco(Partita partitaAttiva, Torneo torneoAttivo) {
        this.partitaAttiva = partitaAttiva;
        this.torneoAttivo = torneoAttivo;
        this.partitaTorneo = true;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/GestioneGiocoFX/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller #1
        GiocoController controller = loader.getController();

        // Imposta il tavolo #2
        if (!partitaTorneo) {
            if (partitaAttiva.getStatoPartita() == Stato.Pronta)
                controller.creaNuovoTavolo(partitaAttiva);
            else
                controller.reimpostaTavolo(partitaAttiva);
        } else {
            if (partitaAttiva.getStatoPartita() == Stato.Pronta)
                controller.creaNuovoTavoloTorneo(partitaAttiva, torneoAttivo);
            else
                controller.reimpostaTavoloTorneo(partitaAttiva, torneoAttivo);
        }

        // Imposta lo stage nel controller #3
        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets()
                .add(getClass().getResource("/com/example/GestioneGiocoFX/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);

        // Avvia l'esecuzione della partita in un thread separato #4
        controller.esegui();

        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // Consuma l'evento per evitare la chiusura immediata della finestra

            // Mostra un Alert per confermare la chiusura
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma chiusura");
            alert.setHeaderText("La partita verrà sospesa");
            alert.getDialogPane().getStylesheets()
                    .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

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
