package tn.eduskool.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.SoumissionDevoirService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class SoumissionFormController {

    @FXML private Label lblDevoir;
    @FXML private TextField txtFichier;
    @FXML private Button btnParcourir;
    @FXML private Button btnValider;

    private Devoir devoir;
    private SoumissionDevoirsController soumissionDevoirsController;
    private final SoumissionDevoirService soumissionService = new SoumissionDevoirService();

    public void setDevoir(Devoir devoir) {
        this.devoir = devoir;
        if (devoir != null) {
            lblDevoir.setText("Devoir: " + devoir.getTitre());
        }
    }

    public void setSoumissionDevoirsController(SoumissionDevoirsController controller) {
        this.soumissionDevoirsController = controller;
    }

    @FXML
    private void parcourirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner le fichier à soumettre");
        File file = fileChooser.showOpenDialog(btnParcourir.getScene().getWindow());
        if (file != null) {
            txtFichier.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void validerSoumission() {
        if (devoir == null || txtFichier.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un fichier");
            return;
        }

        try {
            // Copier le fichier vers le dossier de stockage
            File sourceFile = new File(txtFichier.getText());
            String fileName = "soumission_" + System.currentTimeMillis() + "_" + sourceFile.getName();
            File destFile = new File("uploads/soumissions/" + fileName);

            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Créer la soumission
            SoumissionDevoir soumission = new SoumissionDevoir();
            soumission.setDateSoumission(LocalDateTime.now());
            soumission.setFichier(fileName);
            soumission.setDevoir(devoir);

            if (soumissionService.ajouter(soumission)) {
                showAlert("Succès", "Soumission enregistrée avec succès");

                // Rafraîchir la liste des soumissions
                if (soumissionDevoirsController != null) {
                    soumissionDevoirsController.rafraichirListeSoumissions();
                }

                // Fermer la fenêtre
                ((Stage) btnValider.getScene().getWindow()).close();
            } else {
                showAlert("Erreur", "Échec de l'enregistrement de la soumission");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la copie du fichier");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}