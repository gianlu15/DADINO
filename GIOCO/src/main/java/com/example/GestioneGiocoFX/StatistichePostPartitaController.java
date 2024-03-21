package com.example.GestioneGiocoFX;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGioco.Tavolo;
import com.example.GestioneLogin.HomeLogin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class StatistichePostPartitaController {

    @FXML
    private TableView<GiocatoreStat> tableView;

    @FXML
    private TableColumn<GiocatoreStat, String> giocatoreColumn;

    @FXML
    private TableColumn<GiocatoreStat, String> carteColumn;

    @FXML
    private TableColumn<GiocatoreStat, String> bombeColumn;

    @FXML
    private TableColumn<GiocatoreStat, String> puntiColumn;

    @FXML
    private Button esciButton;

    @FXML
    private Label turniText;

    private Tavolo tavoloDellaPartita;
    private ArrayList<String> giocatoriNomi;
    private ArrayList<Integer> carte;
    private ArrayList<Integer> bombe;
    private ArrayList<Integer> punti;

    private ArrayList<GiocatoreStat> stats;

    private Stage primaryStage;

    @FXML
    public void esci(ActionEvent event) throws Exception {
        HomeLogin hl = new HomeLogin();
        hl.start(primaryStage);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setStatistiche(Tavolo tavolo, ArrayList<Giocatore> giocatori) {
        this.tavoloDellaPartita = tavolo;

        this.giocatoriNomi = new ArrayList<>();
        this.stats = new ArrayList<>();

        turniText.setText("Giri totali: " + tavoloDellaPartita.getTurniTotali());

        for (Giocatore giocatore : giocatori) {
            this.giocatoriNomi.add(giocatore.getNome());
        }

        carte = new ArrayList<>(
                Arrays.asList(Arrays.stream(tavoloDellaPartita.getCartePescate()).boxed().toArray(Integer[]::new)));

        bombe = new ArrayList<>(
                Arrays.asList(Arrays.stream(tavoloDellaPartita.getBombePescate()).boxed().toArray(Integer[]::new)));

        punti = new ArrayList<>(
                Arrays.asList(Arrays.stream(tavoloDellaPartita.getPuntiTotali()).boxed().toArray(Integer[]::new)));

        mostraTabella();
    }

    public void mostraTabella() {
        for (int i = 0; i < giocatoriNomi.size(); i++) {
            stats.add(new GiocatoreStat(giocatoriNomi.get(i),
                    carte.get(i),
                    bombe.get(i),
                    punti.get(i)));
        }

        tableView.setItems(FXCollections.observableArrayList(stats));
        giocatoreColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carteColumn.setCellValueFactory(new PropertyValueFactory<>("carte"));
        bombeColumn.setCellValueFactory(new PropertyValueFactory<>("bombe"));
        puntiColumn.setCellValueFactory(new PropertyValueFactory<>("punti"));

    }

    public static class GiocatoreStat {
        private String nome;
        private Integer carte;
        private Integer bombe;
        private Integer punti;

        private GiocatoreStat(String nome, Integer carte, Integer bombe, Integer punti) {
            this.nome = nome;
            this.carte = carte;
            this.bombe = bombe;
            this.punti = punti;
        }

        public String getNome() {
            return nome;
        }

        public Integer getCarte() {
            return carte;
        }

        public Integer getBombe() {
            return bombe;
        }

        public Integer getPunti() {
            return punti;
        }
    }

}
