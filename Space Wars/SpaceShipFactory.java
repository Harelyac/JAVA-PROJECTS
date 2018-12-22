import oop.ex2.*;

public class SpaceShipFactory {
    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] ShipsArray = new SpaceShip[args.length];
        for(int i = 0; i < args.length; i++)
        {
            switch(args[i]){
                case "h":
                    HumanShip h = new HumanShip();
                    ShipsArray[i] = h;
                    break;
                case "b":
                    BasherShip b = new BasherShip();
                    ShipsArray[i] = b;
                    break;
                case "a":
                    AggressiveShip a = new AggressiveShip();
                    ShipsArray[i] = a;
                    break;
                case "d":
                    DrunkardShip d = new DrunkardShip();
                    ShipsArray[i] = d;
                    break;
                case "r":
                    RunnerShip r = new RunnerShip();
                    ShipsArray[i] = r;
                    break;
                case "s":
                    SpecialShip s = new SpecialShip();
                    ShipsArray[i] = s;
                    break;
            }
        }
       return ShipsArray;
    }
}
