/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;

/**
 *
 * @author Chris
 */
public class SpaceRunnerSubScene extends SubScene{
    
    private final static String FONT_PATH = "src/model/ressources/kenvector_future.ttf";
    private final static String BACKGROUND_IMAGE = "model/ressources/yellow_panel.png";

    
    public SpaceRunnerSubScene() {
        super(new AnchorPane(), 600, 400);
        prefWidth(600);
        prefHeight(400);
        
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE,600,400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        
        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));
    }
    
}
