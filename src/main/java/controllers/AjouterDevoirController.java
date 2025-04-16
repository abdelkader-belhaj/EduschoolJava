package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.DevoirService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterDevoirController {

    private final DevoirService ds1 = new DevoirService();

    @FXML private TextField titreID;
    @FXML private TextArea descriptionID;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> hourCombo;
    @FXML private ComboBox<Integer> minuteCombo;
    @FXML private TextField fichierField;
    @FXML private Button devoirbuttonID;

    @FXML
    public void initialize() {
        // Initialiser les heures (0-23)
        for (int i = 0; i < 24; i++) {
            hourCombo.getItems().add(i);
        }

        // Initialiser les minutes (0-59)
        for (int i = 0; i < 60; i++) {
            minuteCombo.getItems().add(i);
        }

        // Valeurs par défaut
        hourCombo.getSelectionModel().select(12); // Midi par défaut
        minuteCombo.getSelectionModel().select(0);

        // Formatter pour afficher deux chiffres
        hourCombo.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.format("%02d", object);
            }
            @Override
            public Integer fromString(String string) {
                return string.isEmpty() ? null : Integer.parseInt(string);
            }
        });

        minuteCombo.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.format("%02d", object);
            }
            @Override
            public Integer fromString(String string) {
                return string.isEmpty() ? null : Integer.parseInt(string);
            }
        });

        // Empêcher de sélectionner une date passée
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
        fileChooser.setTitle("Sélectionner un fichier PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File selectedFile = fileChooser.showOpenDialog(fichierField.getScene().getWindow());
        if (selectedFile != null) {
            // Validation du fichier
            if (selectedFile.length() > 10_000_000) { // 10MB max
                showAlert("Erreur", "Le fichier est trop volumineux (max 10MB)");
                return;
            }

            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                showAlert("Erreur", "Seuls les fichiers PDF sont acceptés");
                return;
            }

            fichierField.setText(selectedFile.getAbsolutePath());

            // Auto-remplissage du titre
            if (titreID.getText().isEmpty()) {
                titreID.setText(selectedFile.getName().replace(".pdf", ""));
            }
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Validation des champs
            if (titreID.getText().isEmpty() || descriptionID.getText().isEmpty()
                    || fichierField.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont obligatoires");
            }

            // Validation date et heure
            if (datePicker.getValue() == null || hourCombo.getValue() == null || minuteCombo.getValue() == null) {
                throw new IllegalArgumentException("Veuillez sélectionner une date et une heure valide");
            }

            File pdfFile = new File(fichierField.getText());
            if (!pdfFile.exists()) {
                throw new IllegalArgumentException("Le fichier sélectionné n'existe pas");
            }

            // Création de la date limite
            LocalDateTime dateLimite = LocalDateTime.of(
                    datePicker.getValue(),
                    LocalTime.of(hourCombo.getValue(), minuteCombo.getValue())
            );

            // Enregistrement du PDF
            String savedFileName = saveUploadedFile(pdfFile);

            // Création du devoir
            Devoir devoir = new Devoir(
                    titreID.getText(),
                    descriptionID.getText(),
                    dateLimite,
                    savedFileName
            );

            // Sauvegarde en base
            if (ds1.ajouter(devoir)) {
                showAlert("Succès", "Devoir créé avec succès");
                resetForm();
            } else {
                throw new Exception("Échec de l'enregistrement en base de données");
            }
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private String saveUploadedFile(File file) throws IOException {
        Path uploadDir = Paths.get("uploads/devoirs");
        Files.createDirectories(uploadDir);

        String fileName = System.currentTimeMillis() + "_" + file.getName();
        Path destination = uploadDir.resolve(fileName);

        Files.copy(file.toPath(), destination);
        return fileName;
    }

    private void resetForm() {
        titreID.clear();
        descriptionID.clear();
        datePicker.setValue(null);
        hourCombo.getSelectionModel().select(12);
        minuteCombo.getSelectionModel().select(0);
        fichierField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}