package oop.ex6.main;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by daniel hazan on 6/14/2017.
 */
/*this class deal with method block and its attributes*/
public class Method extends Block {
    Matcher matcher;
    ArrayList<String> MethodsParametersArray = new ArrayList<String>();
    String[] params;
    public Method(String Line) throws LineTypeException{
        super(Line);
        LinesReader.MethodsArray.add(this.getName()); //update the MethodsNamesArray
        params = Variables.GetParameters(Line);
    }
    /*Calls the method which located in variables class and check validity of arguments*/
    public boolean CheckValidParameters(String Line){
        try{
            Variables.CheckValidParameters(this,Line);
            return true;
        }
        catch (LineTypeException e ){
            return false;
        }
    }
    public void AddVariable(Variable variable){
        LocalVariablesArray.add(variable);

    }
    /*en moved to variables class*/

    public boolean CheckModifier(String Line)
    {
        matcher = super.ModifierMethod.matcher(Line);
        Pattern LookForDigit = Pattern.compile("\\W");
        Matcher DigitMatcher = LookForDigit.matcher(Line);
        if (!matcher.matches() || DigitMatcher.lookingAt())
            return false;
        return true;
    }

    public String  getName(){
        if (CheckModifier(this.line)){
            return this.line.split("\\s+")[1];

        }return null;

    }
    /*Check brackets at same line*/
    public boolean CheckBrackets (String Line){
        /**check if the current line of Method ends with "){" */
        Pattern pattern = Pattern.compile("\\)\\s*\\{");
        matcher = super.Brackets.matcher(Line);
        Matcher matcher2 = pattern.matcher(Line);
        matcher2.reset();
        while (matcher2.find()) {
            if (matcher2.end() == Line.length() && matcher.matches())//check if the last match is at the end of the line
                return true;
        }return false;
    }


    public String getSubType(){
        return "METHOD";
    }

    /*Return true of false according to validity*/
    public void isValid(Block block) throws LineTypeException{
        if (!CheckModifier(super.line) && !CheckBrackets(super.line)){
            //check parameters
            if (!CheckValidParameters(this.line)){
                throw new LineTypeException("1");
            }
        }
    }
}
