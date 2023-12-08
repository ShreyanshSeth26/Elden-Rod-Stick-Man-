package com.eldenrod.elden_rod;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuPageController implements Initializable {
    @FXML
    private Label hscore;
    @FXML
    private Label lscore;
    @FXML
    private Label runes;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Tarnished tarnishedObject;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream("src\\main\\resources\\com\\eldenrod\\elden_rod\\tarnished.txt"));
            Tarnished.setTarnished((Tarnished) ois.readObject());
            Tarnished obj = Tarnished.getInstance();
            System.out.println(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tarnishedObject=Tarnished.getInstance();
        hscore.setText(String.valueOf(tarnishedObject.getHighestScore()));
        lscore.setText(String.valueOf(tarnishedObject.getLastScore()));
        runes.setText(String.valueOf(tarnishedObject.getRunes()));
        System.out.println(tarnishedObject.getHighestScore());
        System.out.println(tarnishedObject.getLastScore());
        System.out.println(tarnishedObject.getRunes());
    }
    private void playMusic(String filePath, Boolean isInfinite){
        //this function is private as it is a helper function so, no need to be public
        Media media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = EldenRodMain.getMediaPlayer();
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        if(isInfinite) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        EldenRodMain.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }
    @FXML
    private void switchToGame() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        stage = EldenRodMain.getStage();
        scene = new Scene(root);
        stage.setScene(scene);
        EldenRodMain.setScene(scene);
        playMusic("src\\main\\resources\\music\\04_Limgrave.mp3",true);
        tarnishedObject = Tarnished.getInstance();
        tarnishedObject.setScore(0);
        stage.show();
//        runescounter.setText(String.valueOf(tarnishedObject.getRunes()));
    }
    public void switchToGameKey(KeyEvent event) throws IOException {
        switchToGame();
    }
    public void switchToGameClick(ActionEvent event) throws IOException {
        switchToGame();
    }
    public void quit(ActionEvent event){
        System.exit(0);
    }
}
