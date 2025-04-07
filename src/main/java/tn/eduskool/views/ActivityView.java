package tn.eduskool.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.eduskool.entities.Activity;
import tn.eduskool.services.ServiceActivity;

import java.io.File;
import java.sql.Date;
import java.util.List;

public class ActivityView {

    private TextField titreField, descriptionField, imageFileNameField, typesActivityField, idField;
    private DatePicker dateField;
    private CheckBox isApprovedCheckBox;
    private ListView<Activity> activityListView;
    private ImageView imageView;

    public Scene createScene(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #f4f4f4;");
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Activity Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER);

        VBox inputBox = createInputForm(primaryStage);
        inputBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        inputBox.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        inputBox.setPadding(new Insets(20));
        inputBox.setPrefWidth(400);

        VBox listContainer = createActivityListView();
        listContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        listContainer.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        listContainer.setPadding(new Insets(20));
        listContainer.setPrefWidth(500);

        mainContainer.getChildren().addAll(inputBox, listContainer);
        root.getChildren().addAll(titleLabel, mainContainer);

        refreshActivities();

        return new Scene(root, 1200, 800);
    }

    private VBox createInputForm(Stage primaryStage) {
        idField = createStyledTextField("ID (pour modifier ou supprimer)");
        titreField = createStyledTextField("Titre de l'activité");
        descriptionField = createStyledTextField("Description de l'activité");
        dateField = new DatePicker();
        dateField.setStyle("-fx-background-radius: 5; -fx-control-inner-background: white;");
        imageFileNameField = createStyledTextField("Nom de l'image");
        imageFileNameField.setEditable(false);
        isApprovedCheckBox = new CheckBox("Approuvé");
        isApprovedCheckBox.setStyle("-fx-font-size: 14px;");
        typesActivityField = createStyledTextField("Type d'activité");

        Button createBtn = createStyledButton("Ajouter", "primary");
        Button updateBtn = createStyledButton("Modifier", "secondary");
        Button deleteBtn = createStyledButton("Supprimer", "danger");
        Button refreshBtn = createStyledButton("Afficher", "info");
        Button chooseImageBtn = createStyledButton("Choisir l'image", "accent");
        Button showCommentsBtn = createStyledButton("Commentaires", "info");

        chooseImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Fichiers Image", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                imageFileNameField.setText(selectedFile.getAbsolutePath());
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
                imageView.setVisible(true);
            }
        });

        imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.setVisible(false);

        createBtn.setOnAction(e -> {
            String titre = titreField.getText().trim();
            String description = descriptionField.getText().trim();
            String imageFileName = imageFileNameField.getText().trim();
            boolean isApproved = isApprovedCheckBox.isSelected();
            String typesActivity = typesActivityField.getText().trim();
            if (titre.isEmpty() || description.isEmpty() || imageFileName.isEmpty() ||
                    typesActivity.isEmpty() || dateField.getValue() == null) {
                showAlert("Tous les champs sont requis.");
                return;
            }
            boolean success = ServiceActivity.createActivity(
                    titre, description, Date.valueOf(dateField.getValue()),
                    imageFileName, isApproved, typesActivity,
                    new java.sql.Date(System.currentTimeMillis()));
            showAlert(success ? "Activité ajoutée." : "Échec de l'ajout.");
            refreshActivities();
        });

        updateBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String titre = titreField.getText().trim();
                String description = descriptionField.getText().trim();
                String imageFileName = imageFileNameField.getText().trim();
                boolean isApproved = isApprovedCheckBox.isSelected();
                String typesActivity = typesActivityField.getText().trim();
                if (titre.isEmpty() || description.isEmpty() || imageFileName.isEmpty() ||
                        typesActivity.isEmpty() || dateField.getValue() == null) {
                    showAlert("Tous les champs sont requis.");
                    return;
                }
                boolean success = ServiceActivity.updateActivity(
                        id, titre, description, Date.valueOf(dateField.getValue()),
                        imageFileName, isApproved, typesActivity);
                showAlert(success ? "Activité modifiée." : "Activité non trouvée.");
                refreshActivities();
            } catch (NumberFormatException ex) {
                showAlert("ID invalide.");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                boolean success = ServiceActivity.deleteActivity(id);
                showAlert(success ? "Activité supprimée." : "Activité non trouvée.");
                refreshActivities();
            } catch (NumberFormatException ex) {
                showAlert("ID invalide.");
            }
        });

        refreshBtn.setOnAction(e -> refreshActivities());

        showCommentsBtn.setOnAction(e -> {
            tn.eduskool.views.CommentaireView commentaireView = new tn.eduskool.views.CommentaireView();
            Stage commentaireStage = new Stage();
            commentaireView.start(commentaireStage);
        });

        VBox inputBox = new VBox(10,
                new Label("ID :"), idField,
                new Label("Titre :"), titreField,
                new Label("Description :"), descriptionField,
                new Label("Date :"), dateField,
                new Label("Nom de l'image :"), imageFileNameField,
                chooseImageBtn,
                imageView,
                isApprovedCheckBox,
                new Label("Type d'activité :"), typesActivityField,
                new HBox(10, createBtn, updateBtn, deleteBtn, refreshBtn, showCommentsBtn));
        return inputBox;
    }

    private VBox createActivityListView() {
        activityListView = new ListView<>();
        activityListView.setCellFactory(param -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ImageView cellImageView = new ImageView();
                    File imageFile = new File(item.getImageFileName());
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString(), 100, 100, true, true);
                        cellImageView.setImage(image);
                    }
                    Label textLabel = new Label(item.toString());
                    textLabel.setStyle("-fx-font-size: 14px;");
                    HBox hBox = new HBox(10, cellImageView, textLabel);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5));
                    setGraphic(hBox);
                }
            }
        });

        VBox listContainer = new VBox(10, new Label("Liste des Activités"), activityListView);
        return listContainer;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshActivities() {
        List<Activity> activities = ServiceActivity.getAllActivities();
        ObservableList<Activity> observableList = FXCollections.observableArrayList(activities);
        activityListView.setItems(observableList);
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-background-radius: 5; -fx-control-inner-background: white; -fx-font-size: 14px;");
        return textField;
    }

    private Button createStyledButton(String text, String style) {
        Button button = new Button(text);
        button.setStyle(getButtonStyle(style));
        return button;
    }

    private String getButtonStyle(String type) {
        switch (type) {
            case "primary":
                return "-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;";
            case "secondary":
                return "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5;";
            case "danger":
                return "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;";
            case "info":
                return "-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-background-radius: 5;";
            default:
                return "-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 5;";
        }
    }
}
