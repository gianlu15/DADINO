package com.example.GestioneAmministratore.ListaPartite;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.example.GestisciFile;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestionePartite.Partita;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

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
    private ObjectMapper partitaMapper;

    @FXML
    public void initialize() throws URISyntaxException {
        this.partite = new ArrayList<>();
        this.indiceSelezionato = -1;
        this.partitaMapper = new ObjectMapper();

        String path = GestisciFile.ottieniDirectory();

        this.file= new File(path, "partite.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfBaseType("java.util.List<com.example.GestionePartite.Partita>")
                .build();
        partitaMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

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
                    partite = partitaMapper.readValue(file, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    file.createNewFile();
                    System.out.println("Il file è stato creato con successo.");
                } catch (Exception e) {
                    alertCreaFile(file.getName());
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            alertScaricaFile(file.getName());
            e.printStackTrace();
            return;
        }
    }

    private void caricaPartiteSuFile() {
        try {
            partitaMapper.writeValue(file, partite);
        } catch (IOException e) {
            alertCaricaFile(file.getName());
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
