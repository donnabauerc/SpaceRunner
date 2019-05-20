package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ship;


public class GameViewController {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRigthKeyPressed;
    private int angle;
    private AnimationTimer gameTimer;


    public GameViewController(){
        initializeStage();
        createKeyListeners();
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT){
                    isLeftKeyPressed = true;
                }else if(event.getCode() == KeyCode.RIGHT){
                    isRigthKeyPressed = true;
                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT){
                    isLeftKeyPressed = false;
                }else if(event.getCode() == KeyCode.RIGHT){
                    isRigthKeyPressed = false;
                }
            }
        });
    }

    private void initializeStage(){
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    public void createNewGame(Stage menuStage, Ship choosenShip){
        this.menuStage = menuStage;
        this.menuStage.hide();
        createShip(choosenShip);
        createGameLoop();
        gameStage.show();
    }

    private void createShip(Ship choosenShip){
        ship = new ImageView(choosenShip.getUrl());
        ship.setLayoutX(GAME_WIDTH/2 - 37);
        ship.setLayoutY(GAME_HEIGHT-90);
        gamePane.getChildren().add(ship);
    }

    private void createGameLoop(){
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveShip();
            }
        };
        gameTimer.start();
    }

    private void moveShip(){
        if(isLeftKeyPressed && !isRigthKeyPressed){
            if(angle > -30){
                angle -= 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() > -20){
                ship.setLayoutX(ship.getLayoutX()-5);
            }
        }

        if(isRigthKeyPressed && !isLeftKeyPressed){
            if(angle < 30){
                angle += 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() < 522){
                ship.setLayoutX(ship.getLayoutX()+5);
            }
        }

        if(isLeftKeyPressed && isRigthKeyPressed){
            if(angle < 0){
                angle += 5;
            }else if(angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
        }

        if(!isLeftKeyPressed && !isRigthKeyPressed){
            if(angle < 0){
                angle += 5;
            }else if(angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
        }
    }
}
