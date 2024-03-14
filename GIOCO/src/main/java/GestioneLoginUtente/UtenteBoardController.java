package GestioneLoginUtente;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UtenteBoardController {

    @FXML
    private Button accediPartitaButton;

    @FXML
    private Button accediTorneoButton;

    @FXML
    private Button statisticheButton;

    @FXML
    private Button tutorialButton;

    @FXML
    private void accediAPartita(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UtentePartitaBoard.fxml"));
        Parent root = loader.load();

        Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());
        loginStage.setScene(scene);
    }

    @FXML
    private void guardaStatistiche(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneStatistiche/Statistiche.fxml"));
        Parent root = loader.load();

        Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        loginStage.setWidth(700);
        scene.getStylesheets().add(getClass().getResource("/Styles/Style2.css").toExternalForm());
        loginStage.setScene(scene);
    }

    @FXML
    private void backtoHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneLogin/HomeLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
