package GestioneAmministratore.ListaPartite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import GestionePartite.Partita;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private List<Partita> partite = new ArrayList<>();

    @FXML
    public void initialize() {
        scaricaDati();
        mostraTabella();
    }

    private void mostraTabella() {
        tabellaPartite.setItems(FXCollections.observableArrayList(partite));

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codice"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
    }

    private void scaricaDati() {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/GestionePartite/partite.ser"))) {

            partite = (List<Partita>) inputStream.readObject();

        } catch (FileNotFoundException e) { // Se il file non esiste...
            File file = new File("src/main/resources/GestionePartite/partite.ser");
            try {
                if (file.createNewFile()) {
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
}
