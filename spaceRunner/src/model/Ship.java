/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Chris
 */
public enum Ship {
    BLUE("view/shipchooser/blue_ship.png", "view/shipchooser/playerLife_blue.png"),
    GREEN("view/shipchooser/green_ship.png", "view/shipchooser/playerLife_green.png"),
    ORANGE("view/shipchooser/orange_ship.png", "view/shipchooser/playerLife_orange.png"),
    RED("view/shipchooser/red_ship.png", "view/shipchooser/playerLife_red.png");
    
    private String urlShip;
    private String urlLife;

    private Ship(String urlship, String urlLife){
        this.urlShip = urlship;
        this.urlLife = urlLife;
    }
    
    public String getUrl(){
        return this.urlShip;
    }

    public String getUrlLife() {
        return urlLife;
    }
}
