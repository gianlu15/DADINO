package com.example.GestioneAmministratore.ListaTornei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.GestioneTornei.Torneo;
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

public class TorneiListViewController {

    @FXML
    private Button rimuoviTorneo;

    @FXML
    private Button salvaEdEsci;

    @FXML
    private Label infoLabel;

    @FXML
    private TableView<Torneo> tabellaTornei;

    @FXML
    private TableColumn<Torneo, String> colonnaNome;

    @FXML
    private TableColumn<Torneo, Integer> colonnaCodice;

    @FXML
    private TableColumn<Torneo, Torneo.Stato> colonnaStato;

     private List<Torneo> tornei;
    private int indiceSelezionato;
    private File file;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.tornei = new ArrayList<>();
        this.indiceSelezionato = -1;
        this.objectMapper = new ObjectMapper();
        this.file = new File("src/main/resources/com/example/FileJson/tornei.json");

        scaricaTorneiDaFile();
        mostraTabella();
    }

    private void mostraTabella() {
        tabellaTornei.setItems(FXCollections.observableArrayList(tornei));

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nomeTorneo"));
        colonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codice"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<>("statoTorneo"));
    }

    @FXML
    private void rimuoviTorneo(ActionEvent e) {
        if (tabellaTornei.getSelectionModel().isEmpty()) {
            showNoIndexError();
            return;
        }

        indiceSelezionato = tabellaTornei.getSelectionModel().getSelectedIndex();

        tornei.remove(indiceSelezionato);
        tabellaTornei.getItems().remove(indiceSelezionato);
        caricaTorneiSuFile();
        showEliminatoSucces();
    }

    private void showNoIndexError() {
        infoLabel.setText("Nessuna torneo selezionato");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showEliminatoSucces() {
        infoLabel.setText("Torneo eliminato");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void fine(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void scaricaTorneiDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (file.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (file.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    return;
                } else {
                    tornei = objectMapper.readValue(file, new TypeReference<List<Torneo>>() {
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
            // Alert impossibile scaricare dal file(?)
            e.printStackTrace();
        }
    }

    private void caricaTorneiSuFile() {
        try {
            objectMapper.writeValue(file, tornei);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
