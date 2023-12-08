package com.eldenrod.elden_rod;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

// Composite design patter is used in this class

public class Pillar {

    private ImageView pillar;
    private static ArrayList<Pillar> pillars;
    public Pillar(ImageView pillar) {
        this.pillar = pillar;
    }
    public ImageView getImage() {
        return pillar;
    }

    public void setImage(ImageView pillar) {
        this.pillar = pillar;
    }

    public static ArrayList<Pillar> getPillars() {
        return Pillar.pillars;
    }

    public static void setPillars(ArrayList<Pillar> pillars) {
        Pillar.pillars = pillars;
    }

    public static int isTarnishedOut(ImageView tarnished){
        double tarnishedCoord= tarnished.getLayoutX()+30;
        if(tarnishedCoord>=(pillars.get(0).getImage().getLayoutX())&&tarnishedCoord<=(pillars.get(0).getImage().getLayoutX()+pillars.get(0).getImage().getFitWidth())){
            return 0;
        }
        else if(tarnishedCoord>=(pillars.get(1).getImage().getLayoutX())&&tarnishedCoord<=(pillars.get(1).getImage().getLayoutX()+pillars.get(1).getImage().getFitWidth())){
            return 1;
        }
        else if(tarnishedCoord>=(pillars.get(2).getImage().getLayoutX())&&tarnishedCoord<=(pillars.get(2).getImage().getLayoutX()+pillars.get(2).getImage().getFitWidth())){
            return 2;
        }
        else{
            return -1;
        }
    }
    public static boolean didTarnishedHitPillar(double tarnishedCoord){
        if((tarnishedCoord >= (pillars.get(0).getImage().getLayoutX()) && tarnishedCoord <= (pillars.get(0).getImage().getLayoutX() + pillars.get(0).getImage().getFitWidth()) || tarnishedCoord >= (pillars.get(1).getImage().getLayoutX()) && tarnishedCoord <= (pillars.get(1).getImage().getLayoutX() + pillars.get(1).getImage().getFitWidth())) || tarnishedCoord >= (pillars.get(2).getImage().getLayoutX()) && tarnishedCoord <= (pillars.get(2).getImage().getLayoutX() + pillars.get(2).getImage().getFitWidth())){
            return true;
        }
        else {
            return false;
        }
    }
    // Additional methods as needed

}
