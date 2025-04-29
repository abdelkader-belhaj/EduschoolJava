package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;

public class HomeController {

    @FXML
    private void handleShowCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendar_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Calendrier des Activités");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir le calendrier: " + e.getMessage());
            alert.showAndWait();
        }
    }
}