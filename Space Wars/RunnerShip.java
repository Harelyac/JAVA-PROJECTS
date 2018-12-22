import java.lang.Math;

/**
 * Created by Harel on 12/04/2017.
 */

/*define variables*/
public class RunnerShip extends SpaceShip {

    boolean isUpPressed = true;
    int noTurn = 0;
    SpaceShip closestShip;
    double distanceFrom;
    double angleToClosest;
    int turnRight = -1;
    int turnLeft = 1;

    public void doAction(SpaceWars game){
        /*accelerate all the time*/
        this.physics.move(isUpPressed, noTurn);
        /*get the closest spaceship to the runner*/
        closestShip = game.getClosestShipTo(this);
        /*check the distance from the closest to the runner*/
        distanceFrom = this.physics.distanceFrom(closestShip.physics);
        /*check the angle of the closest to the runner*/
        angleToClosest = this.physics.angleTo(closestShip.physics);

        /*check case for teleport*/
        if((distanceFrom < Math.abs(0.25)) && (angleToClosest < Math.abs(0.23)))
            this.teleport();

        /*avoid colliding and run away*/
        else{
            if(angleToClosest <= 0)
                this.physics.move(isUpPressed,turnLeft);
            else
                this.physics.move(isUpPressed,turnRight);
        }
        /*make an energy charge*/
        this.charge();
    }
}
