package tn.eduskool.test;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class mainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/login_view.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Personne");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
