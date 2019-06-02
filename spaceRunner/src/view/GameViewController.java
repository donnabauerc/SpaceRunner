package view;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
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
    private boolean isSpacePressed;

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
    Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private ArrayList<ImageView> playerLives;
    private int playerLife;
    private static int points;
    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 30;
    private final static int ENEMY_RADIUS = 40;
    private final static int METEOR_RADIUS = 20;

    private static ObservableList<Integer> history;

    private static final String LASER_IMAGE = "view/resources/laser.png";
    private static ImageView laser = new ImageView(LASER_IMAGE);;
    private static boolean laser_ammo = true;
    private double laser_trigger_position;

    private ImageView enemy;
    private static final String[] ENEMIES_SRC = {"view/resources/enemyBlack.png","view/resources/enemyBlue.png","view/resources/enemyGreen.png", "view/resources/enemyRed.png"};
    private boolean moveLeft = true;

    private static Ship chosenShip;

    public GameViewController(){
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    private void initializeStage(){
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
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

                if(event.getCode() == KeyCode.SPACE){
                    isSpacePressed = true;
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

                if(event.getCode() == KeyCode.SPACE){
                    isSpacePressed = false;
                }
            }
        });
    }

    public void createNewGame(Stage menuStage, Ship chosenShip){
        this.menuStage = menuStage;
        this.chosenShip = chosenShip;
        this.menuStage.hide();
        createBackground();
        createShip(chosenShip);
        createEnemy();
        createGameElements(chosenShip);
        createGameLoop();
        gameStage.show();
    }

    private void createShip(Ship chosenShip){
        ship = new ImageView(chosenShip.getUrl());

        //cause the ships are too bright with a black background
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.2);
        ship.setEffect(colorAdjust);

        ship.setLayoutX(GAME_WIDTH/2 - 37);
        ship.setLayoutY(GAME_HEIGHT-90);
        gamePane.getChildren().add(ship);
    }

    private void createEnemy(){
        enemy = new ImageView(ENEMIES_SRC[randomPositionGenerator.nextInt(4)]);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.2);
        enemy.setEffect(colorAdjust);

        enemy.setLayoutX(GAME_WIDTH/2-51);
        enemy.setLayoutY(-2*GAME_HEIGHT);
        gamePane.getChildren().add(enemy);
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
                moveEnemy(speed);
                reload();
                shoot(speed);
            }
        };

        gameTimer.start();
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

    private void createGameElements(Ship chosenShip){
        playerLife = 2;

        star = new ImageView(GOLD_STAR_IMAGE);
        setNewElementPosition(star);
        gamePane.getChildren().add(star);

        pointsLabel = new SmallInfoLabel("POINTS: 00");
        pointsLabel.setLayoutX(430);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);

        playerLives = new ArrayList<>();
        for(int i = 0;i < 3; i++){
            playerLives.add(new ImageView(chosenShip.getUrlLife()));
            playerLives.get(i).setLayoutX(445 + (i*50));
            playerLives.get(i).setLayoutY(80);
            gamePane.getChildren().add(playerLives.get(i));
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

    private void moveGameElements(double speed){
        star.setLayoutY(star.getLayoutY()+speed);
        laser.setLayoutY(laser.getLayoutY()-4*speed);

        for(int i = 0; i < brownMeteors.length; i++){
            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+speed);
            brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }

        for(int i = 0; i < greyMeteors.length; i++){
            greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+speed);
            greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
        }
    }

    private void moveShip(double boost){
        if(isLeftKeyPressed && !isRightKeyPressed){
            if(angle > -30){
                angle -= 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() > 5){
                ship.setLayoutX(ship.getLayoutX()-5);
            }
        }

        if(isRightKeyPressed && !isLeftKeyPressed){
            if(angle < 30){
                angle += 5;
            }
            ship.setRotate(angle);

            if(ship.getLayoutX() < 485){
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

    private void moveEnemy(double speed) {
        enemy.setLayoutY(enemy.getLayoutY()+speed/2);

        if(moveLeft && enemy.getLayoutX() >= 15){
            enemy.setLayoutX(enemy.getLayoutX()-speed);
            enemy.setRotate(30);
        }else{
            moveLeft = false;
        }
        if(!moveLeft && enemy.getLayoutX() < GAME_WIDTH-108){
            enemy.setLayoutX(enemy.getLayoutX()+speed);
            enemy.setRotate(-30);
        }else{
            moveLeft = true;
        }
    }

    private void checkIfElementsAreBehindTheShipAndRelocate(){
        if(star.getLayoutY() > 1200){
            setNewElementPosition(star);
        }

        if(enemy.getLayoutY() > 1000){
            gamePane.getChildren().remove(enemy);
            createEnemy();
        }

        for(int i = 0;i < brownMeteors.length; i++){
            if(brownMeteors[i].getLayoutY() > 900){
                setNewElementPosition(brownMeteors[i]);
            }
        }

        for(int i = 0;i < greyMeteors.length; i++){
            if(greyMeteors[i].getLayoutY() > 900){
                setNewElementPosition(greyMeteors[i]);
            }
        }
    }

    private void checkIfElementsCollide(){
        if(SHIP_RADIUS + STAR_RADIUS >= calculateDistance(ship.getLayoutX()+49, star.getLayoutX()+15,
                ship.getLayoutY()+37, star.getLayoutY()+15)){
            setNewElementPosition(star);
            score(10);
        }

        if(ENEMY_RADIUS + SHIP_RADIUS >= calculateDistance(ship.getLayoutX()+49, enemy.getLayoutX()+50,
                ship.getLayoutY()+37,enemy.getLayoutY()+42)){
            removeLife();
            removeLife();
            gamePane.getChildren().remove(enemy);
            createEnemy();
        }

        checkMeteoritesCollision(brownMeteors, false);
        checkMeteoritesCollision(greyMeteors, true);

        checkIfShotMeteor(brownMeteors,false);
        checkIfShotMeteor(greyMeteors,true);

        checkIfShotEnemy();
    }

    private void checkIfShotEnemy() {
        if(ENEMY_RADIUS + 25 >= calculateDistance(laser.getLayoutX()+4, enemy.getLayoutX()+50,
                laser.getLayoutY()+27, enemy.getLayoutY()+42)){
            gamePane.getChildren().remove(enemy);
            createEnemy();

            getLife();
            score(30);
        }
    }

    private void checkIfShotMeteor(ImageView[] Meteors, boolean grey){
        for(int i = 0; i < Meteors.length;i++){
            if(METEOR_RADIUS + 25 >= calculateDistance(laser.getLayoutX()+4, Meteors[i].getLayoutX()+22,
                    ship.getLayoutY()+27, Meteors[i].getLayoutY()+20) && Meteors[i].getLayoutY() > 0){
                if(grey){
                    setNewElementPosition(greyMeteors[i]);
                }else{
                    setNewElementPosition(brownMeteors[i]);
                }
                score(5);
            }
        }
    }

    private void checkMeteoritesCollision(ImageView[] Meteors, boolean grey) {
        for(int i = 0; i < Meteors.length; i++){
            if(METEOR_RADIUS + SHIP_RADIUS >= calculateDistance(ship.getLayoutX()+49, Meteors[i].getLayoutX()+20,
                    ship.getLayoutY()+37, Meteors[i].getLayoutY()+20)){
                removeLife();
                if(grey){
                    setNewElementPosition(greyMeteors[i]);
                }else{
                    setNewElementPosition(brownMeteors[i]);
                }
            }
        }
    }

    private void setNewElementPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(500)+50);
        image.setLayoutY(-(randomPositionGenerator.nextInt(1200)+600));
    }

    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }

    private void reload(){
        if(laser.getLayoutY() <= -(55+(800-laser_trigger_position))){
            gamePane.getChildren().remove(laser);
            laser_ammo = true;
        }
    }

    private void shoot(double v){
        if(isSpacePressed && laser_ammo){
            laser_ammo = false;
            laser_trigger_position = ship.getLayoutY()-v;
            laser.setLayoutY(ship.getLayoutY()-v);
            laser.setLayoutX(ship.getLayoutX()+56);
            gamePane.getChildren().add(laser);
        }
    }

    private void removeLife(){
        gamePane.getChildren().remove(playerLives.remove(playerLife));
        playerLife--;
        if(playerLife < 0){
            gameStage.close();
            gameTimer.stop();
            speedTimer.cancel();
            menuStage.show();
        }
    }

    private void getLife(){
        if(playerLife < 2 ){
            playerLife++;
            playerLives.add(new ImageView(chosenShip.getUrlLife()));
            playerLives.get(playerLife).setLayoutX(445 + (playerLife*50));
            playerLives.get(playerLife).setLayoutY(80);
            gamePane.getChildren().add(playerLives.get(playerLife));
        }
    }

    private void score(int amount){
        points = points+amount;
        String textToSet = "POINTS: ";
        if(points < 10){
            textToSet = textToSet + "0";
        }
        pointsLabel.setText(textToSet + points);
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
