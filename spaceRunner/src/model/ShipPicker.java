/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 *
 * @author Chris
 */

public class ShipPicker extends VBox{
    private ImageView circleImage;
    private ImageView shipImage;
    
    private String circleNotChoosen = "view/shipchooser/circle.png";
    private String circleChoosen = "view/shipchooser/yellow_circle.png";
    
    private Ship ship;
    
    private boolean isCircleChoosen;
    
    public ShipPicker(Ship ship){
        circleImage = new ImageView(circleNotChoosen);
        shipImage = new ImageView(ship.getUrl());
        this.ship = ship;
        isCircleChoosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
    }
    
    public Ship getShip(){
        return ship;
    }
    
    public boolean getIsCircleChoosen(){
        return isCircleChoosen;
    }
    
    public void setIsCircleChoosen(boolean isCircleChoosen){
        this.isCircleChoosen = isCircleChoosen;
        String imageToSet = this.isCircleChoosen ? circleChoosen : circleNotChoosen;
        circleImage.setImage(new Image(imageToSet));
    }
}
