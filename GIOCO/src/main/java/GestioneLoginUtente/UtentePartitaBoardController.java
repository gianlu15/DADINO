package GestioneLoginUtente;

import GestionePartite.Partita;
import GestionePartite.Partita.Stato;
import GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import GestioneGioco.GiocoController;

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
    private boolean partitaTrovata = false;
    private int giocatoriMancanti;
    private Stato statoPartita;
    private File filePartite;
    private File fileUtenti;
    private ObjectMapper objectMapper;

    @FXML
    public void initialize() {
        this.utenti = new ArrayList<>();
        this.partite = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.filePartite = new File("src/main/resources/FileJson/partite.json");
        this.fileUtenti = new File("src/main/resources/FileJson/utenti.json");

        scaricaUtentiDaFile();
        scaricaPartiteDaFile();

        titleLabel.setText("ACCEDI ALLA PARTITA");
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
                if (statoPartita == Stato.Pronta) {
                    partitaAttiva.ripristinaAccessi(accessiBackup);
                    avviaPartita(event);
                } else if (statoPartita == Stato.Sospesa) {
                    partitaAttiva.ripristinaAccessi(accessiBackup);
                    riprendiPartita(event, codice);
                } else {
                    showPartitaTerminataError();
                    return;
                }
            }

        } else {
            for (Partita p : partite) {
                if (codice == p.getCodice()) {
                    partitaAttiva = p;
                    statoPartita = partitaAttiva.getStatoPartita();
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
                if (statoPartita == Stato.Pronta) {
                    partitaAttiva.ripristinaAccessi(accessiBackup);
                    avviaPartita(event);
                } else if (statoPartita == Stato.Sospesa) {
                    partitaAttiva.ripristinaAccessi(accessiBackup);
                    riprendiPartita(event, codice);
                } else {
                    showPartitaTerminataError();
                    return;
                }
            }
        }
    }

    private void avviaPartita(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneGioco/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller #1
        GiocoController controller = loader.getController();

        // Crea il tavolo nel controller #2-a
        controller.creaNuovoTavolo(partitaAttiva);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Imposta lo stage nel controller #3
        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/GestioneGioco/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);

        // Avvia l'esecuzione della partita in un thread separato #4
        Thread partitaThread = new Thread(() -> {
            controller.esegui();
        });

        partitaThread.start();

        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // Consuma l'evento per evitare la chiusura immediata della finestra

            // Mostra un Alert per confermare la chiusura
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma chiusura");
            alert.setHeaderText("La partita verrà sospesa");
            alert.setContentText("I progressi non salvati verranno persi.");

            // Aggiungi pulsanti al dialogo
            ButtonType btnYes = new ButtonType("OK", ButtonBar.ButtonData.YES);

            alert.getButtonTypes().setAll(btnYes);

            // Mostra e gestisci la risposta dell'utente
            alert.showAndWait().ifPresent(result -> {
                if (result == btnYes) {
                    partitaAttiva.setStatoPartita(Stato.Sospesa);
                    controller.interrompiPartita(partitaAttiva);
                    primaryStage.close();
                    partitaThread.interrupt();
                }
            });
        });

    }

    private void riprendiPartita(ActionEvent event, int codice) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneGioco/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller #1
        GiocoController controller = loader.getController();

        // Trova e imposta il tavolo nel controller #2-b
        controller.reimpostaTavolo(partitaAttiva);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Imposta lo stage nel controller #3
        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/GestioneGioco/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);

        // Avvia l'esecuzione della partita in un thread separato #4
        Thread partitaThread = new Thread(() -> {
            controller.esegui();
        });

        partitaThread.start();

        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // Consuma l'evento per evitare la chiusura immediata della finestra

            // Mostra un Alert per confermare la chiusura
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma chiusura");
            alert.setHeaderText("La partita verrà sospesa");
            alert.setContentText("I progressi non salvati verranno persi.");

            // Aggiungi pulsanti al dialogo
            ButtonType btnYes = new ButtonType("OK", ButtonBar.ButtonData.YES);

            alert.getButtonTypes().setAll(btnYes);

            // Mostra e gestisci la risposta dell'utente
            alert.showAndWait().ifPresent(result -> {
                if (result == btnYes) {
                    partitaAttiva.setStatoPartita(Stato.Sospesa);
                    controller.interrompiPartita(partitaAttiva);
                    primaryStage.close();
                    partitaThread.interrupt();
                }
            });
        });
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

    private void scaricaPartiteDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (filePartite.exists()) {
                System.out.println("Il file esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (filePartite.length() == 0) {
                    System.out.println("Il file JSON è vuoto.");
                    return;
                } else {
                    partite = objectMapper.readValue(filePartite, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                System.out.println("Il file non esiste");
                // Alert nessuna partita creata!
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
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
                // TODO Alert nessun utente creato!
            }

        } catch (IOException e) {
            // Alert(?)>
            e.printStackTrace();
        }
    }
}
