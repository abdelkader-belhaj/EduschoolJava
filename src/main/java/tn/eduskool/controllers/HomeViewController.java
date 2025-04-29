package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeViewController {

    @FXML
    private void navigateToActivities() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActivityGridVIew.fxml"));
            Parent activitiesView = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nos Activités");
            stage.setScene(new Scene(activitiesView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}