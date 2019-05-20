package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Ship;

import java.util.Timer;
import java.util.TimerTask;


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

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/black.png";

    private double speed = 1;

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
        createBackground();
        createShip(choosenShip);
        createGameLoop();
        gameStage.show();
    }

    private void createShip(Ship choosenShip){
        ship = new ImageView(choosenShip.getUrl());

        //cause the ships are too bright with a black background
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.2);
        ship.setEffect(colorAdjust);

        ship.setLayoutX(GAME_WIDTH/2 - 37);
        ship.setLayoutY(GAME_HEIGHT-90);
        gamePane.getChildren().add(ship);
    }

    private void createGameLoop(){
        Timer speedTimer = new Timer();

        speedTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                speed += 0.8;
            }
        },0,5000);

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBackground(speed);
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

    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for(int i=0;i<12;i++){
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);

            GridPane.setConstraints(backgroundImage1, i% 3, i/3);
            GridPane.setConstraints(backgroundImage2, i% 3, i/3);

            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }

        gridPane2.setLayoutY(-1024);

        gamePane.getChildren().addAll(gridPane1, gridPane2);
    }

    private void moveBackground(double speed){
        gridPane1.setLayoutY(gridPane1.getLayoutY() + speed);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + speed);

        if(gridPane1.getLayoutY() >= 1024){
            gridPane1.setLayoutY(-1024);
        }

        if(gridPane2.getLayoutY() >= 1024){
            gridPane2.setLayoutY(-1024);
        }
    }
}
