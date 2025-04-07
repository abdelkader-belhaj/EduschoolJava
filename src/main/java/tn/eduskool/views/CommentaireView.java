package tn.eduskool.views;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.eduskool.entities.Commentaire;
import tn.eduskool.services.ServiceCommentaire;
import tn.eduskool.servicesAPI.TextToSpeechUtil;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommentaireView extends Application {

    private TextField tfActivityId, tfContenu, tfId;
    private TextArea taCommentaires;
    private Button btnCreate, btnUpdate, btnDelete, btnLoad, btnListen;
    private HBox starBox;
    private List<Label> stars;
    // La note sélectionnée (entre 1 et 5)
    private int selectedNote = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Champs de texte
        tfActivityId = new TextField();
        tfContenu = new TextField();
        tfId = new TextField();
        taCommentaires = new TextArea();

        // Création du sélecteur d'étoiles
        starBox = new HBox(5);
        starBox.setAlignment(Pos.CENTER);
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

        // Boutons pour les opérations sur les commentaires
        btnCreate = new Button("Créer un commentaire");
        btnCreate.setOnAction(e -> createCommentaire());

        btnUpdate = new Button("Mettre à jour un commentaire");
        btnUpdate.setOnAction(e -> updateCommentaire());

        btnDelete = new Button("Supprimer un commentaire");
        btnDelete.setOnAction(e -> deleteCommentaire());

        btnLoad = new Button("Charger les commentaires");
        btnLoad.setOnAction(e -> loadCommentaires());

        // Nouveau bouton pour écouter le commentaire audio
        btnListen = new Button("Écouter commentaire");
        btnListen.setOnAction(e -> listenCommentaire());

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Activity ID"), tfActivityId,
                new Label("Contenu"), tfContenu,
                new Label("Note"), starBox,
                new Label("ID (pour update, delete ou écouter)"), tfId,
                btnCreate, btnUpdate, btnDelete, btnLoad, btnListen,
                new Label("Commentaires :"), taCommentaires);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(layout, 400, 550);
        primaryStage.setTitle("Gestion des Commentaires");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Met à jour l'affichage des étoiles selon la note sélectionnée
    private void updateStars() {
        for (int i = 0; i < stars.size(); i++) {
            if (i < selectedNote) {
                stars.get(i).setText("★"); // étoile pleine
                stars.get(i).setStyle("-fx-font-size: 24; -fx-text-fill: #FFD700; -fx-cursor: hand;");
            } else {
                stars.get(i).setText("☆"); // étoile vide
                stars.get(i).setStyle("-fx-font-size: 24; -fx-text-fill: #ccc; -fx-cursor: hand;");
            }
        }
    }

    // Méthode pour créer un commentaire et générer l'audio
    private void createCommentaire() {
        try {
            int activityId = Integer.parseInt(tfActivityId.getText());
            String contenu = tfContenu.getText();

            // Crée le commentaire et récupère son ID généré
            int commentId = ServiceCommentaire.createCommentaireReturnId(activityId, contenu, selectedNote);
            if (commentId != -1) {
                // Génère l'audio via l'API Voice RSS
                TextToSpeechUtil.generateAudio(contenu, commentId);
                taCommentaires
                        .setText("Commentaire créé avec succès !\nAudio généré pour le commentaire #" + commentId);
            } else {
                taCommentaires.setText("Erreur lors de la création du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir des valeurs valides.");
        }
    }

    // Méthode pour mettre à jour un commentaire
    private void updateCommentaire() {
        try {
            int id = Integer.parseInt(tfId.getText());
            int activityId = Integer.parseInt(tfActivityId.getText());
            String contenu = tfContenu.getText();
            boolean success = ServiceCommentaire.updateCommentaire(id, activityId, contenu, selectedNote);
            if (success) {
                taCommentaires.setText("Commentaire mis à jour avec succès !");
            } else {
                taCommentaires.setText("Erreur lors de la mise à jour du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir des valeurs valides.");
        }
    }

    // Méthode pour supprimer un commentaire
    private void deleteCommentaire() {
        try {
            int id = Integer.parseInt(tfId.getText());
            boolean success = ServiceCommentaire.deleteCommentaire(id);
            if (success) {
                taCommentaires.setText("Commentaire supprimé avec succès !");
            } else {
                taCommentaires.setText("Erreur lors de la suppression du commentaire.");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir un ID valide.");
        }
    }

    // Méthode pour charger les commentaires
    private void loadCommentaires() {
        try {
            taCommentaires.clear();
            int activityId = Integer.parseInt(tfActivityId.getText());
            List<Commentaire> commentaires = ServiceCommentaire.getCommentairesByActivityId(activityId);
            for (Commentaire c : commentaires) {
                taCommentaires.appendText(c.toString() + "\n");
            }
        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir un ID d'activité valide.");
        }
    }

    // Méthode pour écouter le commentaire via le fichier audio généré
    private void listenCommentaire() {
        try {
            int commentId = Integer.parseInt(tfId.getText());
            String audioFilePath = "uploads/comment_" + commentId + ".ogg";
            File audioFile = new File(audioFilePath);

            // Vérification de l'existence du fichier
            if (!audioFile.exists()) {
                taCommentaires.setText("Fichier audio non trouvé pour le commentaire #" + commentId);
                return;
            }

            System.out.println("Chemin du fichier audio: " + audioFilePath); // Debug : Affiche le chemin exact du
                                                                             // fichier

            // Création du media et du MediaPlayer
            Media media = new Media(audioFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            // Détection de la fin de la lecture
            mediaPlayer.setOnEndOfMedia(() -> taCommentaires.setText("Lecture terminée du commentaire #" + commentId));

            // Gestion des erreurs
            mediaPlayer.setOnError(() -> {
                System.out.println("Erreur de lecture du fichier : " + mediaPlayer.getError().getMessage()); // Affiche
                                                                                                             // l'erreur
                taCommentaires.setText("Erreur de lecture du fichier audio.");
            });

            mediaPlayer.play();
            taCommentaires.setText("Lecture du commentaire audio #" + commentId);

        } catch (NumberFormatException ex) {
            taCommentaires.setText("Veuillez saisir un ID valide.");
        } catch (Exception ex) {
            ex.printStackTrace();
            taCommentaires.setText("Erreur lors de la lecture du fichier audio.");
        }
    }

}
