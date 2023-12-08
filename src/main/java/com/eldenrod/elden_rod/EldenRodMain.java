package com.eldenrod.elden_rod;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class EldenRodMain extends Application {
    @FXML
    private static ImageView pillar1;
    private static ImageView pillar2;
    private static ImageView pillar3;
    @FXML
    private static Rectangle diedbanner;
    @FXML
    private static Label diedtext;
    private static MediaPlayer mediaPlayer;
    private static Stage stage;
    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        EldenRodMain.scene = scene;
    }

    public static Rectangle getDiedbanner() {
        return diedbanner;
    }

    public static Label getDiedtext() {
        return diedtext;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        EldenRodMain.stage = stage;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        EldenRodMain.mediaPlayer = mediaPlayer;
    }


    @Override
    public void start(Stage sample) throws IOException {

        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(EldenRodMain.class.getResource("menu-page.fxml")));
        Parent root = loader.load();
        scene = new Scene(root);
        Media media = new Media(new File("src\\main\\resources\\music\\01_EldenRing.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        stage= new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.setOpacity(0.7);

        Image icon = new Image("elden-ring.jpg");
        stage.getIcons().add(icon);

        stage.setOnCloseRequest((WindowEvent event)->{

            try {
                GameController.stopThreads();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        stage.setTitle("Elden Rod");
        stage.setScene(scene);
        stage.show();


    }
    public static void main(String[] args) {
        launch();
    }


}


