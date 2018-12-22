import java.util.Random;
/**
 * Created by Harel on 12/04/2017.
 */
public class DrunkardShip extends SpaceShip {
    /*this class will include all 3 other types - runner/basher/aggressive*/
    boolean isUpPressed = true;
    int noTurn = 0;
    SpaceShip closestShip;
    double distanceFrom;
    double angleToClosest;
    int turnRight = -1;
    int turnLeft = 1;
    Random rand = new Random();
    int action;
    public static final int MIN_RAND_COOLDOWN = 0;
    public static final int MAX_RAND_COOLDOWN = 100;
    int randCoolDown = MAX_RAND_COOLDOWN;

    public void doAction(SpaceWars game) {
        /*accelerate all the time*/
        this.physics.move(isUpPressed, noTurn);
        /*get the closest spaceship to the Drunkard*/
        closestShip = game.getClosestShipTo(this);
        /*check the distance from the closest to the Drunkard*/
        distanceFrom = this.physics.distanceFrom(closestShip.physics);
        /*check the angle of the Drunkard to the closest in order to face it*/
        angleToClosest = this.physics.angleTo(closestShip.physics);

        /*Fire just like that because of hangover*/
        this.fire(game);

        /*reduce freeze-time*/
        if(this.freeze_fire > 0)
            this.freeze_fire -= 1;

        /*Decrease cooldown*/
        if(randCoolDown > 0)
            randCoolDown -= 1;

        /*Check if we can random*/
        if(randCoolDown == MIN_RAND_COOLDOWN)
            /*Randomize a certain move*/
            action = rand.nextInt(3);

        switch (action) {
            case 1:
            /*try colliding deliberately*/
                if (angleToClosest <= 0)
                    this.physics.move(isUpPressed, turnRight);
                else
                    this.physics.move(isUpPressed, turnLeft);

                if (angleToClosest < Math.abs(0.21))
                    this.fire(game);
                break;

            case 2:
                /*check case for teleport*/
                if ((distanceFrom < Math.abs(0.25)) && (angleToClosest < Math.abs(0.25)))
                    this.teleport();

                /*avoid colliding and run away*/
                else {
                    if (angleToClosest <= 0)
                        this.physics.move(isUpPressed, turnLeft);
                    else
                        this.physics.move(isUpPressed, turnRight);
                }
                break;

            case 3:
                /*try colliding deliberately*/
                if (angleToClosest <= 0)
                    this.physics.move(isUpPressed, turnRight);
                else
                    this.physics.move(isUpPressed, turnLeft);

                /*activate shield when its too close*/
                if (distanceFrom <= Math.abs(0.19)) {
                    this.shieldOn();
                }
                break;
        }
         /*make an energy charge*/
        this.charge();
    }
}
