package com.example.photobooth.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class StartController {

    @FXML
    private Button startButton;

    private Stage primaryStage;
    private MediaPlayer startPlayer;
    private MediaPlayer bgPlayer;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        try {
            Media bgSound = new Media(new File("sound/start-bg.mp3").toURI().toString());
            bgPlayer = new MediaPlayer(bgSound);
            bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgPlayer.play();
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void start() {
        try {
            try {
                Media startSound = new Media(new File("sound/start.mp3").toURI().toString());
                startPlayer = new MediaPlayer(startSound);
                startPlayer.play();
            } catch (Exception e) {
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photobooth.fxml"));
            Parent root = loader.load();

            PhotoboothController photoboothController = loader.getController();
            photoboothController.setPrimaryStage(primaryStage);
            photoboothController.setBgPlayer(bgPlayer);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tara Shot - Photo Booth");
            primaryStage.setWidth(1280);
            primaryStage.setHeight(800);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
        }
    }
}