package view;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Ship;
import model.SmallInfoLabel;

import java.util.*;


public class GameViewController {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/blue.png";

    private double speed = 2;
    private Timer speedTimer = new Timer();

    private final static String METEOR_BROWN_MEDIUM = "view/resources/meteorBrown_m.png";
    private final static String METEOR_Grey_MEDIUM = "view/resources/meteorGrey_m.png";

    private ImageView[] brownMeteors;
    private ImageView[] greyMeteors;
    Random randomPositionGeneratorX;
    Random randomPositionGeneratorY;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private static int points;
    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 27;
    private final static int METEOR_RADIUS = 20;

    private static ObservableList<Integer> history;

    public GameViewController(){
        initializeStage();
        createKeyListeners();
        randomPositionGeneratorX = new Random();
        randomPositionGeneratorY = new Random();
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT){
                    isLeftKeyPressed = true;
                }else if(event.getCode() == KeyCode.RIGHT){
                    isRightKeyPressed = true;
                }

                if(event.getCode() == KeyCode.UP){
                    isUpKeyPressed = true;
                }else if(event.getCode() == KeyCode.DOWN){
                    isDownKeyPressed = true;
                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT){
                    isLeftKeyPressed = false;
                }else if(event.getCode() == KeyCode.RIGHT){
                    isRightKeyPressed = false;
                }

                if(event.getCode() == KeyCode.UP){
                    isUpKeyPressed = false;
                }else if(event.getCode() == KeyCode.DOWN){
                    isDownKeyPressed = false;
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
        createGameElements(choosenShip);
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
        speedTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                speed += 1;
            }
        },0,5000);

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBackground(speed);
                moveGameElements(speed);
                checkIfElementsAreBehindTheShipAndRelocate();
                checkIfElementsCollide();
                moveShip(speed);
            }
        };

        gameTimer.start();
    }

    private void moveShip(double boost){
        if(isLeftKeyPressed && !isRightKeyPressed){
            if(angle > -30){
                angle -= 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() > -20){
                ship.setLayoutX(ship.getLayoutX()-5);
            }
        }

        if(isRightKeyPressed && !isLeftKeyPressed){
            if(angle < 30){
                angle += 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() < 522){
                ship.setLayoutX(ship.getLayoutX()+5);
            }
        }

        if(isUpKeyPressed && !isDownKeyPressed){
            if(ship.getLayoutY() > 25){
                ship.setLayoutY(ship.getLayoutY()-boost);
            }
        }

        if(isDownKeyPressed && !isUpKeyPressed){
            if(ship.getLayoutY() < 700){
                ship.setLayoutY(ship.getLayoutY()+boost);
            }
        }

        if(isLeftKeyPressed && isRightKeyPressed){
            if(angle < 0){
                angle += 5;
            }else if(angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
        }

        if(!isLeftKeyPressed && !isRightKeyPressed){
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

        gridPane1.setLayoutY(0);
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

    private void createGameElements(Ship choosenShip){
        playerLife = 2;

        star = new ImageView(GOLD_STAR_IMAGE);
        setNewElementPosition(star);
        gamePane.getChildren().add(star);

        pointsLabel = new SmallInfoLabel("POINTS: 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);

        playerLifes = new ImageView[3];
        for(int i = 0;i < playerLifes.length; i++){
            playerLifes[i] = new ImageView(choosenShip.getUrlLife());
            playerLifes[i].setLayoutX(455 + (i*50));
            playerLifes[i].setLayoutY(80);
            gamePane.getChildren().add(playerLifes[i]);
        }

        brownMeteors = new ImageView[3];
        for (int i = 0; i < brownMeteors.length; i++){
            brownMeteors[i] = new ImageView(METEOR_BROWN_MEDIUM);
            setNewElementPosition(brownMeteors[i]);
            gamePane.getChildren().add(brownMeteors[i]);
        }
        greyMeteors = new ImageView[3];
        for (int i = 0; i < greyMeteors.length; i++){
            greyMeteors[i]= new ImageView(METEOR_Grey_MEDIUM);
            setNewElementPosition(greyMeteors[i]);
            gamePane.getChildren().add(greyMeteors[i]);
        }


    }

    private void moveGameElements(double speed){
        star.setLayoutY(star.getLayoutY()+speed);

        for(int i = 0; i < brownMeteors.length; i++){
            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+speed);
            brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }

        for(int i = 0; i < greyMeteors.length; i++){
            greyMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+speed);
            greyMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }
    }

    private void checkIfElementsAreBehindTheShipAndRelocate(){
        if(star.getLayoutY() > 1200){
            setNewElementPosition(star);
        }

        for(int i = 0;i < brownMeteors.length; i++){
            if(brownMeteors[i].getLayoutY() > 850){
                setNewElementPosition(brownMeteors[i]);
            }
        }

        for(int i = 0;i < greyMeteors.length; i++){
            if(greyMeteors[i].getLayoutY() > 900){
                setNewElementPosition(greyMeteors[i]);
            }
        }
    }

    private void setNewElementPosition(ImageView image){
        image.setLayoutX(randomPositionGeneratorX.nextInt(550)+5);
        int yPosition = -600-(randomPositionGeneratorY.nextInt(3200));
        image.setLayoutY(yPosition);
    }

    private void checkIfElementsCollide(){

        if(SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX()+49, star.getLayoutX()+35,
                ship.getLayoutY()+37, star.getLayoutY()+15)){
            setNewElementPosition(star);

            points++;
            String textToSet = "POINTS: ";
            if(points > 10){
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + points);
        }

        checkMeteoritesCollision(brownMeteors);
        checkMeteoritesCollision(greyMeteors);
    }

    private void checkMeteoritesCollision(ImageView[] brownMeteors) {
        for(int i = 0; i < brownMeteors.length; i++){
            if(METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX()+49, brownMeteors[i].getLayoutX()+20,
                    ship.getLayoutY()+37, brownMeteors[i].getLayoutY()+20)){
                removeLife();
                setNewElementPosition(brownMeteors[i]);
            }
        }
    }

    private void removeLife(){
        gamePane.getChildren().remove(playerLifes[playerLife]);
        playerLife--;
        if(playerLife < 0){
            gameStage.close();
            gameTimer.stop();
            speedTimer.cancel();
            menuStage.show();
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) * Math.pow(y1-y2,2));
    }

    public static ObservableList<Integer> getPoints(){
        if(points != 0){
            history.add(points);
            return history;
        }else{
            return null;
        }
    }
}
