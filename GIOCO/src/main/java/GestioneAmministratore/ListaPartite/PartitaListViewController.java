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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PartitaListViewController {

    @FXML
    private Button rimuoviPartita;

    @FXML
    private Button salvaEdEsci;

    @FXML
    private Label infoLabel;

    @FXML
    private TableView<Partita> tabellaPartite;

    @FXML
    private TableColumn<Partita, String> colonnaNome;

    @FXML
    private TableColumn<Partita, Integer> colonnaCodice;

    @FXML
    private TableColumn<Partita, Partita.Stato> colonnaStato;

    private List<Partita> partite = new ArrayList<>();
    private int indiceSelezionato = -1;

    @FXML
    public void initialize() {
        scaricaDati();
        mostraTabella();
    }

    private void mostraTabella() {
        tabellaPartite.setItems(FXCollections.observableArrayList(partite));

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codice"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
    }

    @FXML
    private void rimuoviPartita(ActionEvent e) {
        if (tabellaPartite.getSelectionModel().isEmpty()) {
            showNoIndexError();
            return;
        }

        indiceSelezionato = tabellaPartite.getSelectionModel().getSelectedIndex();

        if (tabellaPartite.getItems().size() == 1) {
            showOneMatchError();
            return;
        }

        partite.remove(indiceSelezionato);
        tabellaPartite.getItems().remove(indiceSelezionato);
        showEliminatoSucces();
    }

    private void showNoIndexError() {
        infoLabel.setText("Nessuna partita selezionata");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showOneMatchError() {
        infoLabel.setText("La tabella non può essere vuota");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showEliminatoSucces() {
        infoLabel.setText("Partita eliminata");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void fine(ActionEvent event) {
        caricaDati();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void scaricaDati() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestionePartite/partite.ser"))) {

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
