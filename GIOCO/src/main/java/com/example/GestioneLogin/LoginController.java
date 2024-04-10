package com.example.GestioneLogin;

import java.io.IOException;

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

    private final String amministratoreUsername = "Admin";
    private final String amministratorePassword = "123";

    @FXML
    private void checkLogin(ActionEvent event) throws IOException {
        if (amministratoreUsername.equals(username.getText()) && amministratorePassword.equals(password.getText())) {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/GestioneAmministratore/AfterLogin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleSP.css").toExternalForm());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

        } else if (username.getText().isEmpty() || password.getText().isEmpty()) {
            result.setText("Uno o più campi sono vuoti");
        } else {
            result.setText("Nome utente o password errati!");
        }
    }

    @FXML
    private void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleSP.css").toExternalForm());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
