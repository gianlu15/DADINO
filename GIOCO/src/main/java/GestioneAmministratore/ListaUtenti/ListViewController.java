package GestioneAmministratore.ListaUtenti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import GestioneUtenti.Utente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListViewController {

    @FXML
    private ListView<String> listaUtenti;

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

    private int indiceSelezionato = -1;

    private List<Utente> utenti = new ArrayList<>();
    private List<String> nomi = new ArrayList<>();

    @FXML
    public void initialize() {
        scaricaDati();
        mostraLista();
    }

    // FASE 1: "scarico" la lista dal file .ser
    private void scaricaDati() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {

            utenti = (List<Utente>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestioneFileUtenti/utenti.ser");
            try {
                if (file.createNewFile()) {
                    Utente nuovoUtente = new Utente("Admin");
                    utenti.add(nuovoUtente);
                    caricaDati();

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

    @FXML
    public void aggiungiUtente(ActionEvent event) {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            showEmptyError();
            return;
        }

        if (username.startsWith("Admin")) {
            showAdminNameError();
            return;
        }

        if (username.startsWith("Bot")) {
            showBotError();
            return;
        }

        // FASE 2: aggiungo il nuovo utente alla lista
        boolean utenteEsiste = false;
        for (Utente utente : utenti) {
            if (username.equals(utente.getNome())) {
                utenteEsiste = true;
                break;
            }
        }

        if (!utenteEsiste) {
            Utente nuovoUtente = new Utente(username);
            utenti.add(nuovoUtente);
            nomi.add(nuovoUtente.getNome());

            listaUtenti.getItems().add(nuovoUtente.getNome());

            showSuccesUtente();

            usernameField.clear();
        } else {
            showErrorUtente();
            return;
        }
    }

    private void showErrorUtente() {
        infoLabel.setText("Nome utente già esistente!");
        infoLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccesUtente() {
        infoLabel.setText("Nome utente inserito");
        infoLabel.setStyle("-fx-text-fill: green;");
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

        utenti.remove(indiceSelezionato);
        nomi.remove(indiceSelezionato);
        listaUtenti.getItems().remove(indiceSelezionato);
        showEliminatoSucces();
    }

    // FASE 3: carico la lista aggiornata
    public void caricaDati() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {
            outputStream.writeObject(utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        caricaDati();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void mostraLista() {

        for (Utente u : utenti) {
            nomi.add(u.getNome());
        }

        listaUtenti.getItems().addAll(nomi);
    }

}