package com.example.GestioneStatistiche;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.GestisciFile;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGiocoFX.StageGioco;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class StatisticheController {

    @FXML
    private Button esciButton;

    @FXML
    private TableView<Giocatore> tableView;

    @FXML
    private TableColumn<Giocatore, String> giocatoriColumn;

    @FXML
    private TableColumn<Giocatore, Integer> vittorieColumn;

    @FXML
    private TableColumn<Giocatore, Integer> partiteColumn;

    @FXML
    private TableColumn<Giocatore, Integer> carteColumn;

    @FXML
    private TableColumn<Giocatore, Integer> puntiColumn;

    @FXML
    private TableColumn<Giocatore, Integer> bombeColumn;

    @FXML
    private TableColumn<Giocatore, Integer> vittorieColumn1;

    private File fileGiocatori;
    private ObjectMapper objectMapper;
    private List<Giocatore> giocatori;

    @FXML
    public void initialize() throws URISyntaxException {
        this.giocatori = new ArrayList<>();
        this.objectMapper = new ObjectMapper();

                String path = GestisciFile.ottieniDirectory();

        this.fileGiocatori = new File(path, "giocatori.json");

        scaricaGiocatoriDaFile();

        if (giocatori.size() > 0)
            mostraTabella();
    }

    private void mostraTabella() {
        tableView.setItems(FXCollections.observableArrayList(giocatori));

        giocatoriColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        vittorieColumn.setCellValueFactory(new PropertyValueFactory<>("vittorie"));
        vittorieColumn1.setCellValueFactory(new PropertyValueFactory<>("vittorieTorneo"));
        partiteColumn.setCellValueFactory(new PropertyValueFactory<>("partiteGiocate"));
        carteColumn.setCellValueFactory(new PropertyValueFactory<>("carteTotaliPescate"));
        puntiColumn.setCellValueFactory(new PropertyValueFactory<>("puntiTotaliFatti"));
        bombeColumn.setCellValueFactory(new PropertyValueFactory<>("bombettePescate"));
    }

    @FXML
    public void esci(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/GestioneLoginUtente/UtenteBoard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleSP.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(600);
        stage.setScene(scene);
    }

    private void scaricaGiocatoriDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileGiocatori.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (fileGiocatori.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    alertNessunGiocatore();

                    return;
                } else {
                    giocatori = objectMapper.readValue(fileGiocatori, new TypeReference<List<Giocatore>>() {
                    });
                }

            } else {
                System.out.println("Il file non esiste");
                alertNessunGiocatore();
            }

        } catch (IOException e) {
            alertImpossibileTrovareGiocatori();
            e.printStackTrace();
        }
    }

    @FXML
    public void alertNessunGiocatore() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nessun Giocatore!");
        alert.setHeaderText(null);
        alert.setContentText("Non esistono giocatori!");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertImpossibileTrovareGiocatori() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText("Impossibile trovare i giocatori");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

}
