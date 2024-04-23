package com.example.GestioneAmministratore.ListaUtenti;

import com.example.GestisciFile;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestioneUtenti.Utente;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListViewController {

    @FXML
    private ListView<Utente> listaUtenti;

    @FXML
    private Button aggiungiUtente;

    @FXML
    private Button rimuoviUtente;

    @FXML
    private Button salvaEdEsci;

    @FXML
    private TextField usernameField;

    @FXML
    private Label infoLabel;

    private int indiceSelezionato;
    private List<Utente> utenti;
    private File fileUtenti;
    private File fileGiocatori;
    private ObjectMapper objectMapper;
    private List<Giocatore> giocatori;

    @FXML
    public void initialize() throws URISyntaxException {
        this.utenti = new ArrayList<>();
        this.indiceSelezionato = -1;
        this.objectMapper = new ObjectMapper();
        this.giocatori = new ArrayList<>();

        String path = GestisciFile.ottieniDirectory();

        this.fileUtenti = new File(path, "utenti.json");
        this.fileGiocatori = new File(path, "giocatori.json");

        scaricaUtentiDaFile();
        listaUtenti.getItems().addAll(utenti); // Mostriamo gli utenti del file sulla ListView
    }

    @FXML
    public void aggiungiUtente(ActionEvent event) {
        String username = usernameField.getText();

        if (controlli(username))
            return;

        Utente nuovoUtente = new Utente(username);
        utenti.add(nuovoUtente); // Aggiungianmo l'utente all'array
        listaUtenti.getItems().add(nuovoUtente); // Aggiungiamo l'utente sulla ListView
        caricaUtentiSuFile(); // Carichiamo gli utenti su file

        showSuccesUtente();

        usernameField.clear();
    }

    @FXML
    private void rimuoviUtente(ActionEvent e) {
        if (listaUtenti.getSelectionModel().isEmpty()) {
            showNoIndexError();
            return;
        }

        indiceSelezionato = listaUtenti.getSelectionModel().getSelectedIndex();

        if (indiceSelezionato == 0) {
            showAdminError();
            return;
        }

        scaricaGiocatoriDaFile();
        // Rimuoviamo il giocatore dalla lista
        for (Giocatore g : giocatori) {
            if (g.getNome() == utenti.get(indiceSelezionato).getNome()) {
                giocatori.remove(g);
                caricaGiocatoriSuFile();
                break;
            }
        }

        utenti.remove(indiceSelezionato); // Rimuoviamo l'utente dall'array

        listaUtenti.getItems().remove(indiceSelezionato); // Rimuoviamo l'utente dalla ListView
        caricaUtentiSuFile();

        showEliminatoSucces();
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
                    alertCreaFile(fileUtenti.getName());
                    e.printStackTrace();
                    return;
                }
            }

        } catch (IOException e) {
            alertScaricaFile(fileUtenti.getName());
            e.printStackTrace();
            return;
        }
    }

    private void scaricaGiocatoriDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileGiocatori.exists()) {
                System.out.println("Il file giocatori esiste già.");
                if (fileGiocatori.length() == 0) {
                    System.out.println("Il file giocatori è vuoto");
                    return;
                } else {
                    giocatori = objectMapper.readValue(fileGiocatori, new TypeReference<List<Giocatore>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    fileGiocatori.createNewFile();
                    System.out.println("Il file giocatori è stato creato con successo.");

                } catch (Exception e) {
                    alertCreaFile(fileGiocatori.getName());
                    e.printStackTrace();
                    return;
                }
            }

        } catch (IOException e) {
            alertScaricaFile(fileGiocatori.getName());
            e.printStackTrace();
            return;
        }
    }

    private void caricaGiocatoriSuFile() {
        try {
            objectMapper.writeValue(fileGiocatori, giocatori);
        } catch (IOException e) {
            alertCaricaFile(fileGiocatori.getName());
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

    private boolean controlli(String username) {

        if (username.isEmpty()) {
            showEmptyError();
            return true;
        }

        if (username.startsWith("Admin")) {
            showAdminNameError();
            return true;
        }

        if (username.startsWith("Bot")) {
            showBotError();
            return true;
        }

        if (username.length() > 10) {
            showTooLongError();
            return true;
        }

        if (username.length() < 3) {
            showTooShortError();
            return true;
        }

        for (Utente utente : utenti) {
            if (username.equalsIgnoreCase(utente.getNome())) {
                showErrorUtente();
                return true;
            }
        }

        return false;
    }

    private void showErrorUtente() {
        infoLabel.setText("Nome utente già esistente!");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccesUtente() {
        infoLabel.setText("Nome utente inserito");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showTooLongError() {
        infoLabel.setText("Nome utente troppo lungo");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showTooShortError() {
        infoLabel.setText("Nome utente troppo corto");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showAdminError() {
        infoLabel.setText("Impossibile eliminare l'Admin");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showAdminNameError() {
        infoLabel.setText("Non è possibile chiamarsi Admin");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showBotError() {
        infoLabel.setText("Non è possibile chiamarsi Bot");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showNoIndexError() {
        infoLabel.setText("Nessun utente selezionato");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showEliminatoSucces() {
        infoLabel.setText("Utente rimosso");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showEmptyError() {
        infoLabel.setText("Campo vuoto");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML
    public void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}