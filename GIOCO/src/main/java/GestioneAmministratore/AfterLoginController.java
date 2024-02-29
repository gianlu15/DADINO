package GestioneAmministratore;

import java.io.IOException;

import GestioneAmministratore.ListaPartite.CreaPartite;
import GestioneAmministratore.ListaPartite.PartiteListView;
import GestioneAmministratore.ListaUtenti.ListViewClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

//Controller di AfterLogin.java (SCENA 3)

public class AfterLoginController {

    @FXML
    private Button modificaUtenti;

    @FXML
    private Button creaPartita;

    @FXML
    private Button logout;

    @FXML
    private Button gestisciPartite;

    @FXML
    private Button creaTorneo;

    @FXML
    private Button gestisciTornei;

    

    @FXML
    public void userLogOut(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestioneLogin/HomeLogin.fxml"));
        Parent root = loader.load();

        // Ottieni lo Stage della finestra di Login
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());
        loginStage.setScene(scene);
    }

    @FXML
    public void modificaUtentiAction(ActionEvent event) throws IOException {
        ListViewClass lw = new ListViewClass();
        Stage lwStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        lw.start(lwStage);
    }

    @FXML
    public void creaPartita(ActionEvent event) throws IOException {
        CreaPartite pw = new CreaPartite();
        Stage pwStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pw.start(pwStage);
    }

    @FXML
    public void gestisciPartite(ActionEvent event) throws IOException {
        PartiteListView plw = new PartiteListView();
        Stage plwStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        plw.start(plwStage);
    }

}
