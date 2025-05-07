package tn.eduskool.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import tn.eduskool.entities.Devoir;
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.services.DevoirService;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import tn.eduskool.services.SoumissionDevoirService;
import tn.eduskool.entities.SoumissionDevoir;

public class ListeDevoirsEtudiantController implements BaseController {
    @FXML
    private TableView<Devoir> devoirTable;
    @FXML
    private TableColumn<Devoir, String> titreCol;
    @FXML
    private TableColumn<Devoir, String> descriptionCol;
    @FXML
    private TableColumn<Devoir, String> dateLimiteCol;
    @FXML
    private TableColumn<Devoir, String> fichierCol;
    @FXML
    private TableColumn<Devoir, Void> actionsCol;

    private final DevoirService devoirService = new DevoirService();
    private Utilisateur utilisateur;
    private final SoumissionDevoirService soumissionService = new SoumissionDevoirService();

    @FXML
    public void initialize() {
        configurerColonnes();
    }

    private void configurerColonnes() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateLimiteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedDate()));
        fichierCol.setCellValueFactory(new PropertyValueFactory<>("fichier"));

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button downloadBtn = new Button("Télécharger");
            private final Button submitBtn = new Button("Soumettre");
            private final HBox box = new HBox(5, downloadBtn, submitBtn);

            {
                downloadBtn.setOnAction(e -> telechargerDevoir(getTableView().getItems().get(getIndex())));
                submitBtn.setOnAction(e -> soumettreSolution(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerDevoirs();
    }

    public void chargerDevoirs() {
        try {
            List<Devoir> devoirs = devoirService.recuperer();
            devoirTable.getItems().setAll(devoirs);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les devoirs");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private void telechargerDevoir(Devoir devoir) {
        try {
            File sourceFile = new File("uploads/devoirs/" + devoir.getFichier());
            if (!sourceFile.exists()) {
                showAlert("Erreur", "Le fichier n'existe pas");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(devoir.getFichier());
            File destFile = fileChooser.showSaveDialog(null);

            if (destFile != null) {
                Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showAlert("Succès", "Fichier téléchargé avec succès");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du téléchargement: " + e.getMessage());
        }
    }

    private void soumettreSolution(Devoir devoir) {
        try {
            Path uploadsPath = Paths.get("uploads/soumissions");
            Files.createDirectories(uploadsPath);

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                String fileName = "soumission_" + devoir.getId() + "_" +
                        System.currentTimeMillis() + ".pdf";

                Path destinationPath = uploadsPath.resolve(fileName);
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                SoumissionDevoir soumission = new SoumissionDevoir();
                soumission.setDevoir(devoir);
                soumission.setFichier(fileName);
                soumission.setDateSoumission(LocalDateTime.now());

                if (soumissionService.ajouter(soumission)) {
                    showAlert("Succès", "Solution soumise avec succès");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la manipulation des fichiers: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la soumission: " + e.getMessage());
        }
    }
}
