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
    public void initialize() {
        addBUTT.setOnAction(event -> ajouterActivite());
        deleteBtn.setOnAction(event -> supprimerActivite());
        updateBtn.setOnAction(event -> modifierActivite());
        refreshBtn.setOnAction(event -> afficherActivites());
        showCommentsBtn.setOnAction(event -> afficherCommentaires());
        chooseImgBtn.setOnAction(event -> choisirImage());

        afficherActivites(); // Charger dès le démarrage
    }

    private void ajouterActivite() {
        String titre = titreTXT.getText();
        String description = descriptionTXT.getText();
        LocalDate selectedDate = datePicker.getValue();
        String typesActivity = typeTXT.getText();
        String imageFileName = imgTXT.getText();
        boolean isApproved = apprBOX.isSelected();

        if (selectedDate == null) {
            System.out.println("Veuillez sélectionner une date.");
            return;
        }

        try {
            LocalDateTime date = selectedDate.atStartOfDay();
            LocalDateTime createdAt = LocalDateTime.now();

            boolean success = ServiceActivity.createActivity(titre, description, date, imageFileName,
                    isApproved, typesActivity, createdAt);

            if (success) {
                System.out.println("L'activité a été ajoutée avec succès.");
                afficherActivites();
                viderChamps();
            } else {
                System.out.println("Erreur lors de l'ajout de l'activité.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide.");
        }
    }

    private void supprimerActivite() {
        try {
            int id = Integer.parseInt(idField.getText());
            boolean success = ServiceActivity.deleteActivity(id);
            if (success) {
                System.out.println("Activité supprimée avec succès.");
                afficherActivites();
                viderChamps();
            } else {
                System.out.println("Échec de la suppression.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
        }
    }

    private void modifierActivite() {
        try {
            int id = Integer.parseInt(idField.getText());
            String titre = titreTXT.getText();
            String description = descriptionTXT.getText();
            LocalDate selectedDate = datePicker.getValue();
            String typesActivity = typeTXT.getText();
            String imageFileName = imgTXT.getText();
            boolean isApproved = apprBOX.isSelected();

            if (selectedDate == null) {
                System.out.println("Veuillez sélectionner une date.");
                return;
            }

            LocalDateTime date = selectedDate.atStartOfDay();

            boolean success = ServiceActivity.updateActivity(id, titre, description, date,
                    imageFileName, isApproved, typesActivity);

            if (success) {
                System.out.println("Activité mise à jour avec succès.");
                afficherActivites();
                viderChamps();
            } else {
                System.out.println("Échec de la mise à jour.");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
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
                        imageView.setImage(null); // ou une image par défaut
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
                System.out.println("Image copiée vers : " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Erreur lors de la copie de l'image : " + e.getMessage());
            }

            // Mettre à jour le champ et afficher l'image
            imgTXT.setText(imageName);
            imageView.setImage(new Image(destFile.toURI().toString()));
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
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("ID d'activité invalide.");
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
            e.printStackTrace();
        }
    }
}