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

public class EditCoursController {

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

    private CoursService coursService = new CoursService();
    private ThemeService themeService = new ThemeService();

    private Cours coursToEdit;

    public void setCoursToEdit(Cours cours) {
        this.coursToEdit = cours;
        populateFields();
    }

    @FXML
    public void initialize() {
        try {
            // Charger les thèmes disponibles
            List<Theme> themes = themeService.recuperer();
            themeComboBox.getItems().addAll(themes);

            // Restreindre le DatePicker aux dates d'aujourd'hui ou futures, mais permettre la date actuelle du cours
            datePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    // Désactiver les dates avant aujourd'hui, sauf si c'est la date actuelle du cours
                    boolean isCurrentCourseDate = coursToEdit != null && coursToEdit.getDateHeure() != null &&
                            date.equals(coursToEdit.getDateHeure().toLocalDate());
                    setDisable(empty || (date.isBefore(LocalDate.now()) && !isCurrentCourseDate));
                }
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des thèmes", e.getMessage());
        }
    }

    private void populateFields() {
        if (coursToEdit != null) {
            titreField.setText(coursToEdit.getTitre());
            datePicker.setValue(coursToEdit.getDateHeure().toLocalDate());
            timeField.setText(coursToEdit.getDateHeure().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            enseignantField.setText(coursToEdit.getEnseignant());
            themeComboBox.setValue(coursToEdit.getTheme());
            fichierField.setText(coursToEdit.getFichier()); // Remplir le champ fichier
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
        try {
            // Validation des champs
            if (titreField.getText().isEmpty() || datePicker.getValue() == null ||
                    timeField.getText().isEmpty() || enseignantField.getText().isEmpty() ||
                    themeComboBox.getValue() == null || fichierField.getText().isEmpty()) {
                showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs, y compris le fichier PDF.");
                return;
            }

            // Vérifier que le fichier est un PDF
            if (!fichierField.getText().toLowerCase().endsWith(".pdf")) {
                showAlert("Erreur", "Fichier invalide", "Veuillez sélectionner un fichier PDF.");
                return;
            }

            // Conversion de la date et heure
            LocalDate date = datePicker.getValue();
            LocalTime time;
            try {
                time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format de temps invalide", "Veuillez entrer l'heure au format HH:mm (ex: 14:30)");
                return;
            }

            LocalDateTime dateHeure = LocalDateTime.of(date, time);

            // Mise à jour du cours
            coursToEdit.setTitre(titreField.getText());
            coursToEdit.setDateHeure(dateHeure);
            coursToEdit.setEnseignant(enseignantField.getText());
            coursToEdit.setTheme(themeComboBox.getValue());
            coursToEdit.setFichier(fichierField.getText()); // Mettre à jour le fichier

            // Mise à jour en base de données
            coursService.modifier(coursToEdit);

            // Fermeture de la fenêtre
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification du cours", e.getMessage());
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