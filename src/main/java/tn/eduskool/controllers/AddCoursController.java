package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;
import tn.eduskool.services.ThemeService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddCoursController {

    @FXML
    private TextField titreField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    @FXML
    private TextField enseignantField;

    @FXML
    private ComboBox<Theme> themeComboBox;

    @FXML
    private TextField fichierField; // Nouveau champ pour le chemin du fichier PDF

    @FXML
    private Button chooseFileButton; // Bouton pour ouvrir le FileChooser

    private CoursService coursService;
    private ThemeService themeService;

    @FXML
    public void initialize() {
        coursService = new CoursService();
        themeService = new ThemeService();

        // Restreindre le DatePicker aux dates d'aujourd'hui ou futures
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        loadThemes();
    }

    private void loadThemes() {
        try {
            List<Theme> themes = themeService.recuperer();
            themeComboBox.getItems().addAll(themes);
        } catch (SQLException e) {
            showAlert(" erreur", "Erreur lors du chargement des thèmes", e.getMessage());
        }
    }

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");
        // Filtrer pour n'accepter que les fichiers PDF
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        if (selectedFile != null) {
            fichierField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleSave() {
        // Validation des champs
        if (titreField.getText().isEmpty() ||
                datePicker.getValue() == null ||
                timeField.getText().isEmpty() ||
                enseignantField.getText().isEmpty() ||
                themeComboBox.getValue() == null ||
                fichierField.getText().isEmpty()) {
            showAlert("Erreur de validation", "Champs manquants", "Veuillez remplir tous les champs, y compris le fichier PDF.");
            return;
        }

        try {
            // Vérifier que le fichier est un PDF
            if (!fichierField.getText().toLowerCase().endsWith(".pdf")) {
                showAlert("Erreur de validation", "Fichier invalide", "Veuillez sélectionner un fichier PDF.");
                return;
            }

            // Convertir la date et l'heure en LocalDateTime
            LocalDate date = datePicker.getValue();
            LocalTime time = parseTime(timeField.getText());
            if (time == null) {
                showAlert("Format incorrect", "Format de temps invalide", "Veuillez entrer l'heure au format HH:mm (ex: 14:30)");
                return;
            }

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            // Créer un nouveau cours
            Cours nouveauCours = new Cours(
                    titreField.getText(),
                    dateTime,
                    enseignantField.getText(),
                    themeComboBox.getValue(),
                    fichierField.getText()
            );

            // Sauvegarder le cours
            coursService.ajouter(nouveauCours);

            // Fermer la fenêtre
            closeWindow();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du cours", e.getMessage());
        }
    }

    private LocalTime parseTime(String timeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}