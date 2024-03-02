package GestioneLoginUtente;

import GestionePartite.Partita;
import GestioneUtenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import GestioneGioco.GiocoController;
import GestioneGioco.Tavolo;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

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

    private List<Utente> utenti = new ArrayList<>();
    private List<Partita> partite = new ArrayList<>();
    private Partita partitaAttiva;
    private boolean partitaTrovata = false;
    private int giocatoriMancanti;

    @FXML
    public void initialize() {
        scaricaDatiUtenti();
        scaricaDatiPartite();

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
                avviaPartita(event);
            }

        } else {
            for (Partita p : partite) {
                if (codice == p.getCodice()) {
                    partitaAttiva = p;
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
                avviaPartita(event);
            }
        }
    }

    private void avviaPartita(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneGioco/StageGioco.fxml"));
        Parent root = loader.load();

        // Ottieni il controller
        GiocoController controller = loader.getController();

        Tavolo t = partitaAttiva.getTavolo();

        // Imposta il tavolo nel controller
        controller.setTavolo(t);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        controller.setStage(primaryStage);

        // Mostra la scena
        Scene scene = new Scene(root, 900, 500);
        scene.getStylesheets().add(getClass().getResource("/GestioneGioco/StageGioco.css").toExternalForm());
        primaryStage.setScene(scene);

        // Avvia l'esecuzione della partita in un thread separato
        Thread partitaThread = new Thread(() -> {
            controller.esegui();
        });

        partitaThread.start();
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
        giocatoriMancanti = partitaAttiva.getGiocatoriAcceduti();
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

    private void scaricaDatiUtenti() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {

            System.out.println("File utenti trovato in PartiteGiocatoriController");
            utenti = (List<Utente>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestioneFileUtenti/utenti.ser");
            try {
                if (file.createNewFile()) {

                    Utente nuovoUtente = new Utente("Admin");
                    utenti.add(nuovoUtente);
                    caricaDatiUtenti();

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

    private void scaricaDatiPartite() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestionePartite/partite.ser"))) {

            System.out.println("File partite trovato in CreaPartiteController");
            partite = (List<Partita>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestionePartite/partite.ser");
            try {
                if (file.createNewFile()) {

                    Partita nuova = new Partita("Default", 0, 0000);
                    partite.add(nuova);
                    caricaDatiPartite();

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

    public void caricaDatiPartite() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestionePartite/partite.ser"))) {
            outputStream.writeObject(partite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void caricaDatiUtenti() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/GestioneFileUtenti/utenti.ser"))) {
            outputStream.writeObject(utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
