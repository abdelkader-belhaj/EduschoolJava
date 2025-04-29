package tn.eduskool.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.eduskool.entities.Activity;
import tn.eduskool.entities.Commentaire;
import tn.eduskool.services.ServiceCommentaire;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class ActivityDetailController implements Initializable {

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

    @FXML
    private ListView<Commentaire> commentsListView;

    @FXML
    private Button addCommentButton;

    @FXML
    private Button backButton;

    private Activity activity;
    private List<Commentaire> comments;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backButton.setOnAction(this::onBackButtonClicked);
        addCommentButton.setOnAction(this::onAddCommentClicked);

        // Set up comments list view cell factory
        commentsListView.setCellFactory(listView -> new CommentaireListCell());
    }

    public void setActivity(Activity activity) {
        this.activity = activity;

        // Update UI with activity details
        titleLabel.setText(activity.getTitre());
        descriptionLabel.setText(activity.getDescription());
        dateLabel.setText(activity.getDate().toString());
        typeLabel.setText(activity.getTypesActivity());

        // Load activity image
        try {
            String imagePath = "/tn/eduskool/resources/uploads/" + activity.getImageFileName();
            File file = new File(System.getProperty("user.dir") + imagePath);

            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                activityImage.setImage(image);
            } else {
                // Load default image
                Image defaultImage = new Image(
                        getClass().getResourceAsStream("/tn/eduskool/resources/images/default_activity.png"));
                activityImage.setImage(defaultImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading activity image: " + e.getMessage());
        }

        // Load comments
        loadComments();
    }

    private void loadComments() {
        comments = ServiceCommentaire.getCommentairesByActivityId(activity.getId());
        commentsListView.getItems().clear();
        commentsListView.getItems().addAll(comments);
    }

    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActivityGridVIew.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error returning to activities view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddCommentClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commentaire_view.fxml"));
            Parent root = loader.load();

            CommentaireViewController controller = loader.getController();
            controller.setActivityId(activity.getId());

            Stage stage = new Stage();
            stage.setTitle("Add Comment");
            stage.setScene(new Scene(root));

            // When comment window closes, refresh comments
            stage.setOnHidden(e -> loadComments());

            stage.show();
        } catch (IOException e) {
            System.err.println("Error opening comment dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
}