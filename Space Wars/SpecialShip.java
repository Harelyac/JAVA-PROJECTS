/**
 * Created by Harel on 12/04/2017.
 */
public class SpecialShip extends SpaceShip {
    public void doAction(SpaceWars game) {
        SpaceShip closestShip;
        boolean isUpPressed = true;
        double angleToClosest;
        int turnRight = -1;
        int turnLeft = 1;
        int noTurn = 0;

        closestShip = game.getClosestShipTo(this);
        angleToClosest = this.physics.angleTo(closestShip.physics);

        if(closestShip.type.equals("human")) {

            if (angleToClosest <= 0)
                this.physics.move(isUpPressed, turnRight);
            else
                this.physics.move(isUpPressed, turnLeft);

            if (angleToClosest <= Math.abs(0.21)) {
                this.fire(game);
                if (this.freeze_fire > 0)
                    this.freeze_fire -= 1;
            }
        }


        /*go find a human ship!*/
        else{
            this.physics.move(isUpPressed,noTurn);
        }

        /*This ship will never die*/
        if(this.currentEnergyLevel < 50)
            this.currentEnergyLevel = this.MAX_ENERGY_LEVEL;
        if(this.currentHealthLevel < 5 )
            this.currentHealthLevel = this.MAX_HEALTH_LEVEL;

        /*make an energy charge anyway*/
        this.charge();

    }
}
