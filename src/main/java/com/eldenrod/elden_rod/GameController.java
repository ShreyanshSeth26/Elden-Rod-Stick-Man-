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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController extends Controller implements Initializable {
    @FXML
    private Label runescounter;
    @FXML
    private ImageView rune;
    @FXML
    private Label scorecounter;
    @FXML
    private ImageView tarnished;
    @FXML
    private ImageView pillar1;
    @FXML
    private ImageView pillar2;
    @FXML
    private ImageView pillar3;
    @FXML
    private ImageView rod;
    @FXML
    private Rectangle diedbanner;
    @FXML
    private Label diedtext;


    private Tarnished tarnishedObject;
    private static Thread rodElongation;
    private static Thread moveAnimation;
    private static Thread runeCollection;
    private static Boolean isKeyPressed=false;
    private static Boolean isMousePressed=false;
    private Boolean hasRuneCollected=false;
    private Stage stage;
    private MediaPlayer mediaPlayer;
    private Scene scene;
    private Parent root;
    private int presentRod=0;
    private boolean isRodFallen=true;
    private boolean extendFlag=true;
    private boolean hitPillar=false;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        ArrayList<Pillar>pillars= new ArrayList<>();
        Pillar pill1=new Pillar(pillar1); Pillar pill2=new Pillar(pillar2); Pillar pill3=new Pillar(pillar3);
        pillars.add(pill1);
        pillars.add(pill2);
        pillars.add(pill3);
        Pillar.setPillars(pillars);
        runescounter.setText(String.valueOf(Tarnished.getInstance().getRunes()));
    }
    private void showBossReal() throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("boss-fight.fxml")));
        stage = EldenRodMain.getStage();
        scene = new Scene(root);
        playMusic("src\\main\\resources\\music\\65-Godfrey.mp3",true);
        stage.setScene(scene);
        EldenRodMain.setScene(scene);
        stage.show();
    }
    public void showBoss(ActionEvent event) throws IOException{
        showBossReal();
    }
    public void startRuneCollection(KeyEvent event) throws IOException{
        isKeyPressed=true;
        if(runeCollection==null){
            runeCollection = new Thread(()->{
                System.out.println(tarnished.getTranslateX());
                RotateTransition rotateIN = new RotateTransition(Duration.millis(50),tarnished);
                TranslateTransition translateIN = new TranslateTransition(Duration.millis(50),tarnished);
                rotateIN.setByAngle(180);
                rotateIN.setAxis(Rotate.X_AXIS);
                translateIN.setByY(tarnished.getFitHeight());
                ParallelTransition circleIN = new ParallelTransition(rotateIN,translateIN);
                circleIN.play();
                while (true) {
                    double xCoord = tarnished.getTranslateX() + tarnished.getLayoutX()+30;
                    if (isKeyPressed) {
                        if ((xCoord < (rune.getLayoutX() + rune.getFitWidth() / 2) && xCoord > (rune.getLayoutX() - rune.getFitWidth() / 2)) && !hasRuneCollected) {
                            System.out.println("Rune collected");
                            hasRuneCollected=true;
                            rune.setOpacity(0);
                            tarnishedObject = Tarnished.getInstance();
                            tarnishedObject.setRunes(tarnishedObject.getRunes() + 30);
                            tarnishedObject.increaseScore(20);
                            Platform.runLater(() -> {
                                scorecounter.setText(String.valueOf(tarnishedObject.getScore()));
                                runescounter.setText(String.valueOf(tarnishedObject.getRunes()));
                            });
                        }
                    } else {
                        return;
                    }
                    if (Pillar.didTarnishedHitPillar(xCoord)) {
                        hitPillar = true;
                        break;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                out();
            });
            runeCollection.start();

        }
    }
    public void stopRuneCollection(KeyEvent event) throws IOException{
        try{
            isKeyPressed=false;
            try{
                runeCollection.join();
                hasRuneCollected=false;
                runeCollection=null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RotateTransition rotateOUT = new RotateTransition(Duration.millis(50),tarnished);
            TranslateTransition translateOUT = new TranslateTransition(Duration.millis(50),tarnished);
            rotateOUT.setByAngle(-180);
            translateOUT.setByY(-tarnished.getFitHeight());
            ParallelTransition circleOUT = new ParallelTransition(rotateOUT,translateOUT);
            circleOUT.play();
        }
        catch (Exception e){
            System.out.println("Error in stopping rune collection");
        }
    }
    public void startRodElongation(MouseEvent event) throws IOException {
        runescounter.setText(String.valueOf(Tarnished.getInstance().getRunes()));
        try {
            if (extendFlag) {
                extendFlag = false;
                isMousePressed = true;
                if (rodElongation == null) {
                    rodElongation = new Thread(() -> {
                        while (true) {
                            if (isMousePressed) {
                                rod.setFitHeight(rod.getFitHeight() + 5.0);
                                rod.setLayoutY(rod.getLayoutY() - 5.0);
                            } else {
                                break;
                            }
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                if (!rodElongation.isAlive()) {
                    rodElongation.start();
                }
            }
        }
        catch (Exception e){
            System.out.println("error in starting rod elongation");
        }
    }
    public void stopRodElongation(MouseEvent event) throws IOException{
        try {
            if (isRodFallen) {
                isRodFallen = false;
                isMousePressed = false;
                try {
                    rodElongation.join();
                    rodElongation = null;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                moveAnimation = new Thread(this::rodTarnishedAnimation);
                moveAnimation.start();
            }
        }
        catch (Exception e){
            System.out.println("error in stopping rod elongation");
        }
    }
    private void rodTarnishedAnimation(){
        extendFlag=false;
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(rod);
        translate.setDuration(Duration.millis(500));
        translate.setByX(rod.getFitHeight() / 2);
        translate.setByY(rod.getFitHeight() / 2);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(rod);
        rotate.setDuration(Duration.millis(500));
        rotate.setByAngle(90);
        isRodFallen=true;
        System.out.printf("(%f,%f)\n",tarnished.getLayoutX(),tarnished.getLayoutY());
        ParallelTransition rodTransition = new ParallelTransition(rotate,translate);
        TranslateTransition tarnishedMove = new TranslateTransition();
        tarnishedMove.setNode(tarnished);
        tarnishedMove.setDuration(Duration.millis(1000));
        tarnishedMove.setByX(rod.getFitHeight()+30);
        SequentialTransition animation = new SequentialTransition(rodTransition,tarnishedMove);
        animation.play();
        animation.setOnFinished(event2->{
            resetRod();
            int outValue=Pillar.isTarnishedOut(tarnished);
            if(outValue == -1){
                out();
            } else if (hitPillar) {
                return;
            } else {
                tarnishedObject=Tarnished.getInstance();
                tarnishedObject.increaseScore(10*(outValue-presentRod));
                scorecounter.setText(String.valueOf(tarnishedObject.getScore()));
                System.out.println(tarnishedObject.getScore());
                presentRod=outValue;
                tarnishedObject.setNumPillars(tarnishedObject.getNumPillars()+1);
                if(tarnishedObject.getNumPillars()==8){
                    Platform.runLater(()->{
                        try {
                            showBossReal();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else if(outValue == 2){
                    movePillars();
                }
            }
            isRodFallen=true;
            extendFlag=true;
        });

    }
    private void movePillars(){
        double moveLength= Pillar.getPillars().get(2).getImage().getLayoutX()-115;
        TranslateTransition transitPillar0= new TranslateTransition(Duration.millis(2000),Pillar.getPillars().get(0).getImage());
        TranslateTransition transitPillar1= new TranslateTransition(Duration.millis(2000),Pillar.getPillars().get(1).getImage());
        TranslateTransition transitPillar2= new TranslateTransition(Duration.millis(2000),Pillar.getPillars().get(2).getImage());
        TranslateTransition transitTarnished= new TranslateTransition(Duration.millis(2000),tarnished);
        TranslateTransition transitRune= new TranslateTransition(Duration.millis(2000),rune);
        transitPillar0.setByX(-moveLength);
        transitPillar1.setByX(-moveLength);
        transitPillar2.setByX(-moveLength);
        transitTarnished.setByX(-moveLength);
        transitRune.setByX(-moveLength);
        ParallelTransition translate = new ParallelTransition(transitPillar0,transitPillar1,transitPillar2,transitTarnished,transitRune);
        translate.setOnFinished(Event -> {
            setTranslateToLayout(tarnished);
            setTranslateToLayout(rune);
            for (Pillar pillar : Pillar.getPillars()){
                setTranslateToLayout(pillar.getImage());
            }
            setPillars();
        });
        translate.play();
        rod.setLayoutX(rod.getLayoutX()-moveLength);
    }
    private void setTranslateToLayout(ImageView object){
        object.setLayoutX(object.getLayoutX()+object.getTranslateX());
        object.setTranslateX(0);
    }
    private void setPillars(){
        //changing the order of pillars in the list
        Pillar temp = Pillar.getPillars().get(2);
        Pillar.getPillars().set(2,Pillar.getPillars().get(1));
        Pillar.getPillars().set(1,Pillar.getPillars().get(0));
        Pillar.getPillars().set(0,temp);
        presentRod=0;

        //setting the opacity 0 to implement fade effect
        Pillar.getPillars().get(1).getImage().setOpacity(0);
        Pillar.getPillars().get(2).getImage().setOpacity(0);

        //setting the new random abscissa
        Random random =new Random();
        double X1 = random.nextInt(51)+425;
        double X2 = random.nextInt(51)+765;
        Pillar.getPillars().get(1).getImage().setLayoutX(X1);
        Pillar.getPillars().get(2).getImage().setLayoutX(X2);


        //setting random width
        double width1 = random.nextInt(131)+50;
        double width2 = random.nextInt(131)+50;
        Pillar.getPillars().get(1).getImage().setFitWidth(width1);
        Pillar.getPillars().get(2).getImage().setFitWidth(width2);

        //setting rune to random place
        double extra=rune.getFitWidth()+5;
        double space0=(X1-extra)-(115+Pillar.getPillars().get(0).getImage().getFitWidth()+extra);
        double space1=(X2-extra)-(X1+(width1)+extra);
        double runeX0=random.nextDouble()*space0+(115+Pillar.getPillars().get(0).getImage().getFitWidth()+extra);
        double runeX1=random.nextDouble()*space1+(X1+(width1)+extra);
        int location=random.nextInt(2);
        if(location==0){
            System.out.println(runeX0);
            rune.setLayoutX(runeX0);
        }
        else {
            System.out.println(runeX1);
            rune.setLayoutX(runeX1);
        }

        //transition
        FadeTransition fadePillar1=new FadeTransition(Duration.millis(1000),Pillar.getPillars().get(1).getImage());
        FadeTransition fadePillar2=new FadeTransition(Duration.millis(1000),Pillar.getPillars().get(2).getImage());
        FadeTransition fadeRune=new FadeTransition(Duration.millis(1000),rune);
        fadePillar1.setFromValue(0);
        fadePillar2.setFromValue(0);
        fadeRune.setFromValue(0);
        fadePillar1.setToValue(1);
        fadePillar2.setToValue(1);
        fadeRune.setToValue(1);
        ParallelTransition fade=new ParallelTransition(fadePillar1,fadePillar2,fadeRune);
        fade.play();

        //setting the final opacity
        fade.setOnFinished(Event->{
            Pillar.getPillars().get(1).getImage().setOpacity(1);
            Pillar.getPillars().get(2).getImage().setOpacity(1);
            rune.setOpacity(1);
        });
    }
    private void resetRod(){
        tarnished.setLayoutX(tarnished.getLayoutX()+tarnished.getTranslateX());
        tarnished.setTranslateX(0);
        rod.setLayoutX(rod.getLayoutX()+rod.getFitHeight()+30);
        rod.setLayoutY(rod.getLayoutY()+rod.getFitHeight()-0.01);
        rod.setTranslateX(0);
        rod.setTranslateY(0);
        System.out.println(rod.getLayoutY());
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(rod);
        rotate.setDuration(Duration.millis(50));
        rotate.setByAngle(-90);
        rotate.play();
        rod.setFitHeight(0.01);
        extendFlag=true;
        System.out.printf("(%f,%f)\n",tarnished.getLayoutX(),tarnished.getLayoutY());
    }
    private void out(){
        System.out.println("Out");
        FadeTransition textFade=new FadeTransition(Duration.millis(1000),diedtext);
        FadeTransition bannerFade=new FadeTransition(Duration.millis(1000),diedbanner);
        textFade.setFromValue(0);
        textFade.setToValue(1);
        bannerFade.setFromValue(0);
        bannerFade.setFromValue(0.6);
        ParallelTransition fade=new ParallelTransition(textFade,bannerFade);
        SequentialTransition outTransit;
            TranslateTransition tarnishedDrop = new TranslateTransition();
            tarnishedDrop.setNode(tarnished);
            tarnishedDrop.setDuration(Duration.millis(500));
            tarnishedDrop.setByY(500);
            outTransit = new SequentialTransition(tarnishedDrop, fade);
            outTransit.play();
            tarnishedDrop.setOnFinished(event -> {
                playMusic("src\\main\\resources\\music\\YouDiedSE.mp3",false);
//                scorecounter.setText("0");
            });
        outTransit.setOnFinished((event)-> {
            try {
                save();
                if(moveAnimation!=null && moveAnimation.isAlive()){
                    isKeyPressed=false;
                    moveAnimation.join();
                }

                Thread.sleep(2000);
                tarnishedObject=Tarnished.getInstance();
                tarnishedObject.setLastScore(tarnishedObject.getScore());
                Platform.runLater(()->{
                    try {
                        diedbanner.setOpacity(0);
                        diedtext.setOpacity(0);
                        showReviveMenu();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void reviveTarnished(){
        tarnished.setTranslateY(0);
        tarnished.setTranslateX(0);
        tarnished.setLayoutX(Pillar.getPillars().get(presentRod).getImage().getLayoutX()+(Pillar.getPillars().get(presentRod).getImage().getFitWidth()/2));
        rod.setLayoutX(tarnished.getLayoutX()+10);
        runescounter.setText(String.valueOf(Tarnished.getInstance().getRunes()));
        playMusic("src\\main\\resources\\music\\04_Limgrave.mp3",true);
    }
    public static void stopThreads() throws InterruptedException {
        if(rodElongation!=null && rodElongation.isAlive()){
            isMousePressed=false;
            rodElongation.join();
        }
        if(runeCollection!=null && runeCollection.isAlive()){
            isMousePressed=false;
            runeCollection.join();
        }
        if(moveAnimation!=null && moveAnimation.isAlive()){
            isKeyPressed=false;
            moveAnimation.join();
        }
    }
}