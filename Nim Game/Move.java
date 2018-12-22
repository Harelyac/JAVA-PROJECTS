import org.omg.PortableInterceptor.INACTIVE;

/**
 * Created by Harel on 17/03/2017.
 */
public class Move {

    private int rowNum, leftNum, rightNum;

    /*Constructs a Move object with the given parameters*/
    public Move(int inRow, int inLeft, int inRight){
        rowNum = inRow;
        leftNum = inLeft;
        rightNum = inRight;
    }

    public int getLeftBound(){
        return this.leftNum;
    }

    public int getRightBound(){
        return this.rightNum;
    }

    public int getRow(){
        return this.rowNum;
    }

    public String toString(){
        return this.rowNum + ":" + this.leftNum + "-" + this.rightNum;
    }
}
