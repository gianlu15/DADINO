package GestioneLogin;

import java.io.IOException;

import GestioneAmministratore.AfterLogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//CONTROLLER DI Login.java (SCENA 2)

public class LoginController {

    @FXML
    private Button button;
    @FXML
    private Label result;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Stage stage;

    private final String amministratoreUsername = "ana";
    private final String amministratorePassword = "123";

    @FXML
    private void checkLogin(ActionEvent event) throws IOException {
        if (amministratoreUsername.equals(username.getText()) && amministratorePassword.equals(password.getText())) {
            result.setText("Success!");
            AfterLogin af = new AfterLogin();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ricavo lo stage da Login
            af.start(stage);

        } else if (username.getText().isEmpty() || password.getText().isEmpty()) {
            result.setText("Please enter your data.");
        } else {
            result.setText("Wrong username or password!");
        }
    }

    @FXML
    private void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeLogin.fxml"));
        Parent root = loader.load();

        // Ottieni lo Stage della finestra di Login
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/StyleSP.css").toExternalForm());

        loginStage.setScene(scene);
    }

}
