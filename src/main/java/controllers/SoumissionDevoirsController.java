package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.DevoirService;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SoumissionDevoirsController {

    @FXML private TableView<Devoir> tableDevoirs;
    @FXML private TableColumn<Devoir, String> colTitre;
    @FXML private TableColumn<Devoir, String> colDescription;
    @FXML private TableColumn<Devoir, String> colDate;
    @FXML private TableColumn<Devoir, Void> colTelecharger;
    @FXML private TableColumn<Devoir, Void> colSoumettre;
    @FXML private Button btnVoirSoumissions;

    private final DevoirService devoirService = new DevoirService();
    private ListeSoumissionsController listeSoumissionsController;

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDevoirs();
    }

    private void configurerColonnes() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedDate())
        );

        colTelecharger.setCellFactory(getButtonCellFactory("Télécharger", this::telechargerDevoir));
        colSoumettre.setCellFactory(getButtonCellFactory("Soumettre", this::ouvrirFormulaireSoumission));
    }

    private void chargerDevoirs() {
        List<Devoir> devoirs = devoirService.recuperer();
        tableDevoirs.getItems().setAll(devoirs);
    }

    private Callback<TableColumn<Devoir, Void>, TableCell<Devoir, Void>> getButtonCellFactory(
            String buttonText, java.util.function.Consumer<Devoir> action) {
        return param -> new TableCell<>() {
            private final Button btn = new Button(buttonText);

            {
                btn.setOnAction(event -> {
                    Devoir devoir = getTableView().getItems().get(getIndex());
                    action.accept(devoir);
                });
                btn.setStyle("-fx-background-color: #0b84ff; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };
    }

    private void telechargerDevoir(Devoir devoir) {
        try {
            String nomFichier = devoir.getFichier();
            File fichierPDF = new File("uploads/devoirs/" + nomFichier);

            if (!fichierPDF.exists()) {
                showAlert("Fichier introuvable", "Le fichier n'existe pas !");
                return;
            }

            File destination = new File("C:/Nouveau dossier/" + nomFichier);
            Files.copy(fichierPDF.toPath(), destination.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            showAlert("Succès", "Fichier téléchargé dans C:\\Nouveau dossier !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de télécharger le fichier !");
        }
    }

    private void ouvrirFormulaireSoumission(Devoir devoir) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SoumissionForm.fxml"));
            Parent root = loader.load();

            SoumissionFormController controller = loader.getController();
            controller.setDevoir(devoir);
            controller.setSoumissionDevoirsController(this);

            Stage stage = new Stage();
            stage.setTitle("Soumettre un devoir");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de soumission.");
        }
    }

    @FXML
    private void ouvrirListeSoumissions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeSoumissions.fxml"));
            Parent root = loader.load();

            ListeSoumissionsController controller = loader.getController();
            controller.chargerSoumissions();
            this.listeSoumissionsController = controller;

            Stage stage = new Stage();
            stage.setTitle("Liste des Soumissions");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des soumissions.");
        }
    }

    public void rafraichirListeSoumissions() {
        if (listeSoumissionsController != null) {
            listeSoumissionsController.rafraichirTable();
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