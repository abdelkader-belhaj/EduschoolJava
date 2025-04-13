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

public class ModifierCour {

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

    private Cours coursToEdit;

    @FXML
    void initialize() {
        heureField.getItems().addAll("09:00", "10:30", "14:00", "16:00");
    }

    public void setCours(Cours cours) {
        this.coursToEdit = cours;
        titreField.setText(cours.getTitre());
        enseignantField.setText(cours.getEnseignant());
        datePicker.setValue(cours.getDateHeure().toLocalDate());
        heureField.setValue(cours.getDateHeure().toLocalTime().toString());
        themeComboBox.setText(cours.getTheme().getTitre());
    }

    @FXML
    void modifierCours(ActionEvent event) {
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

            // Mise à jour des données dans l'objet existant
            coursToEdit.setTitre(titre);
            coursToEdit.setDateHeure(dateHeure);
            coursToEdit.setEnseignant(enseignant);
            coursToEdit.setTheme(new Theme(themeText)); // Assurez-vous que Theme a un setter ou constructeur adapté

            CoursService serviceCours = new CoursService();
            serviceCours.modifier(coursToEdit);

            System.out.println("Cours modifié avec succès !");

            // Navigation après modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/eduskool/views/AfficherCours.fxml"));
            Parent root = loader.load();
            titreField.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
