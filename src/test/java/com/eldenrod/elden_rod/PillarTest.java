package com.eldenrod.elden_rod;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PillarTest {

    @Test
    void isTarnishedOut() {
        ImageView tarnished = new ImageView();
        ImageView pillar1 = new ImageView();
        Pillar temp1 = new Pillar(pillar1);
        ImageView pillar2 = new ImageView();
        Pillar temp2= new Pillar(pillar2);
        ImageView pillar3 = new ImageView();
        Pillar temp3 = new Pillar(pillar3);
        ArrayList <Pillar> pillars=new ArrayList<>(3);
        pillars.add(temp1);
        pillars.add(temp2);
        pillars.add(temp3);
        Pillar.setPillars(pillars);
        assertEquals(-1,Pillar.isTarnishedOut(tarnished));
    }

    @Test
    void didTarnishedHitPillar(){
        ImageView tarnished = new ImageView();
        ImageView pillar1 = new ImageView();
        Pillar temp1 = new Pillar(pillar1);
        ImageView pillar2 = new ImageView();
        Pillar temp2= new Pillar(pillar2);
        ImageView pillar3 = new ImageView();
        Pillar temp3 = new Pillar(pillar3);
        ArrayList <Pillar> pillars=new ArrayList<>(3);
        pillars.add(temp1);
        pillars.add(temp2);
        pillars.add(temp3);
        Pillar.setPillars(pillars);
        assertFalse(Pillar.didTarnishedHitPillar(20));
    }
}