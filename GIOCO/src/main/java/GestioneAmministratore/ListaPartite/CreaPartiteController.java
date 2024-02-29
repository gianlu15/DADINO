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

import GestionePartite.Partita;

public class CreaPartiteController {

    @FXML
    private TextField nomePartitaField;

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

    @FXML
    public void initialize() {
        scaricaDati();
        numGiocatoriChoice.getItems().addAll(numeriGiocatori);
    }

    @FXML
    public void creaPartita(ActionEvent ev) throws IOException {

        String nome = nomePartitaField.getText();
        String str = codicePartitaField.getText();
        Integer numGiocatori = numGiocatoriChoice.getValue();

        if (nome.isEmpty()) {
            showEmptyError();
            return;
        }

        if (str.isEmpty()) {
            showCodiceEmpty();
            return;
        }

        if (str.length() != 4) {
            showCodiceLength();
            return;
        }

        if (nome.length() > 10 || nome.length() < 3) {
            showNomeLength();
            return;
        }

        if (numGiocatori == null) {
            showNumGiocatiriNotSelected();
            return;
        }

        try {
            int codice = Integer.parseInt(str);

            boolean nomeEsistente = false;
            boolean codiceEsistente = false;
            for (Partita p : partite) {
                if (nome.equals(p.getNome())) {
                    nomeEsistente = true;
                    showNomePresente();
                    break;
                }
                if (codice == p.getCodice()) {
                    codiceEsistente = true;
                    showCodicePresente();
                    break;
                }
            }

            if (!nomeEsistente && !codiceEsistente) {
                Partita nuovaPartita = new Partita(nome, numGiocatori, codice);

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
        } catch (NumberFormatException e) {
            showNotNumberError();
            return;
        }
    }

    private void showCodiceLength() {
        errorLabel.setText("Il codice deve avere 4 cifre");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showNotNumberError() {
        errorLabel.setText("Il codice non ammette lettere");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showEmptyError() {
        errorLabel.setText("Campo nome vuoto");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showCodiceEmpty() {
        errorLabel.setText("Campo codice vuoto");
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

    private void showCodicePresente() {
        errorLabel.setText("Codice già usato");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSucces() {
        errorLabel.setText("Partita creata");
        errorLabel.setStyle("-fx-text-fill: green;");
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
