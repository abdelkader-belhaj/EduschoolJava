package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import tn.eduskool.entities.Devoir;
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.services.DevoirService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SoumissionDevoirsController implements BaseController {

    @FXML
    private TableView<Devoir> tableDevoirs;
    @FXML
    private TableColumn<Devoir, String> colTitre;
    @FXML
    private TableColumn<Devoir, String> colDescription;
    @FXML
    private TableColumn<Devoir, String> colDate;
    @FXML
    private TableColumn<Devoir, Void> colTelecharger;
    @FXML
    private TableColumn<Devoir, Void> colSoumettre;
    @FXML
    private Button btnVoirSoumissions;
    @FXML
    private Button btnNotifications;
    @FXML
    private TextField searchField;

    private final DevoirService devoirService = new DevoirService();
    private ListeSoumissionsController listeSoumissionsController;
    private List<Devoir> devoirsProches;
    private Utilisateur utilisateur;
    private final Gson gson = new Gson();
    private static final String TRANSLATE_SERVICE_URL = "http://example.com/translate";

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDate()));

        // Bouton télécharger
        colTelecharger.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Télécharger");
            {
                btn.getStyleClass().add("action-button-modifier");
                btn.setOnAction(e -> telechargerDevoir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Bouton soumettre
        colSoumettre.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Soumettre");
            {
                btn.getStyleClass().add("action-button-modifier");
                btn.setOnAction(e -> ouvrirFormulaireSoumission(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Menu contextuel pour la traduction
        tableDevoirs.setRowFactory(tv -> {
            TableRow<Devoir> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem traduireFrItem = new MenuItem("Traduire en Français");
            traduireFrItem.setOnAction(event -> {
                Devoir devoir = row.getItem();
                if (devoir != null) {
                    String texteTraduit = traduireDescription(devoir.getDescription(), "fr");
                    if (!texteTraduit.equals(devoir.getDescription())) {
                        devoir.setDescription(texteTraduit);
                        tableDevoirs.refresh();
                    }
                }
            });

            MenuItem traduireEnItem = new MenuItem("Traduire en Anglais");
            traduireEnItem.setOnAction(event -> {
                Devoir devoir = row.getItem();
                if (devoir != null) {
                    String texteTraduit = traduireDescription(devoir.getDescription(), "en");
                    if (!texteTraduit.equals(devoir.getDescription())) {
                        devoir.setDescription(texteTraduit);
                        tableDevoirs.refresh();
                    }
                }
            });

            contextMenu.getItems().addAll(traduireFrItem, traduireEnItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });

        chargerDevoirs();
        verifierDevoirsProches();
    }

    private String traduireDescription(String texteOriginal, String langueCible) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("text", texteOriginal);
            requestBody.addProperty("lang", langueCible);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRANSLATE_SERVICE_URL))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(gson.toJson(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                return jsonResponse.get("translated_text").getAsString();
            } else {
                afficherAlerte("Erreur de traduction", "Impossible de traduire le texte. Code d'erreur: " + response.statusCode());
                return texteOriginal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur de traduction", "Le service de traduction est indisponible.");
            return texteOriginal;
        }
    }

    private void verifierDevoirsProches() {
        devoirsProches = devoirService.recuperer();
    }

    private void chargerDevoirs() {
        List<Devoir> devoirsList = devoirService.recuperer();
        ObservableList<Devoir> observableDevoirs = FXCollections.observableArrayList(devoirsList);

        // Filtrer les données à afficher dans la table
        FilteredList<Devoir> filteredData = new FilteredList<>(observableDevoirs, p -> true);

        // Lire la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(devoir -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return devoir.getTitre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Tri si nécessaire
        SortedList<Devoir> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableDevoirs.comparatorProperty());

        tableDevoirs.setItems(sortedData);
    }

    private void telechargerDevoir(Devoir devoir) {
        try {
            String nomFichier = devoir.getFichier();
            File fichierPDF = new File("uploads/devoirs/" + nomFichier);

            if (!fichierPDF.exists()) {
                afficherAlerte("Fichier introuvable", "Le fichier n'existe pas !");
                return;
            }

            File destination = new File("C:/Nouveau dossier/" + nomFichier);
            Files.copy(fichierPDF.toPath(), destination.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            afficherAlerte("Succès", "Fichier téléchargé dans C:\\Nouveau dossier !");
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de télécharger le fichier !");
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
            afficherAlerte("Erreur", "Impossible d'ouvrir le formulaire de soumission.");
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
            afficherAlerte("Erreur", "Impossible d'ouvrir la liste des soumissions.");
        }
    }

    public void rafraichirListeSoumissions() {
        if (listeSoumissionsController != null) {
            listeSoumissionsController.rafraichirTable();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void afficherNotifications() {
        ContextMenu contextMenu = new ContextMenu();

        LocalDate today = LocalDate.now();
        for (Devoir devoir : devoirsProches) {
            if (devoir.getDatelimite() != null) {
                long joursRestants = ChronoUnit.DAYS.between(today, devoir.getDatelimite());

                if (joursRestants >= 0 && joursRestants <= 2) {
                    MenuItem item = new MenuItem(devoir.getTitre() + " (échéance dans " +
                            (joursRestants == 0 ? "aujourd'hui" : joursRestants + " jours") + ")");
                    contextMenu.getItems().add(item);
                }
            }
        }

        if (contextMenu.getItems().isEmpty()) {
            MenuItem noNotif = new MenuItem("Aucune échéance proche");
            contextMenu.getItems().add(noNotif);
        }

        contextMenu.show(btnNotifications, javafx.geometry.Side.BOTTOM, 0, 0);
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerDevoirs();
    }

    @Override
    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }
}
