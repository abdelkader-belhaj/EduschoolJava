package tn.eduskool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure the path to the FXML file is correct
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("EduSkool");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
