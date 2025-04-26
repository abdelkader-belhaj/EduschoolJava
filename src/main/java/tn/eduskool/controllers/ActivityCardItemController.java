package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.eduskool.entities.Activity;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ActivityCardItemController {
    @FXML
    private VBox activityCard;
    @FXML
    private ImageView activityImage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label typeLabel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
        titleLabel.setText(truncateText(activity.getTitre(), 50));
        descriptionLabel.setText(truncateText(activity.getDescription(), 100));
        dateLabel.setText(activity.getDate().format(DATE_FORMATTER));
        typeLabel.setText(activity.getTypesActivity());
        loadActivityImage(activity.getImageFileName());
        setupCardBehavior();
    }

    private String truncateText(String text, int limit) {
        if (text == null)
            return "";
        return text.length() > limit ? text.substring(0, limit - 3) + "..." : text;
    }

    private void loadActivityImage(String imageFileName) {
        if (imageFileName != null && !imageFileName.isEmpty()) {
            File imageFile = new File("uploads-img/" + imageFileName);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString(), true);
                activityImage.setImage(image);
                activityImage.setFitWidth(300);
                activityImage.setFitHeight(200);
                activityImage.setPreserveRatio(true);
            } else {
                loadDefaultImage();
            }
        } else {
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-activity.png"));
            activityImage.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Error loading default image: " + e.getMessage());
        }
    }

    private void setupCardBehavior() {
        // Double-clic pour ouvrir les commentaires
        activityCard.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openComments();
            }
        });
    }

    private void openComments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commentaire_view.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle pour ses styles
            Scene currentScene = activityCard.getScene();
            Scene scene = new Scene(root);

            // Appliquer les mêmes styles
            if (currentScene != null && !currentScene.getStylesheets().isEmpty()) {
                scene.getStylesheets().addAll(currentScene.getStylesheets());
            }

            CommentaireViewController controller = loader.getController();
            controller.setActivityId(activity.getId());

            Stage stage = new Stage();
            stage.setTitle("Commentaires - " + activity.getTitre());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture des commentaires: " + e.getMessage());
            e.printStackTrace();
        }
    }
}