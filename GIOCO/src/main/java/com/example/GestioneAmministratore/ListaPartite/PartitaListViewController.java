package com.example.GestioneAmministratore.ListaPartite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.GestionePartite.Partita;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


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

    private List<Partita> partite;
    private int indiceSelezionato;
    private File file;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.partite = new ArrayList<>();
        this.indiceSelezionato = -1;
        this.objectMapper = new ObjectMapper();
        this.file = new File("src/main/resources/com/example/FileJson/partite.json");

        scaricaPartiteDaFile();
        mostraTabella();
    }

    private void mostraTabella() {
        tabellaPartite.setItems(FXCollections.observableArrayList(partite));

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codice"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<>("statoPartita"));
    }

    @FXML
    private void rimuoviPartita(ActionEvent e) {
        if (tabellaPartite.getSelectionModel().isEmpty()) {
            showNoIndexError();
            return;
        }

        indiceSelezionato = tabellaPartite.getSelectionModel().getSelectedIndex();

        partite.remove(indiceSelezionato);
        tabellaPartite.getItems().remove(indiceSelezionato);
        caricaPartiteSuFile();

        showEliminatoSucces();
    }

    private void scaricaPartiteDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (file.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (file.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    return;
                } else {
                    partite = objectMapper.readValue(file, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    file.createNewFile();
                    System.out.println("Il file è stato creato con successo.");
                } catch (Exception e) {
                    // Alert impossibile creare il file(?)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void caricaPartiteSuFile() {
        try {
            objectMapper.writeValue(file, partite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNoIndexError() {
        infoLabel.setText("Nessuna partita selezionata");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showEliminatoSucces() {
        infoLabel.setText("Partita eliminata");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void fine(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
