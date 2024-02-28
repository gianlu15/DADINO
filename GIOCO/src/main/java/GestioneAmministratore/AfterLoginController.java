package GestioneAmministratore;

import java.io.IOException;

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
    private Button logout;

    @FXML
    private Button modificaUtenti;

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

}
