package com.eldenrod.elden_rod;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class Controller {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private MediaPlayer mediaPlayer;
    public void switchToMenuPage() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-page.fxml")));
        stage = EldenRodMain.getStage();
        scene = new Scene(root);
        playMusic("src\\main\\resources\\music\\01_EldenRing.mp3",true);
        stage.setScene(scene);
        EldenRodMain.setScene(scene);
        stage.show();
    }
    public void playMusic(String filePath, Boolean isInfinite){
        //this function is private as it is a helper function so, no need to be public
        Media media = new Media(new File(filePath).toURI().toString());
        mediaPlayer=EldenRodMain.getMediaPlayer();
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        if(isInfinite) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        EldenRodMain.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }
    public void showReviveMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("revive-menu.fxml"));
        Parent root = loader.load();
        //creating a new stage for the pause menu
        Stage rmStage=new Stage();
        rmStage.setTitle("Revive Menu");
        rmStage.initModality(Modality.APPLICATION_MODAL);
        rmStage.initOwner(EldenRodMain.getScene().getWindow());
        rmStage.initStyle(StageStyle.TRANSPARENT);
        rmStage.setOpacity(0.95);

        Scene rmScene = new Scene(root);
        rmStage.setScene(rmScene);
        rmStage.showAndWait();
        if(!Tarnished.getInstance().isRevive()){
            switchToMenuPage();
        }
        else{
            reviveTarnished();
        }
    }
    public void showPauseMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pause-menu.fxml"));
        Parent root = loader.load();

        //creating a new stage for the pause menu
        Stage pmStage=new Stage();
        pmStage.setTitle("Pause Menu");
        pmStage.initModality(Modality.APPLICATION_MODAL);
        pmStage.initOwner(EldenRodMain.getScene().getWindow());
        pmStage.initStyle(StageStyle.TRANSPARENT);
        pmStage.setOpacity(0.95);

        Scene pmScene = new Scene(root);
        pmStage.setScene(pmScene);
        pmStage.showAndWait();
    }
    public void save() throws IOException {
        PauseMenuController.saveFile();
    }
    public abstract void reviveTarnished();
}
