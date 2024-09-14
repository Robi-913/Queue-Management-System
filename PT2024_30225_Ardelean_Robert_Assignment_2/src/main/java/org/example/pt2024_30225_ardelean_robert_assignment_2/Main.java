package org.example.pt2024_30225_ardelean_robert_assignment_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 550);

        Image icon = null;

        if (getClass().getResourceAsStream("src/main/resources/org/example/pt2024_30225_ardelean_robert_assignment_2/Icon/img.png") != null) {
            icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("src/main/resources/org/example/pt2024_30225_ardelean_robert_assignment_2/Icon/img.png")));
        }

        if (icon != null) {
            stage.getIcons().add(icon);
        }

        stage.setTitle("QUEUES MANAGEMENT");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
