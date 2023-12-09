package com.eldenrod.elden_rod;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class BossFightController extends Controller implements Initializable {
    @FXML
    private Label victoryBG;
    @FXML
    private Label victory;
    @FXML
    private Rectangle diedbanner;
    @FXML
    private Label diedtext;
    @FXML
    private ProgressBar bosshealth;
    @FXML
    private ProgressBar healthbar;
    @FXML
    private ProgressBar staminabar;
    @FXML
    private ImageView godfrey;
    @FXML
    private ImageView tarnished;
    @FXML
    private ImageView sheild;
    @FXML
    private ImageView sword;
    @FXML
    private ImageView axe;
    @FXML
    private Label scorecounter;    
    @FXML
    private Label runescounter;

    private Tarnished tarnishedObject = Tarnished.getInstance();
    private Thread swordThread;
    private Thread increaseStamina;
    private Thread godfreyAttack;
    private Boolean isSheildOn=false;
    private Boolean hasAttacked=false;
    private Boolean fightOn=true;
    private Boolean godfreyDissapear=false;
    private MediaPlayer mediaPlayer;
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scorecounter.setText(String.valueOf(tarnishedObject.getScore()));
        runescounter.setText(String.valueOf(tarnishedObject.getRunes()));
        axe.setOpacity(0);
        sword.setOpacity(0);
        sheild.setOpacity(0);
        startAI();
    }

    private void startAI(){
        increaseStamina=new Thread(()->{
            while (fightOn){
                if(staminabar.getProgress()<1){
                    staminabar.setProgress(staminabar.getProgress()+0.1);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        godfreyAttack = new Thread(()->{
            try {
                int sleepTime;
                Random random= new Random();
                Thread.sleep(3000);
                while (fightOn){
                    godfreyDissapear=true;
                    godfrey.setOpacity(0);
                    sleepTime= (random.nextInt(3))*1000;
                    Thread.sleep(sleepTime);
                    godfrey.setOpacity(1);
                    godfreyDissapear=false;
                    godfreyAxeAttack();
                    sleepTime= (random.nextInt(3)+1)*1000;
                    Thread.sleep(sleepTime);
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        increaseStamina.start();
        godfreyAttack.start();
    }
    public void mousePressedHandler (MouseEvent event) throws IOException, InterruptedException {
        if(event.getButton() == MouseButton.PRIMARY){
            if((!hasAttacked)&&(!isSheildOn)&&fightOn) {
                if (swordThread == null) {
                    swordThread = new Thread(() -> {
                        sword.setOpacity(1);
                        TranslateTransition attack = new TranslateTransition(Duration.millis(200), sword);
                        attack.setByX(540);
                        double progress=0;
                        if(staminabar.getProgress()<=0.2){
                            sword.setOpacity(0);
                            sword.setTranslateX(0);
                            swordThread = null;
                            hasAttacked = false;
                            return;
                        }
                        else if(staminabar.getProgress()<0.4){
                            progress=staminabar.getProgress();
                        }
                        else{
                            progress=0.4;
                        }
                        staminabar.setProgress(staminabar.getProgress()-progress);
                        attack.play();
                        attack.setOnFinished((actionEvent) -> {
                            if(!godfreyDissapear) {
                                bosshealth.setProgress(bosshealth.getProgress() - 0.05);
                            }
                            sword.setOpacity(0);
                            sword.setTranslateX(0);
                            swordThread = null;
                            hasAttacked = false;
                            if(bosshealth.getProgress()<=0){
                                fightOn=false;
                                enemyFelled();
                            }
                        });
                    });
                }
                if (!swordThread.isAlive()) {
                    hasAttacked = true;
                    swordThread.start();
                }
            }
        }
        else if(event.getButton() == MouseButton.SECONDARY){
            isSheildOn=true;
            sheild.setOpacity(1);
        }
    }
    public void mouseReleasedHandler (MouseEvent event) throws IOException{
        if(event.getButton() == MouseButton.SECONDARY){
            isSheildOn=false;
            sheild.setOpacity(0);
        }
    }
    public void godfreyAxeAttack(){
        axe.setOpacity(1);
        TranslateTransition axeMove= new TranslateTransition(Duration.millis(500),axe);
        axeMove.setByX(-480);
        RotateTransition axeRotate=new RotateTransition(Duration.millis(100),axe);
        axeRotate.setInterpolator(Interpolator.LINEAR);
        axeRotate.setCycleCount(5);
        axeRotate.setByAngle(-360);
        ParallelTransition axeAttack = new ParallelTransition(axeMove,axeRotate);
        axeAttack.play();
        axeAttack.setOnFinished((actionEvent)->{
            if(!isSheildOn){
                if(healthbar.getProgress()!=0) {
                    healthbar.setProgress(healthbar.getProgress() - 0.25);
                    if (healthbar.getProgress()==0) {fightOn=false;out();}
                }
            }
            axe.setOpacity(0);
            axe.setTranslateX(0);
        });
    }
    private void out(){
        FadeTransition textFade = new FadeTransition(Duration.millis(1800),diedtext);
        FadeTransition bannerFade = new FadeTransition(Duration.millis(1000),diedbanner);
        FadeTransition tarnishedFade = new FadeTransition(Duration.millis(300),tarnished);
        tarnishedFade.setFromValue(1);
        tarnishedFade.setToValue(0);
        textFade.setFromValue(0);
        textFade.setToValue(1);
        bannerFade.setFromValue(0);
        bannerFade.setToValue(0.7);
        ParallelTransition died= new ParallelTransition(textFade,bannerFade);
        SequentialTransition outTransit= new SequentialTransition(tarnishedFade,died);
        playMusic("src\\main\\resources\\music\\YouDiedSE.mp3",false);
        outTransit.play();
        outTransit.setOnFinished((actionEvent)->{
            tarnished.setOpacity(0);
            diedbanner.setOpacity(0.7);
            diedtext.setOpacity(1);
            try {
                increaseStamina.join();
                godfreyAttack.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            increaseStamina=null;
            godfreyAttack=null;
            try {
                save();
                Thread.sleep(1500);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            tarnishedObject=Tarnished.getInstance();
            tarnishedObject.setLastScore(tarnishedObject.getScore());
            Platform.runLater(()-> {
                try {
                    showReviveMenu();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    private void enemyFelled(){
        tarnishedObject.setRunes(tarnishedObject.getRunes()+1000);
        runescounter.setText(String.valueOf(tarnishedObject.getRunes()));
        FadeTransition textFade = new FadeTransition(Duration.millis(1800),victory);
        FadeTransition textFade2 = new FadeTransition(Duration.millis(1800),victoryBG);
        FadeTransition bannerFade = new FadeTransition(Duration.millis(1000),diedbanner);
        FadeTransition godfreyFade = new FadeTransition(Duration.millis(300),godfrey);
        godfreyFade.setFromValue(1);
        godfreyFade.setToValue(0);
        textFade.setFromValue(0);
        textFade.setToValue(1);
        textFade2.setFromValue(0);
        textFade2.setToValue(0.4);
        bannerFade.setFromValue(0);
        bannerFade.setToValue(0.7);
        ParallelTransition died= new ParallelTransition(textFade,bannerFade,textFade2);
        SequentialTransition outTransit= new SequentialTransition(godfreyFade,died);
        playMusic("src\\main\\resources\\music\\EnemyFelledSE.mp3",false);
        outTransit.play();
        outTransit.setOnFinished((actionEvent)->{
            godfrey.setOpacity(0);
            diedbanner.setOpacity(0.7);
            victory.setOpacity(1);
            victoryBG.setOpacity(0.4);
            try {
                increaseStamina.join();
                godfreyAttack.join();
                increaseStamina=null;
                godfreyAttack=null;
                save();
                Thread.sleep(1500);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()-> {
                try {
                    switchToGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
    private void switchToGame() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game2.fxml")));
        stage = EldenRodMain.getStage();
        scene = new Scene(root);
        stage.setScene(scene);
        EldenRodMain.setScene(scene);
        playMusic("src\\main\\resources\\music\\03_Mountains.mp3",true);
        stage.show();
    }
    public void reviveTarnished(){
        tarnished.setOpacity(1);
        healthbar.setProgress(1);
        diedbanner.setOpacity(0);
        diedtext.setOpacity(0);
        fightOn=true;
        playMusic("src\\main\\resources\\music\\65-Godfrey.mp3",true);
        startAI();
    }
}


