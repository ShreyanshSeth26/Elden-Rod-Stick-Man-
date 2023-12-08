package com.eldenrod.elden_rod;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviveMenuController implements Initializable {

    @FXML
    private Label runescounter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runescounter.setText(String.valueOf(Tarnished.getInstance().getRunes()));
    }
    public void continueToMenuPage(ActionEvent event){
        Stage stage=(Stage)((Button)event.getSource()).getScene().getWindow();
        Tarnished.getInstance().setRevive(false);
        stage.close();
    }

    public void revive(ActionEvent event){
        Tarnished tarnished=Tarnished.getInstance();
        if(tarnished.getRunes()>=250){
            Stage stage=(Stage)((Button)event.getSource()).getScene().getWindow();
            tarnished.setRevive(true);
            tarnished.setRunes(tarnished.getRunes()-250);
            stage.close();
        }

    }
}
