import oop.ex2.GameGUI;

/**
 * Created by Harel on 12/04/2017.
 */
public class HumanShip extends SpaceShip  {
    /*define variables*/
    GameGUI HumanGui = new GameGUI();
    int isLeftPressed,isRightPressed;
    boolean isUpPressed = false;
    private int dontTilt = 0;


    public void doAction(SpaceWars game) {
        /*check for teleport*/
        if (game.getGUI().isTeleportPressed())
            this.teleport();

        /*check for accelerate and turn*/
        if (game.getGUI().isRightPressed()) {
            isRightPressed = -1;
            this.physics.move(isUpPressed, isRightPressed);
        }
        if (game.getGUI().isLeftPressed()) {
            isLeftPressed = 1;
            this.physics.move(isUpPressed, isLeftPressed);
        }
        if (game.getGUI().isUpPressed()){
            isUpPressed = true;
            this.physics.move(isUpPressed, dontTilt);
        }

        /*Attempt to put shield on*/
        if(game.getGUI().isShieldsPressed()){
            this.shieldOn();
        }

        /*Attempt to fire a shot*/
        if(game.getGUI().isShotPressed()){
            this.fire(game);
        }

        /*charge the ship*/
        this.charge();

        /*reduce freeze-time*/
        if(this.freeze_fire > 0)
            this.freeze_fire -= 1;

        /*update type of ship to human*/
        this.type = "human";
    }
}
