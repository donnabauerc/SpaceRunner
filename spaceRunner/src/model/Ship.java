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
    BLUE("view/shipchooser/blue_ship.png"),
    GREEN("view/shipchooser/green_ship.png"),
    ORANGE("view/shipchooser/orange_ship.png"),
    RED("view/shipchooser/red_ship.png");
    
    private String urlShip;
    
    private Ship(String urlship){
        this.urlShip = urlship;
    }
    
    public String getUrl(){
        return this.urlShip;
    }
}
