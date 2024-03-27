package com.example.GestioneAmministratore.ListaTornei;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.example.GestisciFile;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestioneTornei.Torneo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreaTorneiController {

    @FXML
    private TextField nomeTorneoField;

    @FXML
    private Label codiceField;

    @FXML
    private Button generaButton;

    @FXML
    private TextField codiceTorneoField;

    @FXML
    private ChoiceBox<Integer> numGiocatoriChoice;

    @FXML
    private Button creaButton;

    @FXML
    private Label errorLabel;

    private List<Torneo> tornei;
    private Integer[] numeriGiocatori;
    private int numeroCasuale;
    private File file;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() throws URISyntaxException {
        this.tornei = new ArrayList<>();
        this.numeriGiocatori = new Integer[] { 4, 6, 8, 12, 16 };
        this.numeroCasuale = 0;
        this.objectMapper = new ObjectMapper();
        
        String path = GestisciFile.ottieniDirectory();

        this.file= new File(path, "tornei.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("[Lcom.example.GestionePartite.Partita")
                .allowIfBaseType("java.util.List<com.example.GestioneTornei.Torneo>")
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        scaricaTorneiDaFile();
        numGiocatoriChoice.getItems().addAll(numeriGiocatori);
    }

    @FXML
    private void generaCodice(ActionEvent event) {
        Random random = new Random();
        boolean codiceEsistente = false;

        do {
            numeroCasuale = random.nextInt(9000) + 1000;
            for (Torneo p : tornei) {
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
    public void creaTorneo(ActionEvent ev) throws IOException, URISyntaxException {

        String nome = nomeTorneoField.getText();
        Integer numGiocatori = numGiocatoriChoice.getValue();

        if (controlli(nome, numGiocatori))
            return;

        Torneo nuovaTorneo = new Torneo(nome, numGiocatori, numeroCasuale);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/GestioneAmministratore/ListaTornei/torneoGiocatori.fxml"));
        Parent root = loader.load();

        // Ottieni lo Stage della finestra di Login
        Stage loginStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/Style2.css").toExternalForm());
        loginStage.setScene(scene);
        TorneoGiocatoriController pgc = loader.getController();
        pgc.setTorneo(nuovaTorneo);

    }

    private boolean controlli(String nome, Integer numGiocatori) {

        if (nome.isEmpty()) {
            showEmptyError();
            return true;
        }

        if (nome.length() > 17 || nome.length() < 3) {
            showNomeLength();
            return true;
        }

        if (numGiocatori == null) {
            showNumGiocatiriNotSelected();
            return true;
        }

        for (Torneo t : tornei) {
            if (nome.equals(t.getNomeTorneo())) {
                showNomePresente();
                return true;
            }
        }

        if (numeroCasuale < 1000 || numeroCasuale > 9999) {
            showNumeroNonGenerato();
            return true;
        }

        return false;
    }

    private void showNumeroNonGenerato() {
        errorLabel.setText("Codice non generato");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showEmptyError() {
        errorLabel.setText("Campo nome vuoto");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showNomeLength() {
        errorLabel.setText("Il nome è troppo lungo/corto");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showNumGiocatiriNotSelected() {
        errorLabel.setText("Numero giocatori non selezionato");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showNomePresente() {
        errorLabel.setText("Torneo con lo stesso nome già presente");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
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
                   alertCreaFile(file.getName());
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            alertScaricaFile(file.getName());
            e.printStackTrace();
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

}
