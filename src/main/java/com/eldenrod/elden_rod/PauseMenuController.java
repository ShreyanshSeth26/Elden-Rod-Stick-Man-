package com.eldenrod.elden_rod;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class PauseMenuController {
    public void resume(ActionEvent event){
        Stage stage=(Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void save(ActionEvent event) throws IOException {
        saveFile();
    }

    static void saveFile() throws IOException {
        System.out.println("Saved");
        Tarnished object = Tarnished.getInstance();
        if(!(object.getScore()==0)) {
            object.setLastScore(object.getScore());
        }
        if (object.getScore() > object.getHighestScore()) {
            object.setHighestScore(object.getScore());
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\com\\eldenrod\\elden_rod\\tarnished.txt"));
        oos.writeObject(object);
    }

    public void quit(ActionEvent event) throws IOException {
        saveFile();
        System.exit(0);
    }
}
