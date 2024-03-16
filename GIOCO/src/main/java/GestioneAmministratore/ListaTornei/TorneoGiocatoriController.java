package GestioneAmministratore.ListaTornei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneTornei.Torneo;
import GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TorneoGiocatoriController {

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

    private Torneo torneoCorrente;
    private List<Utente> utenti;
    private List<Torneo> tornei;
    private int numeroBot;
    private int giocatoriDaInserire;
    private File fileTornei;
    private File fileUtenti;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.utenti = new ArrayList<>();
        this.tornei = new ArrayList<>();
        this.numeroBot = 1;
        this.objectMapper = new ObjectMapper();
        this.fileTornei = new File("src/main/resources/FileJson/tornei.json");
        this.fileUtenti = new File("src/main/resources/FileJson/utenti.json");

        scaricaUtentiDaFile();
        scaricaTorneiDaFile();

        giocatoriChoice.getItems().addAll(utenti);
        aggiungiBot.setDisable(true);
    }

    public void setTorneo(Torneo t) {
        this.torneoCorrente = t;
        giocatoriDaInserire = torneoCorrente.getNumGiocatori();
        setIntestazione(giocatoriDaInserire);
    }

    @FXML
    private void aggiungiGiocatore(ActionEvent e) {
        if (giocatoriListView.getItems().size() == torneoCorrente.getNumGiocatori()) {
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
        torneoCorrente.aggiungiGiocatore(selezionato);
        aggiungiBot.setDisable(false);

        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesUtente();
    }

    @FXML
    private void aggiungiBot(ActionEvent e) {
        if (giocatoriListView.getItems().size() == torneoCorrente.getNumGiocatori()) {
            showMaxGiocatoriError();
            return;
        }

        Utente bot = new Utente("Bot " + numeroBot);

        giocatoriListView.getItems().add(bot);
        torneoCorrente.aggiungiBot(numeroBot);

        numeroBot++;
        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesBot();
    }

    @FXML
    private void creaTorneo(ActionEvent e) {
        if (giocatoriListView.getItems().size() != torneoCorrente.getNumGiocatori()) {
            showGiocatoriMancantiError();
            return;
        }
        
        torneoCorrente.sorteggiaPartite();
        tornei.add(torneoCorrente);
        caricaTorneiSuFile();

        showSuccesTorneo();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setIntestazione(int giocatoriDaInserire) {
        if (giocatoriDaInserire > 0)
            intestazione.setText("INSERISCI " + giocatoriDaInserire + " GIOCATORI");
        else
            intestazione.setText("TORNEO COMPLETO");
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
                try {
                    fileUtenti.createNewFile();
                    System.out.println("Il file utenti.json è stato creato con successo.");

                    // Aggiungiamo l'Admin
                    Utente admin = new Utente("Admin");
                    utenti.add(admin);
                    caricaUtentiSuFile();

                } catch (Exception e) {
                    // Alert(?)
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            // Alert(?)
            e.printStackTrace();
        }
    }

    private void caricaUtentiSuFile() {
        try {
            objectMapper.writeValue(fileUtenti, utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scaricaTorneiDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileTornei.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (fileTornei.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    return;
                } else {
                    tornei = objectMapper.readValue(fileTornei, new TypeReference<List<Torneo>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    fileTornei.createNewFile();
                    System.out.println("Il file è stato creato con successo.");
                } catch (Exception e) {
                    // Alert impossibile creare il file(?)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Alert impossibile scaricare dal file(?)
            e.printStackTrace();
        }
    }

    private void caricaTorneiSuFile() {
        try {
            objectMapper.writeValue(fileTornei, tornei);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void showSuccesTorneo() {
        infoLabel.setText("Torneo creata");
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