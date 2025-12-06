package com.example.photobooth.controller;

import com.example.photobooth.model.Photo;
import com.example.photobooth.model.Strip;
import com.example.photobooth.model.Filter;
import com.example.photobooth.model.BWFilter;
import com.example.photobooth.model.SepiaFilter;
import com.example.photobooth.model.VintageFilter;
import com.example.photobooth.model.PolaroidFilter;
import com.example.photobooth.model.HighContrastFilter;
import com.example.photobooth.model.CoolToneFilter;
import com.example.photobooth.model.WarmToneFilter;
import com.example.photobooth.model.InvertFilter;
import com.example.photobooth.model.GrayscaleFilter;
import com.example.photobooth.model.FilmGrainFilter;
import com.example.photobooth.model.FadedFilter;
import com.example.photobooth.model.KodachromeFilter;
import com.example.photobooth.model.LomographyFilter;
import com.example.photobooth.model.NoFilter;
import com.example.photobooth.model.InvertedFilter;
import com.example.photobooth.model.BulgeFilter;
import com.example.photobooth.model.PinchFilter;
import com.example.photobooth.model.SwirlFilter;
import com.example.photobooth.model.WaveFilter;
import com.example.photobooth.model.FisheyeFilter;
import com.example.photobooth.model.MirrorFilter;
import com.example.photobooth.model.FlipFilter;
import com.example.photobooth.model.RotateFilter;
import com.example.photobooth.model.ZoomFilter;
import com.example.photobooth.model.StretchFilter;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PhotoboothController {

    @FXML
    private ImageView webcamView;

    @FXML
    private ImageView previewView;

    @FXML
    private ImageView stripView;

    @FXML
    private Button captureButton;



    @FXML
    private Button saveButton;

    @FXML
    private Label timerLabel;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private ChoiceBox<String> distortionChoiceBox;

    @FXML
    private ChoiceBox<String> layoutChoiceBox;

    @FXML
    private ChoiceBox<String> timerChoiceBox;

    @FXML
    private ImageView soundIcon;



    private Stage primaryStage;
    private Webcam webcam;
    private List<Photo> photos = new ArrayList<>();
    private Filter currentColorFilter = new NoFilter();
    private Filter currentDistortionFilter = null;
    private Strip.Layout currentLayout = Strip.Layout.VERTICAL_3;
    private int timerDuration = 3;
    private boolean isCapturing = false;
    private String currentFrameColor = "#FFB3BA"; // Default frame color
    private MediaPlayer shutterPlayer;
    private MediaPlayer buttonPlayer;
    private MediaPlayer bgPlayer;
    private MediaPlayer timerPlayer;


   
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setBgPlayer(MediaPlayer bgPlayer) {
        this.bgPlayer = bgPlayer;
    }

    @FXML
    public void initialize() {
        setupUI();


        requestCameraPermission();
    }
    
    private void requestCameraPermission() {
        Platform.runLater(() -> {
   
            Stage permissionStage = new Stage();
            permissionStage.initModality(Modality.APPLICATION_MODAL);
            permissionStage.initStyle(StageStyle.UNDECORATED); 
            permissionStage.setTitle("Camera Permission");
            permissionStage.setResizable(false);
            
       
            VBox permissionBox = new VBox(20);
            permissionBox.setAlignment(Pos.CENTER);
            permissionBox.setPadding(new Insets(30));
            permissionBox.getStyleClass().add("permission-dialog");
            
            Label titleLabel = new Label("Camera Access Required");
            titleLabel.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: linear-gradient(to right, #e5667c, #d44d6a);" +
                "-fx-padding: 0 0 10 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.2), 5, 0.3, 0, 1);"
            );
            
            Label messageLabel = new Label("Tara Shot needs access to your camera to take photos.\nPlease allow camera access to continue.");
            messageLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #444;" +
                "-fx-wrap-text: true;" +
                "-fx-text-alignment: center;" +
                "-fx-padding: 0 20;"
            );
            
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setStyle("-fx-padding: 15 0 0 0;");
            
            Button allowButton = new Button("Allow");
            allowButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #a2c26a, #8aac55);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 800;" +
                "-fx-padding: 12 35;" +
                "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.4), 10, 0.4, 0, 2);" +
                "-fx-min-width: 120px;"
            );
            
            Button denyButton = new Button("Deny");
            denyButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #e5667c, #d44d6a);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 800;" +
                "-fx-padding: 12 35;" +
                "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 10, 0.4, 0, 2);" +
                "-fx-min-width: 120px;"
            );
            
            
            allowButton.setOnMouseEntered(e -> allowButton.setStyle(
                allowButton.getStyle() +
                "-fx-background-color: linear-gradient(to bottom right, #8aac55, #a2c26a);" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;" +
                "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.6), 15, 0.5, 0, 3);"
            ));
            allowButton.setOnMouseExited(e -> allowButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #a2c26a, #8aac55);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 800;" +
                "-fx-padding: 12 35;" +
                "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.4), 10, 0.4, 0, 2);" +
                "-fx-min-width: 120px;"
            ));
            
            denyButton.setOnMouseEntered(e -> denyButton.setStyle(
                denyButton.getStyle() +
                "-fx-background-color: linear-gradient(to bottom right, #d44d6a, #e5667c);" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;" +
                "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.6), 15, 0.5, 0, 3);"
            ));
            denyButton.setOnMouseExited(e -> denyButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #e5667c, #d44d6a);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 800;" +
                "-fx-padding: 12 35;" +
                "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 10, 0.4, 0, 2);" +
                "-fx-min-width: 120px;"
            ));
            
            allowButton.setOnAction(e -> {
                permissionStage.close();
                startCamera();
            });
            
            denyButton.setOnAction(e -> {
                permissionStage.close();
                closeApplication();
            });
            
            buttonBox.getChildren().addAll(allowButton, denyButton);
            permissionBox.getChildren().addAll(titleLabel, messageLabel, buttonBox);
            
            Scene permissionScene = new Scene(permissionBox, 400, 250);
            
            permissionStage.setScene(permissionScene);
            
     
            if (primaryStage != null) {
                permissionStage.initOwner(primaryStage);
                permissionStage.setX(primaryStage.getX() + (primaryStage.getWidth() - 400) / 2);
                permissionStage.setY(primaryStage.getY() + (primaryStage.getHeight() - 250) / 2);
            } else {
           
                permissionStage.centerOnScreen();
            }
            
            permissionStage.showAndWait();
        });
    }
    
    private void closeApplication() {
        Platform.runLater(() -> {
            if (primaryStage != null) {
                primaryStage.close();
            }
            Platform.exit();
            System.exit(0);
        });
    }
    
    private void setupUI() {
        filterChoiceBox.getItems().addAll("No Filter", "Retro B&W", "Sepia", "Vintage", "Polaroid", "High Contrast", "Cool Tone", "Warm Tone", "Invert", "Grayscale", "Film Grain", "Faded", "Kodachrome", "Lomography");
        filterChoiceBox.setValue("No Filter");
        filterChoiceBox.setOnAction(e -> updateColorFilter());
        filterChoiceBox.setOnMousePressed(e -> playButtonSound());

        distortionChoiceBox.getItems().addAll("None", "Bulge", "Pinch", "Swirl", "Wave", "Fisheye", "Mirror", "Flip", "Rotate", "Zoom", "Stretch");
        distortionChoiceBox.setValue("None");
        distortionChoiceBox.setOnAction(e -> updateDistortionFilter());
        distortionChoiceBox.setOnMousePressed(e -> playButtonSound());

        layoutChoiceBox.getItems().addAll("Single", "Vertical 3", "Grid 2x2", "Horizontal 2", "Vertical 4", "Grid 3x3");
        layoutChoiceBox.setValue("Vertical 3");
        layoutChoiceBox.setOnAction(e -> updateLayout());
        layoutChoiceBox.setOnMousePressed(e -> playButtonSound());

        timerChoiceBox.getItems().addAll("0", "3", "5", "10");
        timerChoiceBox.setValue("3");
        timerChoiceBox.setOnAction(e -> updateTimer());
        timerChoiceBox.setOnMousePressed(e -> playButtonSound());

        saveButton.setVisible(false);
        previewView.setImage(null);

        stripView.setOnMouseClicked(this::showPreviewPopup);

        try {
            Media shutterSound = new Media(new File("sound/shutter.mp3").toURI().toString());
            shutterPlayer = new MediaPlayer(shutterSound);
        } catch (Exception e) {
            System.err.println("Error initializing shutter sound: " + e.getMessage());
        }

        try {
            Media buttonSound = new Media(new File("sound/button.mp3").toURI().toString());
            buttonPlayer = new MediaPlayer(buttonSound);
        } catch (Exception e) {
            System.err.println("Error initializing button sound: " + e.getMessage());
        }

        try {
            Media timerSound = new Media(new File("sound/timer.mp3").toURI().toString());
            timerPlayer = new MediaPlayer(timerSound);
        } catch (Exception e) {
            System.err.println("Error initializing timer sound: " + e.getMessage());
        }

        updateSoundIcon();
    }
    
    private void startCamera() {

        new Thread(() -> {
            try {

                System.out.println("Starting camera detection...");
                List<Webcam> allWebcams = Webcam.getWebcams();
                System.out.println("Total webcams detected: " + (allWebcams != null ? allWebcams.size() : "null"));

                if (allWebcams != null) {
                    for (int i = 0; i < allWebcams.size(); i++) {
                        Webcam w = allWebcams.get(i);
                        System.out.println("Webcam " + i + ": " + w.getName() + " - " + w.getDevice().getName());
                    }
                }

                Webcam foundWebcam = null;


                foundWebcam = Webcam.getDefault();
                System.out.println("Default webcam: " + (foundWebcam != null ? foundWebcam.getName() : "null"));


                if (foundWebcam == null) {
                    if (allWebcams != null && !allWebcams.isEmpty()) {
                        foundWebcam = allWebcams.get(0);
                        System.out.println("Using first available webcam: " + foundWebcam.getName());
                    }
                }


                if (foundWebcam == null) {
                    System.out.println("Trying to open any webcam...");
                    for (Webcam w : allWebcams) {
                        try {
                            System.out.println("Attempting to open: " + w.getName());
                            w.open();
                            foundWebcam = w;
                            System.out.println("Successfully opened: " + w.getName());
                            break;
                        } catch (Exception e) {
                            System.out.println("Failed to open " + w.getName() + ": " + e.getMessage());
                        }
                    }
                }

                if (foundWebcam != null) {
                    webcam = foundWebcam;
                    webcam.setViewSize(WebcamResolution.VGA.getSize());

                    try {
                        if (!webcam.isOpen()) {
                            webcam.open();
                        }
                        startWebcamThread();
                        System.out.println("Camera started successfully: " + webcam.getName());
                    } catch (Exception e) {
                        System.err.println("Failed to open webcam: " + e.getMessage());
                        Platform.runLater(() -> showNoCameraError("Failed to open webcam: " + e.getMessage()));
                    }
                } else {
                    System.err.println("No camera was found on your device");
                    Platform.runLater(() -> showNoCameraError("No camera was found on your device"));
                }
            } catch (Exception e) {
                System.err.println("Error accessing camera: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> showNoCameraError("Error accessing camera: " + e.getMessage()));
            }
        }).start();
    }
    
    private void showNoCameraError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No Camera Detected");
        alert.setHeaderText("Camera Not Available");
        alert.setContentText(message + "\nThe application will now close.");
        
        alert.showAndWait();
        
     
        captureButton.setDisable(true);
        captureButton.setText("NO CAMERA");
        
       
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    closeApplication();
                }
            },
            2000
        );
    }
    
    private void startWebcamThread() {
        Thread webcamThread = new Thread(() -> {
            while (webcam != null && webcam.isOpen()) {
                BufferedImage img = webcam.getImage();
                if (img != null) {
                    BufferedImage filteredImg = applyFilters(img);
                    Image fxImage = SwingFXUtils.toFXImage(filteredImg, null);
                    Platform.runLater(() -> webcamView.setImage(fxImage));
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        webcamThread.setDaemon(true);
        webcamThread.start();
    }

    private void updateColorFilter() {
        playButtonSound();
        String selectedFilter = filterChoiceBox.getValue();
        switch (selectedFilter) {
            case "No Filter": currentColorFilter = new NoFilter(); break;
            case "Retro B&W": currentColorFilter = new InvertedFilter(new BWFilter()); break;
            case "Sepia": currentColorFilter = new InvertedFilter(new SepiaFilter()); break;
            case "Vintage": currentColorFilter = new InvertedFilter(new VintageFilter()); break;
            case "Polaroid": currentColorFilter = new InvertedFilter(new PolaroidFilter()); break;
            case "High Contrast": currentColorFilter = new InvertedFilter(new HighContrastFilter()); break;
            case "Cool Tone": currentColorFilter = new InvertedFilter(new CoolToneFilter()); break;
            case "Warm Tone": currentColorFilter = new InvertedFilter(new WarmToneFilter()); break;
            case "Invert": currentColorFilter = new InvertedFilter(new InvertFilter()); break;
            case "Grayscale": currentColorFilter = new InvertedFilter(new GrayscaleFilter()); break;
            case "Film Grain": currentColorFilter = new InvertedFilter(new FilmGrainFilter()); break;
            case "Faded": currentColorFilter = new InvertedFilter(new FadedFilter()); break;
            case "Kodachrome": currentColorFilter = new InvertedFilter(new KodachromeFilter()); break;
            case "Lomography": currentColorFilter = new InvertedFilter(new LomographyFilter()); break;
            case "Cool Tilt": currentColorFilter = new InvertedFilter(new CoolToneFilter()); break;
            case "Warm Glow": currentColorFilter = new InvertedFilter(new WarmToneFilter()); break;
            case "Old Film": currentColorFilter = new InvertedFilter(new FadedFilter()); break;
            case "Soft Fade": currentColorFilter = new InvertedFilter(new FadedFilter()); break;
            case "Polaroid Vintage": currentColorFilter = new InvertedFilter(new PolaroidFilter()); break;
            case "Monochrome": currentColorFilter = new InvertedFilter(new GrayscaleFilter()); break;
        }
    }

    private void updateDistortionFilter() {
        playButtonSound();
        String selectedDistortion = distortionChoiceBox.getValue();
        switch (selectedDistortion) {
            case "None": currentDistortionFilter = null; break;
            case "Bulge": currentDistortionFilter = new BulgeFilter(); break;
            case "Pinch": currentDistortionFilter = new PinchFilter(); break;
            case "Swirl": currentDistortionFilter = new SwirlFilter(); break;
            case "Wave": currentDistortionFilter = new WaveFilter(); break;
            case "Fisheye": currentDistortionFilter = new FisheyeFilter(); break;
            case "Mirror": currentDistortionFilter = new InvertedFilter(new MirrorFilter()); break;
            case "Flip": currentDistortionFilter = new InvertedFilter(new FlipFilter()); break;
            case "Rotate": currentDistortionFilter = new InvertedFilter(new RotateFilter()); break;
            case "Zoom": currentDistortionFilter = new InvertedFilter(new ZoomFilter()); break;
            case "Stretch": currentDistortionFilter = new StretchFilter(); break;
        }
    }

    private BufferedImage applyFilters(BufferedImage image) {
        BufferedImage result = image;
        MirrorFilter mirrorFilter = new MirrorFilter();
        if (currentColorFilter != null) result = currentColorFilter.apply(result);
        if (currentDistortionFilter != null) result = currentDistortionFilter.apply(result);
        result = mirrorFilter.apply(result);
        return result;
    }







    private void playButtonSound() {
        try {
            if (buttonPlayer != null) {
                buttonPlayer.stop();
                buttonPlayer.seek(javafx.util.Duration.ZERO);
                buttonPlayer.play();
            }
        } catch (Exception e) {
        }
    }

    private void updateLayout() {
        playButtonSound();
        String selectedLayout = layoutChoiceBox.getValue();
        switch (selectedLayout) {
            case "Single": currentLayout = Strip.Layout.SINGLE; break;
            case "Vertical 3": currentLayout = Strip.Layout.VERTICAL_3; break;
            case "Grid 2x2": currentLayout = Strip.Layout.GRID_2x2; break;
            case "Horizontal 2": currentLayout = Strip.Layout.HORIZONTAL_2; break;
            case "Vertical 4": currentLayout = Strip.Layout.VERTICAL_4; break;
            case "Grid 3x3": currentLayout = Strip.Layout.GRID_3x3; break;
        }
    }

    private void updateTimer() {
        playButtonSound();
        String selectedTimer = timerChoiceBox.getValue();
        timerDuration = Integer.parseInt(selectedTimer);
    }



    @FXML
    private void capturePhoto() {
        int maxPhotos = getMaxPhotosForLayout();
        if (webcam != null && !isCapturing) {
            if (!photos.isEmpty()) {
                photos.clear();
                stripView.setImage(null);
                previewView.setImage(null);
                saveButton.setDisable(true);
                saveButton.setVisible(false);
            }
            isCapturing = true;
            captureButton.setDisable(true);
            Thread captureThread = new Thread(() -> {
                try {
                    capturePhotoSequence(maxPhotos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            captureThread.setDaemon(true);
            captureThread.start();
        }
    }

    private int getMaxPhotosForLayout() {
        switch (currentLayout) {
            case SINGLE: return 1;
            case VERTICAL_3: return 3;
            case GRID_2x2: return 4;
            case HORIZONTAL_2: return 2;
            case VERTICAL_4: return 4;
            case GRID_3x3: return 9;
            default: return 3;
        }
    }

    private void capturePhotoSequence(int maxPhotos) throws InterruptedException {
        for (int photoNum = 1; photoNum <= maxPhotos; photoNum++) {
            final int currentPhotoNum = photoNum;
            if (timerDuration > 0) {
                for (int i = timerDuration; i > 0; i--) {
                    final int count = i;
                    Platform.runLater(() -> {
                        timerLabel.setText("Photo " + currentPhotoNum + " in " + count + "...");
                        try {
                            if (timerPlayer != null) {
                                timerPlayer.stop();
                                timerPlayer.seek(javafx.util.Duration.ZERO);
                                timerPlayer.play();
                            }
                        } catch (Exception e) {
                        }
                    });
                    Thread.sleep(1000);
                }
                Platform.runLater(() -> {
                    timerLabel.setText("Smile!");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            Platform.runLater(() -> {
                try {
                    if (shutterPlayer != null) {
                        shutterPlayer.stop();
                        shutterPlayer.seek(javafx.util.Duration.ZERO);
                        shutterPlayer.play();
                    }
                } catch (Exception e) {
                }

                BufferedImage img = webcam.getImage();
                Photo photo = new Photo(img);
                List<Filter> filters = new ArrayList<>();
                if (currentColorFilter != null) filters.add(currentColorFilter);
                if (currentDistortionFilter != null) filters.add(currentDistortionFilter);
                photo.applyFilters(filters);
                photos.add(photo);

                InvertedFilter invertedFilter = new InvertedFilter(new NoFilter());
                BufferedImage mirroredPreviewImage = invertedFilter.apply(photo.getImage());
                Image fxImage = SwingFXUtils.toFXImage(mirroredPreviewImage, null);
                previewView.setImage(fxImage);

                timerLabel.setText("Photo " + currentPhotoNum + " taken!");
            });
            Thread.sleep(1000);
        }
        Platform.runLater(() -> {
            timerLabel.setText("");
            generateStrip();
            isCapturing = false;
            captureButton.setDisable(false);
        });
    }

    private void generateStrip() {
        Strip strip = new Strip(photos, currentLayout);
        BufferedImage stripImage = strip.generateStrip();

        if (!"transparent".equals(currentFrameColor)) {
            stripImage = applyFrameToStrip(stripImage, currentFrameColor);
        }

        InvertedFilter invertedFilter = new InvertedFilter(new NoFilter());
        BufferedImage mirroredStripImage = invertedFilter.apply(stripImage);
        Image fxImage = SwingFXUtils.toFXImage(mirroredStripImage, null);
        stripView.setImage(fxImage);
        saveButton.setDisable(false);
        saveButton.setVisible(true);

        showAutoPreviewPopup();
    }

    private BufferedImage applyFrameToStrip(BufferedImage stripImage, String frameColor) {
        int frameWidth = 8;
        BufferedImage framedImage = new BufferedImage(
            stripImage.getWidth() + 2 * frameWidth,
            stripImage.getHeight() + 2 * frameWidth,
            BufferedImage.TYPE_INT_RGB
        );

        java.awt.Graphics2D g2d = framedImage.createGraphics();

        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillRect(0, 0, framedImage.getWidth(), framedImage.getHeight());

        g2d.setColor(java.awt.Color.decode(frameColor));
        g2d.fillRect(0, 0, framedImage.getWidth(), framedImage.getHeight());

        g2d.drawImage(stripImage, frameWidth, frameWidth, null);
        g2d.dispose();

        return framedImage;
    }

    @FXML
    private void saveStrip() {
        playButtonSound();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Strip");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Strip strip = new Strip(photos, currentLayout);
                BufferedImage stripImage = strip.generateStrip();
                ImageIO.write(stripImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void toggleMusic() {
        if (bgPlayer != null) {
            if (bgPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                bgPlayer.pause();
            } else {
                bgPlayer.play();
            }
            updateSoundIcon();
        }
    }

    private void updateSoundIcon() {
        if (soundIcon != null) {
            if (bgPlayer != null && bgPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                soundIcon.setImage(new Image("file:pictures/soundoff.png"));
            } else {
                soundIcon.setImage(new Image("file:pictures/soundon.png"));
            }
        }
    }

    @FXML
    private void showPreviewPopup(javafx.scene.input.MouseEvent event) {
        if (stripView.getImage() == null) return;

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Photo Preview");



        VBox popupContainer = new VBox(20);
        popupContainer.setAlignment(Pos.CENTER);
        popupContainer.setPadding(new Insets(30));
        popupContainer.setStyle(
            "-fx-background-image: url('file:pictures/bg-login.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(229, 102, 124, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 20, 0.5, 0, 5);"
        );
        popupContainer.setMaxWidth(800);
        popupContainer.setMaxHeight(700);

        ImageView popupImage = new ImageView(stripView.getImage());
        popupImage.setFitWidth(650);
        popupImage.setFitHeight(500);
        popupImage.setPreserveRatio(true);
        popupImage.setSmooth(true);

        StackPane frameContainer = new StackPane();
        frameContainer.getChildren().add(popupImage);
        frameContainer.setStyle("-fx-border-color: " + currentFrameColor + "; -fx-border-width: 8;");
        frameContainer.setMaxWidth(666);
        frameContainer.setMaxHeight(516);



        Button closeButton = new Button("✕");
        closeButton.setStyle(
            "-fx-background-color: #e5667c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 50%;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 8, 0.4, 0, 2);" +
            "-fx-min-width: 35px;" +
            "-fx-min-height: 35px;" +
            "-fx-max-width: 35px;" +
            "-fx-max-height: 35px;"
        );
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            closeButton.getStyle() +
            "-fx-background-color: #f1778e;" +
            "-fx-scale-x: 1.1;" +
            "-fx-scale-y: 1.1;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.6), 12, 0.5, 0, 3);"
        ));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: #e5667c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 50%;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 8, 0.4, 0, 2);" +
            "-fx-min-width: 35px;" +
            "-fx-min-height: 35px;" +
            "-fx-max-width: 35px;" +
            "-fx-max-height: 35px;"
        ));
        closeButton.setOnAction(e -> popupStage.close());

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_RIGHT);
        headerBox.getChildren().add(closeButton);

        popupContainer.getChildren().addAll(headerBox, frameContainer);

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(popupContainer);
        rootPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        rootPane.setPadding(new Insets(40));

        rootPane.setOnMouseClicked(e -> {
            if (e.getTarget() == rootPane) {
                popupStage.close();
            }
        });

        Scene scene = new Scene(rootPane, primaryStage.getWidth(), primaryStage.getHeight());
        popupStage.setScene(scene);
        popupStage.centerOnScreen();
        popupStage.showAndWait();
    }

    private void showAutoPreviewPopup() {
        if (stripView.getImage() == null) return;

        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Photo Preview");

        ImageView backgroundImage = new ImageView(primaryStage.getScene().snapshot(null));
        backgroundImage.setEffect(new javafx.scene.effect.GaussianBlur(15));
        backgroundImage.setOpacity(0.8);

        VBox popupContainer = new VBox(20);
        popupContainer.setAlignment(Pos.CENTER);
        popupContainer.setPadding(new Insets(30));
        popupContainer.setStyle(
            "-fx-background-image: url('file:pictures/bg-login.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(229, 102, 124, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 20, 0.5, 0, 5);"
        );
        popupContainer.setMaxWidth(800);
        popupContainer.setMaxHeight(700);

        ImageView popupImage = new ImageView(stripView.getImage());
        popupImage.setFitWidth(650);
        popupImage.setFitHeight(500);
        popupImage.setPreserveRatio(true);
        popupImage.setSmooth(true);

        StackPane frameContainer = new StackPane();
        frameContainer.getChildren().add(popupImage);
        frameContainer.setStyle("-fx-border-color: " + currentFrameColor + "; -fx-border-width: 8;");
        frameContainer.setMaxWidth(666);
        frameContainer.setMaxHeight(516);

        Button saveButtonPopup = new Button("Save Image");
        saveButtonPopup.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #a2c26a, #8aac55);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 30;" +
            "-fx-border-color: rgba(255, 255, 255, 0.5);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.4), 10, 0.4, 0, 2);" +
            "-fx-min-width: 150px;"
        );
        saveButtonPopup.setOnMouseEntered(e -> saveButtonPopup.setStyle(
            saveButtonPopup.getStyle() +
            "-fx-background-color: linear-gradient(to bottom right, #8aac55, #a2c26a);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;" +
            "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.6), 15, 0.5, 0, 3);"
        ));
        saveButtonPopup.setOnMouseExited(e -> saveButtonPopup.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #a2c26a, #8aac55);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 30;" +
            "-fx-border-color: rgba(255, 255, 255, 0.5);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(162, 194, 106, 0.4), 10, 0.4, 0, 2);" +
            "-fx-min-width: 150px;"
        ));
        saveButtonPopup.setOnAction(e -> {
            saveStrip();
            popupStage.close();
        });

        Button closeButton = new Button("✕");
        closeButton.setStyle(
            "-fx-background-color: #e5667c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 50%;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 8, 0.4, 0, 2);" +
            "-fx-min-width: 35px;" +
            "-fx-min-height: 35px;" +
            "-fx-max-width: 35px;" +
            "-fx-max-height: 35px;"
        );
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            closeButton.getStyle() +
            "-fx-background-color: #f1778e;" +
            "-fx-scale-x: 1.1;" +
            "-fx-scale-y: 1.1;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.6), 12, 0.5, 0, 3);"
        ));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: #e5667c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 50%;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(229, 102, 124, 0.4), 8, 0.4, 0, 2);" +
            "-fx-min-width: 35px;" +
            "-fx-min-height: 35px;" +
            "-fx-max-width: 35px;" +
            "-fx-max-height: 35px;"
        ));
        closeButton.setOnAction(e -> popupStage.close());

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_RIGHT);
        headerBox.getChildren().add(closeButton);

        popupContainer.getChildren().addAll(headerBox, frameContainer, saveButtonPopup);

        StackPane rootPane = new StackPane();
        rootPane.getChildren().addAll(backgroundImage, popupContainer);
        rootPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        rootPane.setPadding(new Insets(40));

        rootPane.setOnMouseClicked(e -> {
            if (e.getTarget() == rootPane || e.getTarget() == backgroundImage) {
                popupStage.close();
            }
        });

        Scene scene = new Scene(rootPane, primaryStage.getWidth(), primaryStage.getHeight());
        popupStage.setScene(scene);
        popupStage.centerOnScreen();
        popupStage.showAndWait();
    }
}
