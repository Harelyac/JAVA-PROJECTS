import java.lang.Math;

/**
 * Created by Harel on 12/04/2017.
 */
public class BasherShip extends SpaceShip  {
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
        /*get the closest spaceship to the basher*/
        closestShip = game.getClosestShipTo(this);
        /*check the distance from the closest to the basher*/
        distanceFrom = this.physics.distanceFrom(closestShip.physics);
        /*check the angle of the basher to the closest in order to face it*/
        angleToClosest = this.physics.angleTo(closestShip.physics);

        /*try colliding deliberately*/
        if(angleToClosest <= 0)
            this.physics.move(isUpPressed,turnRight);
        else
            this.physics.move(isUpPressed,turnLeft);

        /*activate shield when its too close*/
        if(distanceFrom <= Math.abs(0.19)) {
            this.shieldOn();
        }
        /*make an energy charge*/
        this.charge();
    }
}
