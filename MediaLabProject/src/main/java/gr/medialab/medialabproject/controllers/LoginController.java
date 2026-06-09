package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.User;
import gr.medialab.medialabproject.service.DataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = DataService.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/gr/medialab/medialabproject/main.fxml")
                );
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.initUser(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("MediaLab Documents");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}