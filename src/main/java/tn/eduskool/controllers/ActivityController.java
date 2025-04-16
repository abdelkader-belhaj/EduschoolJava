package tn.eduskool.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.eduskool.entities.Activity;
import tn.eduskool.services.ServiceActivity;

public class ActivityController {

    @FXML
    private ListView<Activity> activityListView;
    @FXML
    private Button addBUTT, deleteBtn, refreshBtn, updateBtn, showCommentsBtn, chooseImgBtn;
    @FXML
    private CheckBox apprBOX;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField descriptionTXT, idField, imgTXT, titreTXT, typeTXT;
    @FXML
    private VBox inputBox, listContainer;
    @FXML
    private ImageView imageView;
    @FXML
    private Button helpBtn;

    @FXML
    public void initialize() {
        addBUTT.setOnAction(event -> ajouterActivite());
        deleteBtn.setOnAction(event -> supprimerActivite());
        updateBtn.setOnAction(event -> modifierActivite());
        refreshBtn.setOnAction(event -> afficherActivites());
        showCommentsBtn.setOnAction(event -> afficherCommentaires());
        chooseImgBtn.setOnAction(event -> choisirImage());
        helpBtn.setOnAction(event -> ouvrirAide());

        afficherActivites(); // Charger dès le démarrage
    }

    private void ajouterActivite() {
        if (!validerTousLesChamps()) {
            return;
        }

        String titre = titreTXT.getText();
        String description = descriptionTXT.getText();
        LocalDate selectedDate = datePicker.getValue();
        String typesActivity = typeTXT.getText();
        String imageFileName = imgTXT.getText();
        boolean isApproved = apprBOX.isSelected();

        try {
            LocalDateTime date = selectedDate.atStartOfDay();
            LocalDateTime createdAt = LocalDateTime.now();

            boolean success = ServiceActivity.createActivity(titre, description, date, imageFileName,
                    isApproved, typesActivity, createdAt);

            if (success) {
                afficherAlerte(AlertType.INFORMATION, "Succès", "L'activité a été ajoutée avec succès.");
                afficherActivites();
                viderChamps();
            } else {
                afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'activité.");
            }
        } catch (DateTimeParseException e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Format de date invalide.");
        }
    }

    private void supprimerActivite() {
        if (!validerChampId()) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());

            // Confirmation de suppression
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette activité ?");
            confirmation.setContentText("Cette action est irréversible.");

            var result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = ServiceActivity.deleteActivity(id);
                if (success) {
                    afficherAlerte(AlertType.INFORMATION, "Succès", "Activité supprimée avec succès.");
                    afficherActivites();
                    viderChamps();
                } else {
                    afficherAlerte(AlertType.ERROR, "Erreur", "Échec de la suppression.");
                }
            }
        } catch (NumberFormatException e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "ID invalide. Veuillez entrer un nombre entier.");
        }
    }

    private void modifierActivite() {
        if (!validerTousLesChamps()) {
            return;
        }

        if (!validerChampId()) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String titre = titreTXT.getText();
            String description = descriptionTXT.getText();
            LocalDate selectedDate = datePicker.getValue();
            String typesActivity = typeTXT.getText();
            String imageFileName = imgTXT.getText();
            boolean isApproved = apprBOX.isSelected();

            LocalDateTime date = selectedDate.atStartOfDay();

            boolean success = ServiceActivity.updateActivity(id, titre, description, date,
                    imageFileName, isApproved, typesActivity);

            if (success) {
                afficherAlerte(AlertType.INFORMATION, "Succès", "Activité mise à jour avec succès.");
                afficherActivites();
                viderChamps();
            } else {
                afficherAlerte(AlertType.ERROR, "Erreur", "Échec de la mise à jour.");
            }
        } catch (Exception e) {
            afficherAlerte(AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    // Méthodes de validation
    private boolean validerTousLesChamps() {
        if (!validerChampTitre())
            return false;
        if (!validerChampDescription())
            return false;
        if (!validerChampDate())
            return false;
        if (!validerChampType())
            return false;
        if (!validerChampImage())
            return false;
        return true;
    }

    private boolean validerChampTitre() {
        String titre = titreTXT.getText().trim();
        if (titre.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "Le titre ne peut pas être vide.");
            titreTXT.requestFocus();
            return false;
        }
        if (titre.length() < 3) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "Le titre doit contenir au moins 3 caractères.");
            titreTXT.requestFocus();
            return false;
        }
        if (titre.length() > 50) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "Le titre ne doit pas dépasser 50 caractères.");
            titreTXT.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampDescription() {
        String description = descriptionTXT.getText().trim();
        if (description.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "La description ne peut pas être vide.");
            descriptionTXT.requestFocus();
            return false;
        }
        if (description.length() < 10) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte",
                    "La description doit contenir au moins 10 caractères.");
            descriptionTXT.requestFocus();
            return false;
        }
        if (description.length() > 500) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte",
                    "La description ne doit pas dépasser 500 caractères.");
            descriptionTXT.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "Veuillez sélectionner une date.");
            datePicker.requestFocus();
            return false;
        }

        // Validation supplémentaire : vérifier que la date n'est pas dans le passé
        LocalDate today = LocalDate.now();
        if (selectedDate.isBefore(today)) {
            afficherAlerte(AlertType.WARNING, "Date invalide", "La date ne peut pas être dans le passé.");
            datePicker.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampType() {
        String type = typeTXT.getText().trim();
        if (type.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "Le type d'activité ne peut pas être vide.");
            typeTXT.requestFocus();
            return false;
        }
        if (type.length() < 3) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "Le type doit contenir au moins 3 caractères.");
            typeTXT.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampImage() {
        String image = imgTXT.getText().trim();
        if (image.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "Veuillez sélectionner une image.");
            chooseImgBtn.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampId() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "L'ID de l'activité ne peut pas être vide.");
            idField.requestFocus();
            return false;
        }
        try {
            int idNumber = Integer.parseInt(id);
            if (idNumber <= 0) {
                afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "L'ID doit être un nombre positif.");
                idField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "L'ID doit être un nombre entier.");
            idField.requestFocus();
            return false;
        }
        return true;
    }

    // Méthode utilitaire pour afficher des alertes
    private void afficherAlerte(AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherActivites() {
        activityListView.getItems().clear();
        var activities = ServiceActivity.getAllActivities();
        activityListView.getItems().addAll(activities);

        activityListView.setCellFactory(listView -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            private final VBox vbox = new VBox();
            private final HBox hbox = new HBox(10);
            private final Label titleLabel = new Label();
            private final Label dateLabel = new Label();

            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: gray;");
                vbox.getChildren().addAll(titleLabel, dateLabel);
                hbox.getChildren().addAll(imageView, vbox);
            }

            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    titleLabel.setText("Titre : " + activity.getTitre());
                    dateLabel.setText("Date : " + activity.getDate().toString());

                    File imageFile = new File("uploads-img/" + activity.getImageFileName());
                    if (imageFile.exists()) {
                        imageView.setImage(new Image(imageFile.toURI().toString()));
                    } else {
                        imageView.setImage(null);
                    }

                    setGraphic(hbox);
                }
            }
        });

        // Ajouter la gestion des clics sur la liste des activités
        activityListView.setOnMouseClicked(event -> {
            Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
            if (selectedActivity != null) {
                // Remplir les champs avec les données de l'activité sélectionnée
                idField.setText(String.valueOf(selectedActivity.getId()));
                titreTXT.setText(selectedActivity.getTitre());
                descriptionTXT.setText(selectedActivity.getDescription());
                datePicker.setValue(selectedActivity.getDate().toLocalDate());
                typeTXT.setText(selectedActivity.getTypesActivity());
                imgTXT.setText(selectedActivity.getImageFileName());
                apprBOX.setSelected(selectedActivity.isApproved());

                // Afficher l'image si disponible
                File imageFile = new File("uploads-img/" + selectedActivity.getImageFileName());
                if (imageFile.exists()) {
                    imageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    imageView.setImage(null);
                }

                // Double-clic pour naviguer vers la vue des commentaires
                if (event.getClickCount() == 2) {
                    openCommentViewForActivity(selectedActivity.getId());
                }
            }
        });
    }

    private void viderChamps() {
        titreTXT.clear();
        descriptionTXT.clear();
        datePicker.setValue(null);
        typeTXT.clear();
        imgTXT.clear();
        idField.clear();
        apprBOX.setSelected(false);
        imageView.setImage(null);
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Fichiers image", "*.jpg", "*.png", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(chooseImgBtn.getScene().getWindow());
        if (selectedFile != null) {
            // Créer un nom unique pour éviter les conflits
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            String imageName = "img_" + System.currentTimeMillis() + extension;

            // Copier l'image dans uploads-img
            File destDir = new File("uploads-img");
            if (!destDir.exists())
                destDir.mkdirs();

            File destFile = new File(destDir, imageName);
            try {
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imgTXT.setText(imageName);
                imageView.setImage(new Image(destFile.toURI().toString()));
                afficherAlerte(AlertType.INFORMATION, "Image importée",
                        "Image copiée avec succès vers : " + destFile.getAbsolutePath());
            } catch (IOException e) {
                afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la copie de l'image : " + e.getMessage());
            }
        }
    }

    private void afficherCommentaires() {
        try {
            // Si une activité est sélectionnée, ouvrir la vue des commentaires avec cet ID
            if (!idField.getText().isEmpty()) {
                int activityId = Integer.parseInt(idField.getText());
                openCommentViewForActivity(activityId);
            } else {
                // Sinon, ouvrir la vue des commentaires sans ID pré-rempli
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/commentaire_view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Commentaires");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException e) {
            afficherAlerte(AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement de la vue des commentaires : " + e.getMessage());
        } catch (NumberFormatException e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "ID d'activité invalide.");
        }
    }

    private void openCommentViewForActivity(int activityId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commentaire_view.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et définir l'ID de l'activité
            CommentaireViewController controller = loader.getController();
            controller.setActivityId(activityId);

            Stage stage = new Stage();
            stage.setTitle("Commentaires pour l'activité #" + activityId);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte(AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement de la vue des commentaires : " + e.getMessage());
        }
    }

    private void ouvrirAide() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/help.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Aide");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'aide : " + e.getMessage());
        }
    }
}