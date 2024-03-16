package GestioneLoginUtente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneGiocoFX.StageGioco;
import GestioneGiocoTorneoFX.VisualizzaTorneo3Controller;
import GestioneGiocoTorneoFX.VisualizzaTorneo7Controller;
import GestioneTornei.Torneo;
import GestioneTornei.Torneo.Stato;
import GestioneUtenti.Utente;
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

public class UtenteTorneoBoardController {

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
    private List<Torneo> tornei;
    private ArrayList<String> accessiBackup;
    private Torneo torneoAttivo;
    private boolean torneoTrovato;
    private int giocatoriMancanti;
    private Stato statoTorneo;
    private File fileTornei;
    private File fileUtenti;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.utenti = new ArrayList<>();
        this.tornei = new ArrayList<>();
        this.torneoTrovato = false;
        this.objectMapper = new ObjectMapper();
        this.fileTornei = new File("src/main/resources/FileJson/tornei.json");
        this.fileUtenti = new File("src/main/resources/FileJson/utenti.json");

        scaricaUtentiDaFile();
        scaricaTorneiDaFile();

        titleLabel.setText("ACCEDI AL TORNEO");
    }

    @FXML
    private void backToBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UtenteBoard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

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

        if (torneoTrovato) {

            if (codice != torneoAttivo.getCodice()) {
                showCodiceDiversoError();
                return;
            }

            if (torneoAttivo.controlloUtente(username)) {
                torneoAttivo.effettuaAccesso(username);
                showAccessoEffettuato();
                setTitleLable();
                usernameField.setText("");
                codiceFiled.setText("");
            } else {
                showNotAPlayerError();
                return;
            }

            if (torneoAttivo.pronto()) {
                torneoAttivo.ripristinaAccessi(accessiBackup);
                avviaTorneo(event);
            }

        } else {
            for (Torneo t : tornei) {
                if (codice == t.getCodice()) {
                    torneoAttivo = t;
                    statoTorneo = torneoAttivo.getStatoTorneo();

                    if (statoTorneo == Stato.Terminato) {
                        showTorneoTerminatoError();
                        return;
                    }

                    accessiBackup = new ArrayList<>(torneoAttivo.getAccessi());
                    torneoTrovato = true;
                }
            }

            titleLabel.setText("TORNEO:" + "\"" + torneoAttivo.getNomeTorneo() + "\"");

            if (torneoAttivo.controlloUtente(username)) {
                torneoAttivo.effettuaAccesso(username);
                showAccessoEffettuato();
                setTitleLable();
                usernameField.setText("");
                codiceFiled.setText("");
            } else {
                showNotAPlayerError();
                return;
            }

            if (torneoAttivo.pronto()) {
                torneoAttivo.ripristinaAccessi(accessiBackup);
                avviaTorneo(event);
            }
        }
    }

    private void avviaTorneo(ActionEvent event) throws Exception {

        FXMLLoader loader;
        Parent root;

        if (torneoAttivo.getNumGiocatori() < 10) {
            loader = new FXMLLoader(getClass().getResource("/GestioneGiocoFX/visualizzazioneTornei3.fxml"));
            root = loader.load();
            VisualizzaTorneo3Controller vtc = loader.getController();
            vtc.setTorneo(torneoAttivo);
        } else{
            loader = new FXMLLoader(getClass().getResource("/GestioneGiocoFX/visualizzazioneTornei7.fxml"));
            root = loader.load();
            VisualizzaTorneo7Controller vtc = loader.getController();
            vtc.setTorneo(torneoAttivo);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/Style2.css").toExternalForm());
        stage.setScene(scene);
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
        for (Torneo t : tornei) {
            if (numero == t.getCodice()) {
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
        giocatoriMancanti = torneoAttivo.ottieniAccessi();
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

    private void showTorneoTerminatoError() {
        infoLabel.setText("Torneo terminata");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showCodiceDiversoError() {
        infoLabel.setText("Il codice non corrisponde alla torneo selezionato");
        infoLabel.setStyle("-fx-text-fill: #da2c38;");
    }

    private void showAccessoEffettuato() {
        infoLabel.setText("Accesso effettuato");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void showNotAPlayerError() {
        infoLabel.setText("Non fai parte di questo torneo!");
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
    public void alertNessunTorneo() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nessuna torneo");
        alert.setHeaderText(null);
        alert.setContentText("Non esiste nessun torneo.");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertImpossibileTrovareTorneo() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText("Impossibile trovare tornei");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    private void scaricaTorneiDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileTornei.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (fileTornei.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    alertNessunTorneo();

                    return;
                } else {
                    tornei = objectMapper.readValue(fileTornei, new TypeReference<List<Torneo>>() {
                    });
                }

            } else {
                System.out.println("Il file non esiste");
                alertNessunTorneo();
            }

        } catch (IOException e) {
            alertImpossibileTrovareTorneo();
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
                .add(StageGioco.class.getResource("/Styles/alertStyle.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    public void alertImpossibileTrovareUtenti() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText("Impossibile Trovare Utenti");
        alert.getDialogPane().getStylesheets()
                .add(StageGioco.class.getResource("/Styles/alertStyle.css").toExternalForm());

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
