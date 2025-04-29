package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import tn.eduskool.entities.Activity;
import tn.eduskool.services.ServiceActivity;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ActivitiesGridViewController implements Initializable {

    @FXML
    private FlowPane activitiesGrid;

    @FXML
    private Button addActivityButton;

    @FXML
    private Button helpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addActivityButton.setOnAction(event -> openActivityManagement());
        helpButton.setOnAction(event -> openHelpWindow());
        loadActivities();
    }

    private void loadActivities() {
        activitiesGrid.getChildren().clear();

        List<Activity> activities = ServiceActivity.getAllActivities();

        for (Activity activity : activities) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/activityGradlItem.fxml"));
                Parent activityCard = loader.load();

                ActivityCardItemController controller = loader.getController();
                controller.setActivity(activity);

                activityCard.setOnMouseClicked(event -> openCommentView(activity));

                activitiesGrid.getChildren().add(activityCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openActivityManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity_view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestion des activités");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> loadActivities());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openHelpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/help.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            // Appliquer les mêmes styles que la fenêtre principale
            scene.getStylesheets().addAll(addActivityButton.getScene().getStylesheets());

            stage.setTitle("Aide - EduSkool");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCommentView(Activity activity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commentaire_view.fxml"));
            Parent root = loader.load();

            CommentaireViewController controller = loader.getController();
            controller.setActivityId(activity.getId());

            Stage stage = new Stage();
            stage.setTitle("Commentaires - " + activity.getTitre());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshActivities() {
        loadActivities();
    }
}