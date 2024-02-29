package GestioneLogin;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//CONTROLLER DI HomeLogin.java (SCENA 1)

public class HomeLoginController {

    private Stage stage;

    @FXML
    private void handleAmministratoreButtonClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ricavo lo stage da HomeLogin
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
