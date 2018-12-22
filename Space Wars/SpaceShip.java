import java.awt.Image;
import oop.ex2.*;

/**
 * The API spaceships need to implement for the SpaceWars game. 
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 *  a base class for the other spaceships or any other option you will choose.
 *  
 * @author oop
 */
public abstract class SpaceShip{

    /*Constants definition*/
    public static final int MAX_ENERGY_LEVEL = 250;
    public static final int BASE_ENERGY_LEVEL = 200;
    public static final int MAX_HEALTH_LEVEL = 25;
    public static final int MIN_HEALTH_LEVEL = 0;
    public static final int FREEZE_MAX = 7;
    public static final int FREEZE_MIN = 0;

    /*Variables definition*/
    public int maximalEnergyLevel = MAX_ENERGY_LEVEL;
    public int currentEnergyLevel = BASE_ENERGY_LEVEL;
    public int currentHealthLevel = MAX_HEALTH_LEVEL;
    protected SpaceShipPhysics physics = new SpaceShipPhysics();
    public boolean shield = false;
    public double ratio;
    public int freeze_fire = FREEZE_MIN;
    public String type = "enemy";
    public Image img;



    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * 
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {

    }

    /*Charge the ship energy ratio each round its been invoked*/
    public void charge(){
       ratio = physics.getVelocity() / physics.getMaxVelocity();
       /*Check ratio and charge accordingly*/
       if(ratio < 0.5)
           this.currentEnergyLevel += 1;
       else if(ratio >= 0.5)
           this.currentEnergyLevel += 2;
       else if(ratio == 1){
           this.currentEnergyLevel += 3;
       }

       /*Check if energy passed the max amount*/
       if(this.currentEnergyLevel > this.maximalEnergyLevel){
           this.currentEnergyLevel = MAX_ENERGY_LEVEL;
       }

    }

    /**
     * This method is called every time a collision with this ship occurs 
     */
    public void collidedWithAnotherShip(){
        if(this.currentEnergyLevel >= 15){
            if(this.shield) {
                this.maximalEnergyLevel += 20;
                this.currentEnergyLevel -= 15;
            }
        }
        else if(this.currentEnergyLevel >= 10){
            this.maximalEnergyLevel -= 5;
            this.currentEnergyLevel -= 10;
        }
            this.currentHealthLevel--;
    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
        this.currentEnergyLevel = BASE_ENERGY_LEVEL;
        this.maximalEnergyLevel = MAX_ENERGY_LEVEL;
        this.currentHealthLevel = MAX_HEALTH_LEVEL;
        this.freeze_fire = FREEZE_MIN;
        this.physics = new SpaceShipPhysics();
    }

    /**
     * Checks if this ship is dead.
     * 
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
        if(this.currentHealthLevel == MIN_HEALTH_LEVEL){
            return true;
        }
        else
            return false;
    }

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return this.physics;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets shot at (with or without a shield).
     */
    public void gotShot(){
        if(!this.shield)
        this.currentHealthLevel -= 1;
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * 
     * @return the image of this ship.
     */
    public Image getImage(){
        /*Check for different types of ships for specific image*/
        if(this.type.equals("human"))
            if(shield)
                img = GameGUI.SPACESHIP_IMAGE_SHIELD;
            else
                img = GameGUI.SPACESHIP_IMAGE;
        else{
            if(shield)
                img = GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
            else
                img = GameGUI.ENEMY_SPACESHIP_IMAGE;
        }
        return img;
    }

    /**
     * Attempts to fire a shot.
     * 
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
        if(this.freeze_fire == FREEZE_MIN) {
            game.addShot(physics);
            this.currentEnergyLevel -= 15;
            this.freeze_fire = FREEZE_MAX;
        }
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
        this.currentEnergyLevel -= 3;
        this.shield = true;
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
        if(this.currentEnergyLevel >= 100)
            this.currentEnergyLevel -= 100;
        else
            this.currentHealthLevel -= 100;
        this.physics = new SpaceShipPhysics();
    }
}
