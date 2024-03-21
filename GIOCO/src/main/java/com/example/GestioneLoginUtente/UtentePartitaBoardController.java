package com.example.GestioneLoginUtente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.example.GestioneGiocoFX.StageGioco;
import com.example.GestionePartite.Partita;
import com.example.GestionePartite.Partita.Stato;
import com.example.GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UtentePartitaBoardController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField codiceFiled;

    @FXML
    private Button accediButton;

    @FXML
    private Label infoLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    private List<Utente> utenti;
    private List<Partita> partite;
    private ArrayList<String> accessiBackup;
    private Partita partitaAttiva;
    private boolean partitaTrovata;
    private int giocatoriMancanti;
    private Stato statoPartita;
    private File filePartite;
    private File fileUtenti;
    private ObjectMapper objectMapper;
    private ObjectMapper partitaMapper;

    @FXML
    public void initialize() {
        this.utenti = new ArrayList<>();
        this.partite = new ArrayList<>();
        this.partitaTrovata = false;
        this.objectMapper = new ObjectMapper();
        this.partitaMapper = new ObjectMapper();

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("com.example")
            .allowIfSubType("java.util.ArrayList")
            .allowIfBaseType("java.util.List<com.example.GestionePartite.Partita>")
            .build();
        partitaMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        this.filePartite = new File("src/main/resources/com/example/FileJson/partite.json");
        this.fileUtenti = new File("src/main/resources/com/example/FileJson/utenti.json");

        scaricaUtentiDaFile();
        scaricaPartiteDaFile();

        titleLabel.setText("ACCEDI ALLA PARTITA");
    }

    @FXML
    private void backToBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UtenteBoard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleSP.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void accedi(ActionEvent event) throws Exception {

        String username = usernameField.getText();
        String codiceString = codiceFiled.getText();

        if (controlli(username, codiceString)) {
            return;
        }

        int codice = Integer.parseInt(codiceString);

        if (partitaTrovata) {

            if (codice != partitaAttiva.getCodice()) {
                showCodiceDiversoError();
                return;
            }

            if (partitaAttiva.controlloUtente(username)) {
                partitaAttiva.effettuaAccesso(username);
                showAccessoEffettuato();
                setTitleLable();
                usernameField.setText("");
                codiceFiled.setText("");
            } else {
                showNotAPlayerError();
                return;
            }

            if (partitaAttiva.pronta()) {
                partitaAttiva.ripristinaAccessi(accessiBackup);
                avviaPartita(event);
            }

        } else {
            for (Partita p : partite) {
                if (codice == p.getCodice()) {
                    partitaAttiva = p;
                    statoPartita = partitaAttiva.getStatoPartita();

                    if (statoPartita == Stato.Terminata) {
                        showPartitaTerminataError();
                        return;
                    }

                    accessiBackup = new ArrayList<>(partitaAttiva.getAccessi());
                    partitaTrovata = true;
                }
            }

            titleLabel.setText("PARTITA:" + "\"" + partitaAttiva.getNome() + "\"");

            if (partitaAttiva.controlloUtente(username)) {
                partitaAttiva.effettuaAccesso(username);
                showAccessoEffettuato();
                setTitleLable();
                usernameField.setText("");
                codiceFiled.setText("");
            } else {
                showNotAPlayerError();
                return;
            }

            if (partitaAttiva.pronta()) {
                partitaAttiva.ripristinaAccessi(accessiBackup);
                avviaPartita(event);
            }
        }
    }

    private void avviaPartita(ActionEvent event) throws Exception {
        StageGioco sg = new StageGioco(partitaAttiva, null);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sg.start(primaryStage);
    }

    private boolean controlli(String username, String codice) {

        int numero;

        if (username.isEmpty()) {
            showEmptyError();
            return true;
        }

        if (codice.isEmpty()) {
            showEmptyError();
            return true;
        }

        try {
            numero = Integer.parseInt(codice);
        } catch (NumberFormatException e) {
            showNotNumberError();
            return true;
        }

        if (codice.length() != 4) {
            showCodiceLengthError();
            return true;
        }

        boolean codiceEsistente = false;
        for (Partita p : partite) {
            if (numero == p.getCodice()) {
                codiceEsistente = true;
                break;
            }
        }

        boolean usernameEsistente = false;
        for (Utente u : utenti) {
            if (username.equals(u.getNome())) {
                usernameEsistente = true;
                break;
            }
        }

        if (!codiceEsistente) {
            showCodiceNonEsistenteError();
            return true;
        }

        if (!usernameEsistente) {
            showUsernameNonEsistenteError();
            return true;
        }

        return false;
    }

    private void setTitleLable() {
        giocatoriMancanti = partitaAttiva.ottieniAccessi();
        if (giocatoriMancanti > 1)
            subtitleLabel.setText("Mancano " + giocatoriMancanti + " giocatori per l'accesso");
        else
            subtitleLabel.setText("Manca " + giocatoriMancanti + " giocatore per l'accesso");
    }

    private void showEmptyError() {
        infoLabel.setText("Uno o più campi sono vuoti");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showNotNumberError() {
        infoLabel.setText("Il codice deve contenere solo numeri");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showPartitaTerminataError() {
        infoLabel.setText("Partita terminata");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showCodiceDiversoError() {
        infoLabel.setText("Il codice non corrisponde alla partita selezionata");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showAccessoEffettuato() {
        infoLabel.setText("Accesso effettuato");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showNotAPlayerError() {
        infoLabel.setText("Non fai parte di questa partita!");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showCodiceLengthError() {
        infoLabel.setText("Il codice deve contenere 4 numeri");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showCodiceNonEsistenteError() {
        infoLabel.setText("Il codice non esiste");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showUsernameNonEsistenteError() {
        infoLabel.setText("Username non esistente");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    @FXML
    public void alertNessunaPartita() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nessuna Partita");
        alert.setHeaderText(null);
        alert.setContentText("Non esiste nessuna partita.");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertImpossibileTrovarePartita() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText("Impossibile Trovare Partita");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    private void scaricaPartiteDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (filePartite.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (filePartite.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    alertNessunaPartita();

                    return;
                } else {
                    partite = partitaMapper.readValue(filePartite, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                System.out.println("Il file non esiste");
                alertNessunaPartita();
            }

        } catch (IOException e) {
            alertImpossibileTrovarePartita();
            e.printStackTrace();
        }
    }

    @FXML
    public void alertNessunUtente() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nessun Utente!");
        alert.setHeaderText(null);
        alert.setContentText("Nessun Utente creato!");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertImpossibileTrovareUtenti() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText("Impossibile Trovare Utenti");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/com/example/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    private void scaricaUtentiDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileUtenti.exists()) {

                System.out.println("Il file esiste già.");
                utenti = objectMapper.readValue(fileUtenti, new TypeReference<List<Utente>>() {
                });

            } else {
                System.out.println("Il file non esiste");
                alertNessunUtente();
            }

        } catch (IOException e) {
            alertImpossibileTrovareUtenti();
            e.printStackTrace();
        }
    }
}
