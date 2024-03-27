package com.example.GestioneAmministratore.ListaPartite;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.example.GestisciFile;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestionePartite.Partita;
import com.example.GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public class PartitaGiocatoriController {

    @FXML
    private Label intestazione;

    @FXML
    private ChoiceBox<Utente> giocatoriChoice;

    @FXML
    private Button fine;

    @FXML
    private ListView<Utente> giocatoriListView;

    @FXML
    private Button aggiungiGiocatore;

    @FXML
    private Button aggiungiBot;

    @FXML
    private Label infoLabel;

    private Partita partitaCorrente;
    private List<Utente> utenti;
    private List<Partita> partite;
    private int numeroBot;
    private int giocatoriDaInserire;
    private File filePartite;
    private File fileUtenti;
    private ObjectMapper objectMapper;
    private ObjectMapper partitaMapper;

    @FXML
    public void initialize() throws URISyntaxException {
        this.utenti = new ArrayList<>();
        this.partite = new ArrayList<>();
        this.numeroBot = 1;
        this.objectMapper = new ObjectMapper();
        this.partitaMapper = new ObjectMapper();

        String path = GestisciFile.ottieniDirectory();

        this.filePartite = new File(path, "partite.json");
        this.fileUtenti = new File(path, "utenti.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfBaseType("java.util.List<com.example.GestionePartite.Partita>")
                .build();
        partitaMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        scaricaUtentiDaFile();
        scaricaPartiteDaFile();

        giocatoriChoice.getItems().addAll(utenti);
        aggiungiBot.setDisable(true);
    }

    public void setPartita(Partita p) {
        this.partitaCorrente = p;
        giocatoriDaInserire = partitaCorrente.getNumGiocatori();
        setIntestazione(giocatoriDaInserire);
    }

    @FXML
    private void aggiungiGiocatore(ActionEvent e) {
        if (giocatoriListView.getItems().size() == partitaCorrente.getNumGiocatori()) {
            showMaxGiocatoriError();
            return;
        }

        Utente selezionato = giocatoriChoice.getValue();

        if (selezionato == null) {
            showEmptyUtenteError();
            return;
        }

        if (giocatoriListView.getItems().contains(selezionato)) {
            showUtenteAlreadyExistsError();
            return;
        }

        giocatoriListView.getItems().add(selezionato);
        partitaCorrente.aggiungiGiocatore(selezionato);
        aggiungiBot.setDisable(false);

        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesUtente();
    }

    @FXML
    private void aggiungiBot(ActionEvent e) {
        if (giocatoriListView.getItems().size() == partitaCorrente.getNumGiocatori()) {
            showMaxGiocatoriError();
            return;
        }

        Utente bot = new Utente("Bot" + numeroBot);

        giocatoriListView.getItems().add(bot);
        partitaCorrente.aggiungiBot(numeroBot);

        numeroBot++;
        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesBot();
    }

    @FXML
    private void creaPartita(ActionEvent e) {
        if (giocatoriListView.getItems().size() != partitaCorrente.getNumGiocatori()) {
            showGiocatoriMancantiError();
            return;
        }
        partite.add(partitaCorrente);
        caricaPartiteSuFile();

        showSuccesPartita();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setIntestazione(int giocatoriDaInserire) {

        if (giocatoriDaInserire > 0)
            intestazione.setText("INSERISCI " + giocatoriDaInserire + " GIOCATORI");
        else
            intestazione.setText("PARTITA COMPLETA");

    }

    private void scaricaUtentiDaFile() {
        try {

            // Se il file esiste lo leggiamo
            if (fileUtenti.exists()) {

                System.out.println("Il file utenti.json esiste già.");
                utenti = objectMapper.readValue(fileUtenti, new TypeReference<List<Utente>>() {
                });

            } else {

                // Se il file non esiste, lo creiamo ma non lo leggiamo
                fileUtenti.createNewFile();
                System.out.println("Il file utenti.json è stato creato con successo.");

                // Aggiungiamo l'Admin
                Utente admin = new Utente("Admin");
                utenti.add(admin);
                caricaUtentiSuFile();
            }

        } catch (IOException e) {
            alertScaricaFile(fileUtenti.getName());
            e.printStackTrace();
            return;
        }
    }

    private void caricaUtentiSuFile() {
        try {
            objectMapper.writeValue(fileUtenti, utenti);
        } catch (IOException e) {
            alertCaricaFile(fileUtenti.getName());
            e.printStackTrace();
            return;
        }
    }

    private void scaricaPartiteDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (filePartite.exists()) {
                System.out.println("Il file partite.json esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (filePartite.length() == 0) {
                    System.out.println("Il file partite.json JSON è vuoto.");
                    return;
                } else {
                    partite = partitaMapper.readValue(filePartite, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    filePartite.createNewFile();
                    System.out.println("Il file partite.json è stato creato con successo.");
                } catch (Exception e) {
                    alertCreaFile(filePartite.getName());
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            alertScaricaFile(filePartite.getName());
            e.printStackTrace();
            return;
        }
    }

    private void caricaPartiteSuFile() {
        try {
            partitaMapper.writeValue(filePartite, partite);
        } catch (IOException e) {
            alertCaricaFile(filePartite.getName());
            e.printStackTrace();
            return;
        }
    }

    @FXML
    public void alertCreaFile(String nomeFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setContentText("Impossibile creare il file: " + nomeFile);
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertScaricaFile(String nomeFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setContentText("Impossibile leggere il file: " + nomeFile);
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertCaricaFile(String nomeFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setContentText("Impossibile salvare il file: " + nomeFile);
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    private void showEmptyUtenteError() {
        infoLabel.setText("Nessun utente selezionato");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showMaxGiocatoriError() {
        infoLabel.setText("Massimo giocatori raggiunto");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showGiocatoriMancantiError() {
        infoLabel.setText("Giocatori mancanti");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showSuccesUtente() {
        infoLabel.setText("Utente aggiunto");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showSuccesPartita() {
        infoLabel.setText("Partita creata");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showSuccesBot() {
        infoLabel.setText("Bot aggiunto");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showUtenteAlreadyExistsError() {
        infoLabel.setText("Utente già presente");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }
}