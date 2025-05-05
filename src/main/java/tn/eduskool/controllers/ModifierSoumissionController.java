package tn.eduskool.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.services.SoumissionDevoirService;

import java.io.File;

import java.time.LocalDateTime;

public class ModifierSoumissionController {

    @FXML
    private DatePicker dateSoumissionPicker;
    @FXML
    private TextField fichierField;
    @FXML
    private TextField noteField;

    private SoumissionDevoir soumission;
    private ListeSoumissionsController listeSoumissionsController;
    private final SoumissionDevoirService service = new SoumissionDevoirService();

    // Méthode pour définir la soumission à modifier
    public void setSoumission(SoumissionDevoir s) {
        this.soumission = s;
        if (s != null) {
            dateSoumissionPicker.setValue(s.getDateSoumission().toLocalDate());
            fichierField.setText(s.getFichier());
            noteField.setText(s.getNote() != null ? String.valueOf(s.getNote()) : "");
        }
    }

    // Méthode pour définir le contrôleur parent
    public void setListeSoumissionsController(ListeSoumissionsController controller) {
        this.listeSoumissionsController = controller;
    }

    @FXML
    public void choisirFichier() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sélectionner un fichier");
        File file = fc.showOpenDialog(null);
        if (file != null) {
            fichierField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void enregistrer() {
        try {
            // Validation des données
            if (fichierField.getText().isEmpty()) {
                afficherErreur("Erreur", "Veuillez sélectionner un fichier");
                return;
            }

            // Gestion de la note
            Integer note = null;
            if (!noteField.getText().isEmpty()) {
                try {
                    note = Integer.parseInt(noteField.getText());
                    if (note < 0 || note > 20) {
                        afficherErreur("Erreur", "La note doit être entre 0 et 20");
                        return;
                    }
                } catch (NumberFormatException e) {
                    afficherErreur("Erreur", "La note doit être un nombre valide");
                    return;
                }
            }

            // Mise à jour de l'objet soumission
            soumission.setDateSoumission(LocalDateTime.of(
                    dateSoumissionPicker.getValue(),
                    LocalDateTime.now().toLocalTime()
            ));
            soumission.setFichier(fichierField.getText());
            soumission.setNote(note);

            // Appel du service pour mettre à jour en base de données
            if (service.modifier(soumission)) {
                // Rafraîchir la table dans le contrôleur parent si disponible
                if (listeSoumissionsController != null) {
                    listeSoumissionsController.rafraichirTable();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                afficherErreur("Erreur", "Échec de la mise à jour de la soumission");
            }
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur", "Une erreur inattendue est survenue");
        }
    }

    @FXML
    public void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) dateSoumissionPicker.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}