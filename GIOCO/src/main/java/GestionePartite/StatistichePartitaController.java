package GestionePartite;

import java.util.ArrayList;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatistichePartitaController {

    @FXML
    private TableView<Giocatore> statisticheTableView;

    @FXML
    private TableColumn<Giocatore, Integer> giocatoriColumn;

    @FXML
    private TableColumn<Giocatore, Integer> cartePescateColumn;

    @FXML
    private TableColumn<Giocatore, Integer> bombePescateColumn;

    @FXML
    private TableColumn<Giocatore, Integer> puntiTotaliColumn;

    @FXML
    private Button homeButton;

    @FXML
    private Label turniText;

    private Tavolo tavolo;
    private ArrayList<Giocatore> giocatori;


    @FXML
    public void initialize() {
        ottieniStats();
    }

    public void setTavolo(Tavolo t){
        tavolo = t;
        giocatori = new ArrayList<>(tavolo.punteggi.keySet());
    }

    private void ottieniStats(){
        statisticheTableView.setItems(FXCollections.observableArrayList(giocatori));

        giocatoriColumn.setCellValueFactory(new PropertyValueFactory<>("giocatori"));
        cartePescateColumn.setCellValueFactory(new PropertyValueFactory<>("carte pescate"));
        bombePescateColumn.setCellValueFactory(new PropertyValueFactory<>("bombe pescate"));
        puntiTotaliColumn.setCellValueFactory(new PropertyValueFactory<>("punti totali fatti"));
    }

}
