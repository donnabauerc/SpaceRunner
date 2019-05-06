/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.SpaceRunnerButton;

/**
 *
 * @author Chris
 */
public class ViewController {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewController() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene); 
        createButtons();
    }
    
    public Stage getMainStage(){
        return mainStage;
    }
    
    private void createButtons(){
        SpaceRunnerButton button = new SpaceRunnerButton("Click me!");
        mainPane.getChildren().add(button);
    }
    
}
