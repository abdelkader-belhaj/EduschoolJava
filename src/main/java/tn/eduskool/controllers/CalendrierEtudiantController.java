package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.services.CoursService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendrierEtudiantController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label selectedDateLabel;

    @FXML
    private ListView<Label> coursListView;

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

            // Ajouter chaque cours à la ListView comme une étiquette
            for (Cours cours : coursList) {
                Label coursLabel = new Label(cours.getTitre() + " - " + cours.getEnseignant() + " (" + cours.getTheme().getTitre() + ")");
                coursLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #D81B60; -fx-font-family: 'Comic Sans MS'; -fx-padding: 5; -fx-background-color: #FFF3E0;");
                coursListView.getItems().add(coursLabel);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des cours", "Erreur lors du chargement des cours: " + e.getMessage());
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger FrontEnseignant.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent frontEnseignantView = loader.load();
            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Définir la nouvelle scène
            Scene scene = new Scene(frontEnseignantView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de MainView.fxml");
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
