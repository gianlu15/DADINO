package GestioneAmministratore.ListaPartite;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import GestionePartite.Partita;

public class CreaPartiteController {

    @FXML
    private TextField nomePartitaField;

    @FXML
    private Label codiceField;

    @FXML
    private Button generaButton;

    @FXML
    private TextField codicePartitaField;

    @FXML
    private ChoiceBox<Integer> numGiocatoriChoice;

    @FXML
    private Button creaButton;

    @FXML
    private Label errorLabel;

    private List<Partita> partite = new ArrayList<>();

    private Integer[] numeriGiocatori = { 2, 3, 4 };
    private int numeroCasuale = 0;

    @FXML
    public void initialize() {
        scaricaDati();
        numGiocatoriChoice.getItems().addAll(numeriGiocatori);
    }

    @FXML
    private void generaCodice(ActionEvent event) {
        Random random = new Random();
        boolean codiceEsistente = false;

        do {
            numeroCasuale = random.nextInt(9000) + 1000;
            for (Partita p : partite) {
                if (numeroCasuale == p.getCodice()) {
                    codiceEsistente = true;
                    break;
                } else
                    codiceEsistente = false;
            }

        } while (codiceEsistente);

        codiceField.setText(String.valueOf(numeroCasuale));

    }

    @FXML
    public void creaPartita(ActionEvent ev) throws IOException {

        String nome = nomePartitaField.getText();
        Integer numGiocatori = numGiocatoriChoice.getValue();
        

        if (nome.isEmpty()) {
            showEmptyError();
            return;
        }

        if (nome.length() > 17 || nome.length() < 3) {
            showNomeLength();
            return;
        }

        if (numGiocatori == null) {
            showNumGiocatiriNotSelected();
            return;
        }

        boolean nomeEsistente = false;
        for (Partita p : partite) {
            if (nome.equals(p.getNome())) {
                nomeEsistente = true;
                showNomePresente();
                return;
            }
        }

        if (numeroCasuale<1000 || numeroCasuale>9999) {
            showNumeroNonGenerato();
            return;
        }

        if (!nomeEsistente) {
            Partita nuovaPartita = new Partita(nome, numGiocatori, numeroCasuale);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/GestioneAmministratore/ListaPartite/partitaGiocatori.fxml"));
            Parent root = loader.load();

            // Ottieni lo Stage della finestra di Login
            Stage loginStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());
            loginStage.setScene(scene);
            PartitaGiocatoriController pgc = loader.getController();
            pgc.setPartita(nuovaPartita);

        }
    }

    private void showNumeroNonGenerato() {
        errorLabel.setText("Codice non generato");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showEmptyError() {
        errorLabel.setText("Campo nome vuoto");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showNomeLength() {
        errorLabel.setText("Il nome è troppo lungo/corto");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showNumGiocatiriNotSelected() {
        errorLabel.setText("Numero giocatori non selezionato");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showNomePresente() {
        errorLabel.setText("Partita con lo stesso nome già presente");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void scaricaDati() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestionePartite/partite.ser"))) {

            System.out.println("File partite trovato in CreaPartiteController");
            partite = (List<Partita>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestionePartite/partite.ser");
            try {
                if (file.createNewFile()) {
                    Partita nuova = new Partita("Default", 0, 0000);
                    partite.add(nuova);
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

    public void caricaDati() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestionePartite/partite.ser"))) {
            outputStream.writeObject(partite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
