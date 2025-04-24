package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Button homeBtn, profileBtn;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        homeBtn.setOnAction(e -> messageLabel.setText("Mode Accueil"));
        profileBtn.setOnAction(e -> messageLabel.setText("Mode Profil"));
    }
}