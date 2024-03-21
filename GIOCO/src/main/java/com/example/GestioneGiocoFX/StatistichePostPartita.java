package com.example.GestioneGiocoFX;

import java.io.IOException;
import java.util.ArrayList;

import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGioco.Tavolo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StatistichePostPartita extends Application {

    private Tavolo tavoloPartita;
    private ArrayList<Giocatore> giocatori;

    public StatistichePostPartita(Tavolo tavoloPartita, ArrayList<Giocatore> giocatori){
        this.tavoloPartita = tavoloPartita;
        this.giocatori = giocatori;
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/GestioneGiocoFX/StatistichePostPartita.fxml"));
        Parent root = loader.load();

        StatistichePostPartitaController controller = loader.getController();

        controller.setStatistiche(tavoloPartita, giocatori);

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();

    }
}
