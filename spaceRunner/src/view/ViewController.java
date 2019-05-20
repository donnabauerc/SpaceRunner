/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.InfoLabel;
import model.Ship;
import model.ShipPicker;
import model.SpaceRunnerButton;
import model.SpaceRunnerSubScene;

/**
 *
 * @author Chris
 */
public class ViewController {
    //4.03 tutorial nr 6
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 708;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    
    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 150;
    
    private SpaceRunnerSubScene shipChooserScene;
    private SpaceRunnerSubScene scoresSubScene;
    private SpaceRunnerSubScene helpSubScene;
    private SpaceRunnerSubScene creditsSubScene;
    
    List<SpaceRunnerButton> menuButtons;
    
    private SpaceRunnerSubScene sceneToHide;
    
    List<ShipPicker> shipsList;
    private Ship choosenShip;
    
    public ViewController() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene); 
        
        createButtons();
        createBackground();
        createLogo();
        createSubScenes();
    }
    
    private void showSubScene(SpaceRunnerSubScene subScene){
        if(sceneToHide != null){
            sceneToHide.moveSubScene();
        }
        
        subScene.moveSubScene();
        sceneToHide = subScene;
    }
    
    public Stage getMainStage(){
        return mainStage;
    }
    
    private void addMenuButton(SpaceRunnerButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size()*100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }
    
    private void createButtons(){
        createStartButton();
        createScoresButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }
    
    private void createStartButton(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("PLAY");
        addMenuButton(startButton);
        
        startButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                showSubScene(shipChooserScene);
            }
            
        });
    }
    
    private void createScoresButton(){
        SpaceRunnerButton scoresButton = new SpaceRunnerButton("SCORES");
        addMenuButton(scoresButton);
        
        scoresButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                showSubScene(scoresSubScene);
            }
            
        });
    }
    
    private void createHelpButton(){
        SpaceRunnerButton helpButton = new SpaceRunnerButton("HELP");
        addMenuButton(helpButton);
        
        helpButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                showSubScene(helpSubScene);
            }
            
        });
    }
    
    private void createCreditsButton(){
        SpaceRunnerButton creditsButton = new SpaceRunnerButton("CREDITS");
        addMenuButton(creditsButton);
        
        creditsButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                showSubScene(creditsSubScene);
            }
            
        });
    }
    
    private void createExitButton(){
        SpaceRunnerButton exitButton = new SpaceRunnerButton("EXIT");
        addMenuButton(exitButton);
        
        exitButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
            
        });
    }
    
    private void createBackground(){
        Image backgroundImage = new Image("view/resources/purple.png", 256, 256,false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, null);
        
        mainPane.setBackground(new Background(background));
    }
   
    private void createLogo(){
        ImageView logo = new ImageView("view/resources/logo.png");
        logo.setLayoutX(700);
        logo.setLayoutY(50);
        
        logo.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(new DropShadow());
            }  
        });
        
        logo.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(null);
            }  
        });
        
        mainPane.getChildren().add(logo);
    }
    
    private void createSubScenes(){
        scoresSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(scoresSubScene);
        
        helpSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(helpSubScene);
        
        creditsSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(creditsSubScene);
        
        createShipChooserSubScene();
    }

    private void createShipChooserSubScene() {
        shipChooserScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(shipChooserScene);
        
        InfoLabel chooseShipLabel = new InfoLabel("Choose Your Ship");
        chooseShipLabel.setLayoutX(110);
        chooseShipLabel.setLayoutY(35);
        shipChooserScene.getPane().getChildren().add(chooseShipLabel);
        shipChooserScene.getPane().getChildren().add(createShipsToChoose());
        
        shipChooserScene.getPane().getChildren().add(createButtonToStart());
    }
    
    private HBox createShipsToChoose(){
        HBox box = new HBox();
        box.setSpacing(20);
        shipsList = new ArrayList<>();
        
        for(Ship ship : Ship.values()){
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            
            shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    for(ShipPicker ship : shipsList){
                        ship.setIsCircleChoosen(false);
                    }
                    shipToPick.setIsCircleChoosen(true);
                    choosenShip = shipToPick.getShip();
                } 
            });
        }

        box.setLayoutX(300-(118*2));
        box.setLayoutY(140);
        
        return box;
    }

    private SpaceRunnerButton createButtonToStart(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("Start");
        startButton.setLayoutX(350);
        startButton.setLayoutY(320);

        startButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(choosenShip != null){
                    GameViewController gameController = new GameViewController();
                    gameController.createNewGame(mainStage, choosenShip);
                }
            }
        });

        return startButton;
    }
    
}
