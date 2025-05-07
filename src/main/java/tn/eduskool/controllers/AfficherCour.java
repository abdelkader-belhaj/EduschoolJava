package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import tn.eduskool.entities.Cours;
import tn.eduskool.services.CoursService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Optional;

public class AfficherCour {

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField enseignantField;

    @FXML
    private ComboBox<String> heureField;

    @FXML
    private TextField themeComboBox;

    @FXML
    private TextField titreField;

    @FXML
    private Button deleteButton;

    private Cours cours; // Stocke le cours affiché

    public void setCours(Cours cours) {
        this.cours = cours;
        this.titreField.setText(cours.getTitre());
        this.enseignantField.setText(cours.getEnseignant());
        this.dateField.setValue(cours.getDateHeure().toLocalDate());
        this.heureField.setValue(cours.getDateHeure().toLocalTime().toString());
        this.themeComboBox.setText(cours.getTheme().getTitre());
    }

    public void setTitre(String titre) {
        this.titreField.setText(titre);
    }

    public void setEnseignant(String enseignant) {
        this.enseignantField.setText(enseignant);
    }

    public void setDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        this.dateField.setValue(localDate);
    }

    public void setHeure(String heure) {
        this.heureField.setValue(heure);
    }

    public void setTheme(String theme) {
        this.themeComboBox.setText(theme);
    }

    @FXML
    void deleteCours(ActionEvent event) {
        if (cours != null) {
            // Fenêtre de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce cours ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    CoursService service = new CoursService();
                    service.supprimer(cours.getId());
                    System.out.println("Cours supprimé avec succès.");

                    // Retourner à la liste des cours
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/eduskool/views/AfficherCours.fxml"));
                    Parent root = loader.load();
                    titreField.getScene().setRoot(root);

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Aucun cours sélectionné.");
        }
    }
}
