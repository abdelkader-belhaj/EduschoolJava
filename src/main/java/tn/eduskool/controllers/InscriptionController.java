package tn.eduskool.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import tn.eduskool.entities.Utilisateur;
import tn.eduskool.services.UtilisateurService;
import tn.eduskool.tools.DatabaseConnection;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.UUID;

public class InscriptionController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField cinField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private PasswordField confirmMotDePasseField;
    @FXML
    private TextField adresseField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private TextField telephoneField;
    @FXML
    private ComboBox<String> typeUtilisateurComboBox;
    @FXML
    private ImageView photoImageView;
    @FXML
    private Button uploadPhotoButton;
    @FXML
    private Button inscrireButton;

    private UtilisateurService utilisateurService;
    private File selectedPhotoFile;
    private String photoPath;

    @FXML
    public void initialize() {
        // Initialiser la connexion à la base de données
        Connection connection = DatabaseConnection.getInstance().getCnx();
        utilisateurService = new UtilisateurService(connection);

        // Initialiser le ComboBox avec les types d'utilisateurs (sans admin)
        typeUtilisateurComboBox.getItems().addAll("Étudiant", "Enseignant");
        typeUtilisateurComboBox.setValue("Étudiant");

        // Initialiser le bouton d'upload de photo
        uploadPhotoButton.setOnAction(event -> uploadPhoto());
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            // Créer un nouvel utilisateur avec les données du formulaire
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nomField.getText().trim());
            utilisateur.setPrenom(prenomField.getText().trim());
            utilisateur.setCin(Integer.parseInt(cinField.getText().trim()));
            utilisateur.setEmail(emailField.getText().trim());
            utilisateur.setMotDePasse(motDePasseField.getText());
            utilisateur.setAdresse(adresseField.getText().trim());
            utilisateur.setDateNaissance(dateNaissancePicker.getValue());
            utilisateur.setTelephone(Integer.parseInt(telephoneField.getText().trim()));
            utilisateur.setPhoto(photoPath);

            // Déterminer le type d'utilisateur
            String typeSelectionne = typeUtilisateurComboBox.getValue();
            int idUtilisateur;

            switch (typeSelectionne) {
                case "Enseignant":
                    idUtilisateur = utilisateurService.inscrireEnseignant(utilisateur);
                    break;
                case "Étudiant":
                default:
                    idUtilisateur = utilisateurService.inscrireEtudiant(utilisateur);
                    break;
            }

            if (idUtilisateur > 0) {
                // Copier la photo sélectionnée vers le dossier de stockage
                if (selectedPhotoFile != null) {
                    savePhoto(idUtilisateur);
                }

                showAlert(Alert.AlertType.INFORMATION, "Inscription réussie",
                        "Votzzre compte a été créé avec succès. Vous pourrez vous connecter après vérification par un administrateur.");

                // Réinitialiser le formulaire
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur d'inscription",
                        "Une erreur s'est produite lors de l'inscription. Veuillez réessayer.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie",
                    "Veuillez vérifier que les champs numériques (CIN, téléphone) contiennent des nombres valides.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Une erreur s'est produite: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        // Vérifier que tous les champs sont remplis
        if (nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() ||
                cinField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                motDePasseField.getText().isEmpty() ||
                confirmMotDePasseField.getText().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                dateNaissancePicker.getValue() == null ||
                telephoneField.getText().trim().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Champs manquants",
                    "Veuillez remplir tous les champs obligatoires.");
            return false;
        }

        // Vérifier que les mots de passe correspondent
        if (!motDePasseField.getText().equals(confirmMotDePasseField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Mots de passe différents",
                    "Les mots de passe ne correspondent pas.");
            return false;
        }

        // Vérifier que l'email n'est pas déjà utilisé
        if (utilisateurService.emailExiste(emailField.getText().trim())) {
            showAlert(Alert.AlertType.WARNING, "Email déjà utilisé",
                    "Cet email est déjà associé à un compte.");
            return false;
        }

        // Vérifier que le CIN n'est pas déjà utilisé
        try {
            int cin = Integer.parseInt(cinField.getText().trim());
            if (utilisateurService.cinExiste(cin)) {
                showAlert(Alert.AlertType.WARNING, "CIN déjà utilisé",
                        "Ce CIN est déjà associé à un compte.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "CIN invalide",
                    "Le CIN doit être un nombre entier.");
            return false;
        }

        // Vérifier que l'âge est suffisant (par exemple, au moins 16 ans)
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        LocalDate aujourdhui = LocalDate.now();
        if (dateNaissance.plusYears(16).isAfter(aujourdhui)) {
            showAlert(Alert.AlertType.WARNING, "Âge insuffisant",
                    "Vous devez avoir au moins 16 ans pour vous inscrire.");
            return false;
        }

        return true;
    }

    private void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        selectedPhotoFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());

        if (selectedPhotoFile != null) {
            try {
                Image image = new Image(selectedPhotoFile.toURI().toString());
                photoImageView.setImage(image);
                photoImageView.setFitWidth(150);
                photoImageView.setFitHeight(150);
                photoImageView.setPreserveRatio(true);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Impossible de charger l'image: " + e.getMessage());
            }
        }
    }

    private void savePhoto(int idUtilisateur) {
        try {
            // Créer le dossier de stockage des photos s'il n'existe pas
            String uploadDir = "uploads/profils/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom de fichier unique
            String extension = selectedPhotoFile.getName().substring(selectedPhotoFile.getName().lastIndexOf('.'));
            String fileName = "user_" + idUtilisateur + "_" + UUID.randomUUID().toString() + extension;
            Path destination = Paths.get(uploadDir + fileName);

            // Copier le fichier
            Files.copy(selectedPhotoFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour le chemin de la photo dans la base de données
            photoPath = destination.toString();

            // Mettre à jour l'utilisateur dans la base de données
            utilisateurService.updatePhoto(idUtilisateur, photoPath);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'enregistrer la photo: " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        cinField.clear();
        emailField.clear();
        motDePasseField.clear();
        confirmMotDePasseField.clear();
        adresseField.clear();
        dateNaissancePicker.setValue(null);
        telephoneField.clear();
        typeUtilisateurComboBox.setValue("Étudiant");
        photoImageView.setImage(null);
        selectedPhotoFile = null;
        photoPath = null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}