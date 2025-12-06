package com.example.photobooth;

import com.example.photobooth.controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/start.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Tara Shot");

        StartController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
