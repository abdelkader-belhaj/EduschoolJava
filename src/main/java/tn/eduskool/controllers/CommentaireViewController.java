package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tn.eduskool.entities.Commentaire;
import tn.eduskool.services.ServiceCommentaire;
import tn.eduskool.serviceExterne.TextToSpeechUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CommentaireViewController implements Initializable {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnListen;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnUpdate;

    @FXML
    private HBox starBox;

    @FXML
    private TextArea taCommentaires;

    @FXML
    private TextField tfActivityId;

    @FXML
    private TextField tfContenu;

    @FXML
    private TextField tfId;

    // Pour la gestion des étoiles (note)
    private List<Label> stars;
    private int selectedNote = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation du sélecteur d'étoiles (entre 1 et 5)
        stars = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆"); // étoile vide
            star.setStyle("-fx-font-size: 24; -fx-cursor: hand;");
            final int starValue = i;
            star.setOnMouseClicked(e -> {
                selectedNote = starValue;
                updateStars();
            });
            stars.add(star);
            starBox.getChildren().add(star);
        }
    }

    // Mise à jour de l'affichage des étoiles selon la note sélectionnée
    private void updateStars() {
        for (int i = 0; i < stars.size(); i++) {
            if (i < selectedNote) {
                stars.get(i).setText("★");
                stars.get(i).setStyle("-fx-font-size: 24; -fx-text-fill: #FFD700; -fx-cursor: hand;");
            } else {
                stars.get(i).setText("☆");
                stars.get(i).setStyle("-fx-font-size: 24; -fx-text-fill: #ccc; -fx-cursor: hand;");
            }
        }
    }

    @FXML
    void handleCreate(ActionEvent event) {
        if (!validerChampActivityId() || !validerChampContenu() || !validerNote()) {
            return;
        }

        try {
            int activityId = Integer.parseInt(tfActivityId.getText().trim());
            String contenu = tfContenu.getText().trim();
            // Appel au service pour créer le commentaire et récupérer son ID généré
            int commentId = ServiceCommentaire.createCommentaireReturnId(activityId, contenu, selectedNote);
            if (commentId != -1) {
                // Génération de l'audio du commentaire
                TextToSpeechUtil.generateAudio(contenu, commentId);
                afficherAlerte(AlertType.INFORMATION, "Succès",
                        "Commentaire créé avec succès ! Audio généré pour le commentaire #" + commentId);
                // Rafraîchir la liste des commentaires après création
                handleLoad(event);
                // Réinitialiser le champ contenu et la note
                tfContenu.clear();
                selectedNote = 0;
                updateStars();
            } else {
                afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la création du commentaire.");
            }
        } catch (NumberFormatException ex) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs valides.");
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        if (!validerChampId() || !validerChampActivityId() || !validerChampContenu() || !validerNote()) {
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText().trim());
            int activityId = Integer.parseInt(tfActivityId.getText().trim());
            String contenu = tfContenu.getText().trim();
            boolean success = ServiceCommentaire.updateCommentaire(id, activityId, contenu, selectedNote);
            if (success) {
                afficherAlerte(AlertType.INFORMATION, "Succès", "Commentaire mis à jour avec succès !");
                // Rafraîchir la liste des commentaires après mise à jour
                handleLoad(event);
                // Réinitialiser les champs
                tfId.clear();
                tfContenu.clear();
                selectedNote = 0;
                updateStars();
            } else {
                afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du commentaire.");
            }
        } catch (NumberFormatException ex) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs valides.");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        if (!validerChampId()) {
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText().trim());

            // Confirmation de suppression
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce commentaire ?");
            confirmation.setContentText("Cette action est irréversible.");

            var result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = ServiceCommentaire.deleteCommentaire(id);
                if (success) {
                    afficherAlerte(AlertType.INFORMATION, "Succès", "Commentaire supprimé avec succès !");
                    // Rafraîchir la liste des commentaires après suppression
                    handleLoad(event);
                    // Réinitialiser le champ ID
                    tfId.clear();
                } else {
                    afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la suppression du commentaire.");
                }
            }
        } catch (NumberFormatException ex) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Veuillez saisir un ID valide.");
        }
    }

    @FXML
    void handleLoad(ActionEvent event) {
        if (!validerChampActivityId()) {
            return;
        }

        try {
            taCommentaires.clear();
            int activityId = Integer.parseInt(tfActivityId.getText().trim());
            List<Commentaire> commentaires = ServiceCommentaire.getCommentairesByActivityId(activityId);
            if (commentaires.isEmpty()) {
                taCommentaires.setText("Aucun commentaire trouvé pour cette activité.");
            } else {
                taCommentaires.setText("Liste des commentaires pour l'activité #" + activityId + ":\n\n");
                for (Commentaire c : commentaires) {
                    taCommentaires.appendText(c.toString() + "\n");
                }
            }
        } catch (NumberFormatException ex) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Veuillez saisir un ID d'activité valide.");
        }
    }

    @FXML
    void handleListen(ActionEvent event) {
        if (!validerChampId()) {
            return;
        }

        try {
            int commentId = Integer.parseInt(tfId.getText().trim());
            String audioFilePath = "uploads/comment_" + commentId + ".ogg";
            File audioFile = new File(audioFilePath);
            if (!audioFile.exists()) {
                afficherAlerte(AlertType.WARNING, "Fichier non trouvé",
                        "Fichier audio non trouvé pour le commentaire #" + commentId);
                return;
            }
            Media media = new Media(audioFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> taCommentaires.setText("Lecture terminée du commentaire #" + commentId));
            mediaPlayer
                    .setOnError(() -> afficherAlerte(AlertType.ERROR, "Erreur", "Erreur de lecture du fichier audio."));
            mediaPlayer.play();
            taCommentaires.setText("Lecture du commentaire audio #" + commentId);
        } catch (NumberFormatException ex) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Veuillez saisir un ID valide.");
        } catch (Exception ex) {
            ex.printStackTrace();
            afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la lecture du fichier audio.");
        }
    }

    // Méthodes de validation
    private boolean validerChampId() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "L'ID du commentaire ne peut pas être vide.");
            tfId.requestFocus();
            return false;
        }
        try {
            int idNumber = Integer.parseInt(id);
            if (idNumber <= 0) {
                afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "L'ID doit être un nombre positif.");
                tfId.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "L'ID doit être un nombre entier.");
            tfId.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampActivityId() {
        String activityId = tfActivityId.getText().trim();
        if (activityId.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "L'ID de l'activité ne peut pas être vide.");
            tfActivityId.requestFocus();
            return false;
        }
        try {
            int idNumber = Integer.parseInt(activityId);
            if (idNumber <= 0) {
                afficherAlerte(AlertType.WARNING, "Saisie incorrecte",
                        "L'ID de l'activité doit être un nombre positif.");
                tfActivityId.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte", "L'ID de l'activité doit être un nombre entier.");
            tfActivityId.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerChampContenu() {
        String contenu = tfContenu.getText().trim();
        if (contenu.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Champ manquant", "Le contenu du commentaire ne peut pas être vide.");
            tfContenu.requestFocus();
            return false;
        }
        if (contenu.length() < 3) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte",
                    "Le commentaire doit contenir au moins 3 caractères.");
            tfContenu.requestFocus();
            return false;
        }
        if (contenu.length() > 200) {
            afficherAlerte(AlertType.WARNING, "Saisie incorrecte",
                    "Le commentaire ne doit pas dépasser 200 caractères.");
            tfContenu.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validerNote() {
        if (selectedNote <= 0) {
            afficherAlerte(AlertType.WARNING, "Note manquante", "Veuillez attribuer une note (entre 1 et 5 étoiles).");
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

    // Méthode pour définir l'ID de l'activité (utilisée lors de la navigation
    // depuis la liste des activités)
    public void setActivityId(int activityId) {
        tfActivityId.setText(String.valueOf(activityId));
        // Charger automatiquement les commentaires pour cette activité
        handleLoad(new ActionEvent());
    }

    // Méthode pour pré-remplir les champs de modification d'un commentaire existant
    public void loadCommentForEdit(Commentaire commentaire) {
        if (commentaire != null) {
            tfId.setText(String.valueOf(commentaire.getId()));
            tfActivityId.setText(String.valueOf(commentaire.getActivityId()));
            tfContenu.setText(commentaire.getContenu());
            selectedNote = commentaire.getNote();
            updateStars();
        }
    }
}