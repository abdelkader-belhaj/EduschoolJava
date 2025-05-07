package tn.eduskool.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        // ...existing code...
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        launch(args);
    }
}
