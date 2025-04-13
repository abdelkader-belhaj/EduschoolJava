package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterCour {

    @FXML
    private TextField titreField;

    @FXML
    private TextField enseignantField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> heureField;

    @FXML
    private TextField themeComboBox;

    @FXML
    private Button saveButton;

    @FXML
    void initialize() {
        // Remplir quelques heures (tu peux changer selon tes besoins)
        heureField.getItems().addAll("09:00", "10:30", "14:00", "16:00");
    }

    @FXML
    void saveCours(ActionEvent event) {
        try {
            String titre = titreField.getText();
            String enseignant = enseignantField.getText();
            LocalDate date = datePicker.getValue();
            String heureStr = heureField.getValue();
            String themeText = themeComboBox.getText();

            if (titre.isEmpty() || enseignant.isEmpty() || date == null || heureStr == null || themeText.isEmpty()) {
                System.out.println("Tous les champs doivent être remplis.");
                return;
            }

            LocalTime heure = LocalTime.parse(heureStr);
            LocalDateTime dateHeure = LocalDateTime.of(date, heure);

            Theme theme = new Theme(themeText); // Si Theme a un constructeur avec String
            Cours cours = new Cours(titre, dateHeure, enseignant, theme);

            CoursService serviceCours = new CoursService();
            serviceCours.ajouter(cours);

            System.out.println("Cours ajouté avec succès !");

            // Navigation vers la liste ou affichage (optionnel)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/eduskool/views/AfficherCours.fxml"));
            Parent root = loader.load();
            titreField.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
