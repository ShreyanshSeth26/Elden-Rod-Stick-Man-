package com.eldenrod.elden_rod;
//##################Equivalent to stick hero#####################

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.ArrayList;

// Singleton design pattern used in this class

public class Tarnished implements Serializable {
    private static Tarnished tarnished;
    private transient boolean revive=false;
    private ImageView tarnishedImage;
    private Thread tarnishedMove;
    private int score=0;
    private int lastScore=0;
    private int highestScore=0;
    private int Runes;
    private transient int numPillars=0;
    public int getNumPillars() {
        return numPillars;
    }

    public void setNumPillars(int numPillars) {
        this.numPillars = numPillars;
    }

    public boolean isRevive() {
        return revive;
    }

    public void setRevive(boolean revive) {
        this.revive = revive;
    }

    public int getLastScore() {
        return lastScore;
    }

    public static void setTarnished(Tarnished tarnished) {
        Tarnished.tarnished = tarnished;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    private Tarnished() {}
    public static Tarnished getInstance(){
        if(Tarnished.tarnished== null){
            tarnished= new Tarnished();
            return tarnished;
        }
        else{
            return tarnished;
        }
    }
    public void increaseScore(int n){
        score+=n;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
//    public void move(ImageView tarnishedImage, double distance) {
//        this.tarnishedImage=tarnishedImage;
//        double travelled =0;
//        while (travelled<distance){
//            if((distance-travelled)>=10){
//                moveAnimate(10);
//                travelled+=5;
//            }
//            else{
//                moveAnimate(distance-travelled);
//                travelled=distance;
//            }
//        }
//    }
//    private void moveAnimate(double dist){
//        tarnishedMove=new Thread(()->{
//            TranslateTransition tarnishedAnimate = new TranslateTransition();
//            tarnishedAnimate.setNode(tarnishedImage);
//            tarnishedAnimate.setDuration(Duration.millis(30));
//            tarnishedAnimate.setByX(dist);
//            tarnishedAnimate.play();
//            tarnishedAnimate.setOnFinished((event)->{
//                tarnishedImage.setLayoutX(tarnishedImage.getLayoutX()+tarnishedImage.getTranslateX());
//                tarnishedImage.setTranslateX(0);
//            });
//        });
//        tarnishedMove.start();
//        try {
//            tarnishedMove.join();
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public void setRunes(int runes) {
        Runes = runes;
    }
    public int getRunes() {
        return Runes;
    }
    public void extendStick() { /* ... */ }
    public void revive() { /* ... */ }

    @Override
    public String toString() {
        return "Tarnished{" +
                "score=" + score +
                ", lastScore=" + lastScore +
                ", highestScore=" + highestScore +
                ", Runes=" + Runes +
                '}';
    }
}
