package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
        try {
            int activityId = Integer.parseInt(tfActivityId.getText());
            String contenu = tfContenu.getText();
            // Appel au service pour créer le commentaire et récupérer son ID généré
            int commentId = ServiceCommentaire.createCommentaireReturnId(activityId, contenu, selectedNote);
            if (commentId != -1) {
                // Génération de l'audio du commentaire
                TextToSpeechUtil.generateAudio(contenu, commentId);
                taCommentaires
                        .setText("Commentaire créé avec succès !\nAudio généré pour le commentaire #" + commentId);
                // Rafraîchir la liste des commentaires après création
                handleLoad(event);
                // Réinitialiser le champ contenu et la note
                tfContenu.clear();
                selectedNote = 0;
                updateStars();
            } else {
                taCommentaires.setText("Erreur lors de la création du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir des valeurs valides.");
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        try {
            int id = Integer.parseInt(tfId.getText());
            int activityId = Integer.parseInt(tfActivityId.getText());
            String contenu = tfContenu.getText();
            boolean success = ServiceCommentaire.updateCommentaire(id, activityId, contenu, selectedNote);
            if (success) {
                taCommentaires.setText("Commentaire mis à jour avec succès !");
                // Rafraîchir la liste des commentaires après mise à jour
                handleLoad(event);
                // Réinitialiser les champs
                tfId.clear();
                tfContenu.clear();
                selectedNote = 0;
                updateStars();
            } else {
                taCommentaires.setText("Erreur lors de la mise à jour du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir des valeurs valides.");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        try {
            int id = Integer.parseInt(tfId.getText());
            boolean success = ServiceCommentaire.deleteCommentaire(id);
            if (success) {
                taCommentaires.setText("Commentaire supprimé avec succès !");
                // Rafraîchir la liste des commentaires après suppression
                handleLoad(event);
                // Réinitialiser le champ ID
                tfId.clear();
            } else {
                taCommentaires.setText("Erreur lors de la suppression du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir un ID valide.");
        }
    }

    @FXML
    void handleLoad(ActionEvent event) {
        try {
            taCommentaires.clear();
            int activityId = Integer.parseInt(tfActivityId.getText());
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
            taCommentaires.setText("Veuillez saisir un ID d'activité valide.");
        }
    }

    @FXML
    void handleListen(ActionEvent event) {
        try {
            int commentId = Integer.parseInt(tfId.getText());
            String audioFilePath = "uploads/comment_" + commentId + ".ogg";
            File audioFile = new File(audioFilePath);
            if (!audioFile.exists()) {
                taCommentaires.setText("Fichier audio non trouvé pour le commentaire #" + commentId);
                return;
            }
            Media media = new Media(audioFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> taCommentaires.setText("Lecture terminée du commentaire #" + commentId));
            mediaPlayer.setOnError(() -> taCommentaires.setText("Erreur de lecture du fichier audio."));
            mediaPlayer.play();
            taCommentaires.setText("Lecture du commentaire audio #" + commentId);
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir un ID valide.");
        } catch (Exception ex) {
            ex.printStackTrace();
            taCommentaires.setText("Erreur lors de la lecture du fichier audio.");
        }
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