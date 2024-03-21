package com.example.GestioneAmministratore.ListaPartite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.example.GestionePartite.Partita;
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

    private List<Partita> partite;
    private Integer[] numeriGiocatori;
    private int numeroCasuale;
    private File file;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.partite = new ArrayList<>();
        this.numeriGiocatori = new Integer[] { 2, 3, 4 };
        this.numeroCasuale = 0;
        this.objectMapper = new ObjectMapper();
        this.file = new File("src/main/resources/com/example/FileJson/partite.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfBaseType("java.util.List<com.example.GestionePartite.Partita>")
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        scaricaPartiteDaFile();
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

        if (controlli(nome, numGiocatori))
            return;

        Partita nuovaPartita = new Partita(nome, numGiocatori, numeroCasuale);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/GestioneAmministratore/ListaPartite/partitaGiocatori.fxml"));
        Parent root = loader.load();

        Stage loginStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/Style2.css").toExternalForm());
        loginStage.setScene(scene);
        PartitaGiocatoriController pgc = loader.getController();
        pgc.setPartita(nuovaPartita);

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

        for (Partita p : partite) {
            if (nome.equals(p.getNome())) {
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
            // Alert impossibile scaricare dal file(?)
            e.printStackTrace();
        }
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
        errorLabel.setText("Partita con lo stesso nome già presente");
        errorLabel.setStyle("-fx-text-fill: #da2c38;");
    }
}
