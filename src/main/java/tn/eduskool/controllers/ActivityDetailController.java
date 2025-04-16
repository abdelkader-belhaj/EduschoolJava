package tn.eduskool.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.eduskool.entities.Activity;
import tn.eduskool.entities.Commentaire;
import tn.eduskool.services.ServiceCommentaire;
import java.util.List;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class ActivityDetailController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label activityTitleLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label dateLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label imagePathLabel;
    @FXML
    private ImageView activityImageView;
    @FXML
    private Label commentCountLabel;
    @FXML
    private TableView<Commentaire> commentTableView;
    @FXML
    private TableColumn<Commentaire, Integer> colCommentId;
    @FXML
    private TableColumn<Commentaire, String> colCommentContent;
    @FXML
    private TableColumn<Commentaire, Integer> colCommentRating;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        // Initialiser les colonnes de la table des commentaires
        colCommentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCommentContent.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colCommentRating.setCellValueFactory(new PropertyValueFactory<>("note"));

        // Ajouter un rendu personnalisé pour la colonne de note
        colCommentRating.setCellFactory(column -> {
            return new TableCell<Commentaire, Integer>() {
                @Override
                protected void updateItem(Integer note, boolean empty) {
                    super.updateItem(note, empty);

                    if (empty || note == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(note + "/5");

                        // Colorer en fonction de la note
                        if (note >= 4) {
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        } else if (note >= 2) {
                            setStyle("-fx-text-fill: orange;");
                        } else {
                            setStyle("-fx-text-fill: red;");
                        }
                    }
                }
            };
        });
    }

    public void setActivity(Activity activity) {
        if (activity == null)
            return;

        // Mettre à jour le titre de la fenêtre
        titleLabel.setText("Détails de l'activité: " + activity.getTitre());

        // Remplir les informations de l'activité
        idLabel.setText(String.valueOf(activity.getId()));
        activityTitleLabel.setText(activity.getTitre());
        descriptionTextArea.setText(activity.getDescription());
        dateLabel.setText(activity.getDate().format(dateFormatter));
        statusLabel.setText(activity.isApproved() ? "Approuvée" : "En attente");
        typeLabel.setText(activity.getTypesActivity());
        createdAtLabel.setText(activity.getCreatedAt().format(dateFormatter));

        // Style du statut
        if (activity.isApproved()) {
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }

        // Charger l'image si elle existe
        if (activity.getImageFileName() != null && !activity.getImageFileName().isEmpty()) {
            try {
                File imageFile = new File("uploads-img/" + activity.getImageFileName());

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    activityImageView.setImage(image);
                    imagePathLabel.setText(activity.getImageFileName());
                } else {
                    imagePathLabel.setText("Image non trouvée: " + activity.getImageFileName());
                }

            } catch (Exception e) {
                imagePathLabel.setText("Erreur de chargement de l'image: " + e.getMessage());
            }
        } else {
            imagePathLabel.setText("Aucune image disponible");
        }

        // Afficher les commentaires
        commentCountLabel.setText(String.valueOf(activity.getCommentaires().size()));
        List<Commentaire> commentaires = ServiceCommentaire.getCommentairesByActivityId(activity.getId());
        commentTableView.setItems(FXCollections.observableArrayList(commentaires));
        commentCountLabel.setText(String.valueOf(commentaires.size()));

    }

    @FXML
    private void closeWindow() {
        // Fermer la fenêtre
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}