package tn.eduskool.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.services.CoursService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendrierCoursController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label selectedDateLabel;

    @FXML
    private ListView<HBox> coursListView;

    private CoursService coursService;

    @FXML
    public void initialize() {
        coursService = new CoursService();
        // Sélectionner la date actuelle par défaut
        datePicker.setValue(LocalDate.now());
        updateCoursList();
    }

    @FXML
    private void updateCoursList() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert("Erreur", "Sélection de date", "Veuillez sélectionner une date.");
            return;
        }

        // Mettre à jour l'étiquette avec la date sélectionnée
        selectedDateLabel.setText("Cours pour le " + selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        // Vider la liste
        coursListView.getItems().clear();

        try {
            // Récupérer les cours pour la date sélectionnée
            List<Cours> coursList = coursService.recuperer().stream()
                    .filter(cours -> cours.getDateHeure().toLocalDate().equals(selectedDate))
                    .toList();

            // Ajouter chaque cours à la ListView avec des boutons d'action
            for (Cours cours : coursList) {
                HBox hbox = new HBox(10);
                hbox.setStyle("-fx-padding: 5; -fx-background-color: #FFF3E0; -fx-border-color: #FF5722;");

                Label coursLabel = new Label(cours.getTitre() + " - " + cours.getEnseignant() + " (" + cours.getTheme().getTitre() + ")");
                coursLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #D81B60;");

                Button editButton = new Button("Modifier");
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12;");
                editButton.setOnAction(e -> handleEditCours(cours));

                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 12;");
                deleteButton.setOnAction(e -> handleDeleteCours(cours));

                hbox.getChildren().addAll(coursLabel, editButton, deleteButton);
                coursListView.getItems().add(hbox);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des cours", "Erreur lors du chargement des cours: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCours.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau cours");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            updateCoursList();
        } catch (IOException e) {
            showAlert("Erreur", "Ajout de cours", "Erreur lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
        }
    }

    private void handleEditCours(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCours.fxml"));
            Parent root = loader.load();

            EditCoursController controller = loader.getController();
            controller.setCoursToEdit(cours);

            Stage stage = new Stage();
            stage.setTitle("Modifier le cours");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            updateCoursList();
        } catch (IOException e) {
            showAlert("Erreur", "Modification de cours", "Erreur lors de l'ouverture du formulaire d'édition: " + e.getMessage());
        }
    }

    private void handleDeleteCours(Cours cours) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer le cours");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer le cours: " + cours.getTitre() + "?");

            if (alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
                coursService.supprimer(cours.getId());
                updateCoursList();
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Suppression de cours", "Erreur lors de la suppression: " + e.getMessage());
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger FrontEnseignant.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnseignantController.fxml"));
            Parent frontEnseignantView = loader.load();
            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Définir la nouvelle scène
            Scene scene = new Scene(frontEnseignantView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de FrontEnseignant.fxml");
        }
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
