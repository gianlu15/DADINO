package GestioneLogin;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

//CONTROLLER DI HomeLogin.java (SCENA 1)

public class HomeLoginController {

    private Stage stage;

    @FXML
    private void handleAmministratoreButtonClick(ActionEvent event) {
        try {
            Login login = new Login();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ricavo lo stage da HomeLogin
            login.start(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
