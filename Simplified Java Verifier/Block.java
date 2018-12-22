package oop.ex6.main;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by daniel hazan on 6/14/2017.
 */
/*This class is a superclass that export some great methods*/
public abstract class Block extends LineType{

    public Pattern ModifierMethod;
    public Pattern ModifierStatement;
    public Pattern Brackets;
    public ArrayList<Variable> LocalVariablesArray;
    public String ALLTYPESOFVARIABLES = "int|double|String|boolean|char";
    String line;
    public Block PreviousBlock;
    public Block(){
        super();
    }

    public void setPreviousBlock(Block previousBlock) {
        PreviousBlock = previousBlock;
    }

    public Block getPreviousBlock() {
        return PreviousBlock;
    }

    public ArrayList<Variable> getLocalVariablesArray() {
        return LocalVariablesArray;
    }

    public void ConcatenateVariables(ArrayList<Variable> Variables){
        if(LocalVariablesArray != null)
            LocalVariablesArray.addAll(Variables);
    }

    public abstract void AddVariable(Variable variable);

    public Block(String Line){
        line = Line;
        ModifierStatement = Pattern.compile("if\\s*\\({1}[^(]|while\\s*\\({1}[^(]");//matches only modifiers that starts with letters(non digit) with "(" once only
        ModifierMethod = Pattern.compile("void\\s[^0-9 |^\\( | ^s]\\w*\\s*\\({1}[^(]+");//matches only modifiers that starts with void + whitespase
        // +letters(non digit) with "(" once only
        Brackets = Pattern.compile("^[^\\)]*\\)\\s*\\{$");
    }
    public abstract boolean CheckValidParameters( String Line);
    public abstract boolean CheckModifier(String Line);
    public abstract boolean CheckBrackets(String Line);
    public String getType(){
        return "BLOCK";
    }
    public abstract String getSubType();
    public abstract void isValid(Block CurrentBlock) throws LineTypeException;
}
