package GestioneAmministratore.ListaPartite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import GestionePartite.Partita;
import GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    private Partita pCorrente;

    private List<Utente> utenti = new ArrayList<>();
    private List<Partita> partite = new ArrayList<>();

    private int numeroBot = 1;
    private int giocatoriDaInserire;

    @FXML
    public void initialize() {
        scaricaDatiUtenti();
        scaricaDatiPartite();

        giocatoriChoice.getItems().addAll(utenti);
        aggiungiBot.setDisable(true);
        ;
    }

    public void setPartita(Partita p) {
        this.pCorrente = p;
        giocatoriDaInserire = pCorrente.getNumGiocatori();
        setIntestazione(giocatoriDaInserire);
    }

    @FXML
    private void aggiungiGiocatore(ActionEvent e) {
        if (giocatoriListView.getItems().size() == pCorrente.getNumGiocatori()) {
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
        pCorrente.aggiungiGiocatore(selezionato);
        aggiungiBot.setDisable(false);

        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesUtente();
    }

    @FXML
    private void aggiungiBot(ActionEvent e) {
        if (giocatoriListView.getItems().size() == pCorrente.getNumGiocatori()) {
            showMaxGiocatoriError();
            return;
        }

        Utente bot = new Utente("Bot" + numeroBot);
        numeroBot++;

        giocatoriListView.getItems().add(bot);
        pCorrente.aggiungiBot(numeroBot);

        giocatoriDaInserire--;
        setIntestazione(giocatoriDaInserire);

        showSuccesBot();
    }

    @FXML
    private void creaPartita(ActionEvent e) {
        if (giocatoriListView.getItems().size() != pCorrente.getNumGiocatori()) {
            showGiocatoriMancantiError();
            return;
        }

        partite.add(pCorrente);
        caricaDatiPartite();
        showSuccesPartita();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setIntestazione(int giocatoriDaInserire) {
        switch (giocatoriDaInserire) {

            case 4:
                intestazione.setText("INSERISCI 4 GIOCATORI");
                break;

            case 3:
                intestazione.setText("INSERISCI 3 GIOCATORI");
                break;

            case 2:
                intestazione.setText("INSERISCI 2 GIOCATORI");
                break;

            case 1:
                intestazione.setText("INSERISCI UN GIOCATORE");
                break;

            default:
                intestazione.setText("PARTITA COMPLETA");
                break;
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

    private void scaricaDatiUtenti() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {

            System.out.println("File utenti trovato in PartiteGiocatoriController");
            utenti = (List<Utente>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestioneFileUtenti/utenti.ser");
            try {
                if (file.createNewFile()) {

                    Utente nuovoUtente = new Utente("Admin");
                    utenti.add(nuovoUtente);
                    caricaDatiUtenti();

                    System.out.println("File creato con successo.");
                } else {
                    System.out.println("Impossibile creare il file.");
                }
            } catch (IOException f) {
                f.printStackTrace();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void scaricaDatiPartite() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestionePartite/partite.ser"))) {

            System.out.println("File partite trovato in CreaPartiteController");
            partite = (List<Partita>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestionePartite/partite.ser");
            try {
                if (file.createNewFile()) {
                    System.out.println("File creato con successo.");
                } else {
                    System.out.println("Impossibile creare il file.");
                }
            } catch (IOException f) {
                f.printStackTrace();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void caricaDatiPartite() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestionePartite/partite.ser"))) {
            outputStream.writeObject(partite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void caricaDatiUtenti() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {
            outputStream.writeObject(utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}