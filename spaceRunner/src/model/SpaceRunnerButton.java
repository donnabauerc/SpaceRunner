/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 *
 * @author Chris
 */
public class SpaceRunnerButton extends Button{
    private final String FONT_PATH = "src/model/ressources/kenvector_future.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/ressources/yellow_button_pressed.png')";
    private final String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/ressources/yellow_button.png')";
    
    public SpaceRunnerButton(String txt){
        setText(txt);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(45);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListeners();
    }
    
    public void setButtonFont(){
        try { 
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException ex) {
            setFont(Font.font("Verdana", 23)); 
        }
    }
    
    public void setButtonPressedStyle(){
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(41);
        setLayoutY(getLayoutY() + 4);
    }
    
    public void setButtonReleasedStyle(){
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() - 4);
    }
    
    public void initializeButtonListeners(){
        setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                     setButtonPressedStyle();
                }
            }
        
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                     setButtonReleasedStyle();
                }
            }
    
        });
        
        setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());
            }
        });
        
        setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }
}
