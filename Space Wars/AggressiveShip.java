import java.lang.Math;

    /**
     * Created by Harel on 12/04/2017.
     */
    public class AggressiveShip extends SpaceShip  {
        /*Define variables*/
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
        /*get the closest spaceship to the aggressive*/
        closestShip = game.getClosestShipTo(this);
        /*check the distance from the closest to the aggressive*/
        distanceFrom = this.physics.distanceFrom(closestShip.physics);
        /*check the angle of the aggressive to the closest in order to face it*/
        angleToClosest = this.physics.angleTo(closestShip.physics);

        /*try chasing after the closest ship*/
        if(angleToClosest <= 0)
            this.physics.move(isUpPressed,turnRight);
        else
            this.physics.move(isUpPressed,turnLeft);

        if(angleToClosest < Math.abs(0.21))
            this.fire(game);

        /*reduce freeze-time*/
        if(this.freeze_fire > 0)
            this.freeze_fire -= 1;

        /*make an energy charge*/
            this.charge();
        }
    }
