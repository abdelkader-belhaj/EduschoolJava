package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.DevoirService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModifierDevoirController {

    private Devoir devoirToModify;
    private final DevoirService devoirService = new DevoirService();

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> hourCombo;
    @FXML
    private ComboBox<Integer> minuteCombo;
    @FXML
    private TextField fichierField;
    @FXML
    private Button parcourirBtn;
    @FXML
    private Button modifierButton;

    // ✅ Méthode appelée depuis ListeDevoirsController
    public void initData(Devoir devoir) {
        this.devoirToModify = devoir;

        titreField.setText(devoir.getTitre());
        descriptionField.setText(devoir.getDescription());

        initializeDateTimeControls();

        LocalDateTime dateLimite = devoir.getDatelimite();
        datePicker.setValue(dateLimite.toLocalDate());
        hourCombo.getSelectionModel().select((Integer) dateLimite.getHour());
        minuteCombo.getSelectionModel().select((Integer) dateLimite.getMinute());

        fichierField.setText(devoir.getFichier());
    }

    private void initializeDateTimeControls() {
        for (int i = 0; i < 24; i++)
            hourCombo.getItems().add(i);
        for (int i = 0; i < 60; i++)
            minuteCombo.getItems().add(i);

        hourCombo.setConverter(new StringConverter<>() {
            public String toString(Integer object) {
                return String.format("%02d", object);
            }

            public Integer fromString(String string) {
                return string.isEmpty() ? null : Integer.parseInt(string);
            }
        });

        minuteCombo.setConverter(new StringConverter<>() {
            public String toString(Integer object) {
                return String.format("%02d", object);
            }

            public Integer fromString(String string) {
                return string.isEmpty() ? null : Integer.parseInt(string);
            }
        });

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    @FXML
    private void handleParcourir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showOpenDialog(parcourirBtn.getScene().getWindow());
        if (file != null) {
            fichierField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleModifier() {
        try {
            String titre = titreField.getText().trim(); // Supprimer les espaces inutiles en début et fin
            String description = descriptionField.getText().trim();

            // Vérifier si les champs sont vides
            if (titre.isEmpty() || description.isEmpty()) {
                showAlert("Erreur", "Tous les champs sont obligatoires");
                return;
            }

            // Vérifier que le titre ne contient que des lettres et des espaces
            if (!titre.matches("[a-zA-Z\\s]+")) {
                showAlert("Erreur", "Le titre doit être composé uniquement de lettres et d'espaces");
                return;
            }

            // Vérifier que la date limite n'est pas dans le passé
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date limite ne peut pas être antérieure à la date actuelle");
                return;
            }

            // Créer la nouvelle date limite
            LocalDateTime nouvelleDateLimite = LocalDateTime.of(
                    selectedDate,
                    LocalTime.of(hourCombo.getValue(), minuteCombo.getValue()));

            // Mettre à jour l'objet Devoir
            devoirToModify.setTitre(titre);
            devoirToModify.setDescription(description);
            devoirToModify.setDatelimite(nouvelleDateLimite);
            devoirToModify.setFichier(fichierField.getText());

            // Modifier le devoir sans vérifier de retour
            devoirService.modifier(devoirToModify);
            showAlert("Succès", "Devoir modifié avec succès");
            closeWindow();

        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }
}
