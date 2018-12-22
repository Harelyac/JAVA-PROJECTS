package oop.ex6.main;
import java.util.ArrayList;

/**
 * Created by daniel hazan on 6/14/2017.
 */
/*This is the main super class where Block,Variables,Statement and other play role as subclasses*/
public abstract class LineType {
   public abstract void isValid(Block block) throws LineTypeException;
   public abstract String getType() throws LineTypeException;
}
