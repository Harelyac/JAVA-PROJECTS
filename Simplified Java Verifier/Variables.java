package oop.ex6.main;
import sun.nio.cs.ext.ISCII91;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Created by daniel hazan on 6/14/2017.
 */
public class Variables extends LineType {
    /*Patterns for value checking!*/
    private static Pattern NamePattern = Pattern.compile("\\D+|[_]+\\w+");
    private Pattern StringValuePattern = Pattern.compile("^\"[^0-9]+\"$");
    private Pattern CharValuePattern = Pattern.compile("^\'[^0-9]\'$");
    private Pattern DoubleValuePattern = Pattern.compile("^[-]?[0-9]+\\.?[0-9]*$");
    private Pattern IntValuePattern = Pattern.compile("^[-]?[0-9]+$");
    private Pattern BooleanValuePattern = Pattern.compile("^(true|false)$");
    private static Pattern TypePattern = Pattern.compile("(int|boolean|String|char|double)");
    private Pattern isInitialized = Pattern.compile("(\\w+)\\s[=]\\s(\\\"?\\'?\\w+\\.?\\w*\\\"?\\'?)");

    /*Parsed data from line*/
    private static String Type;
    private String [] VariablesArray;

    /*info on specific variable*/
    private String VarType;
    private String VarName;
    private boolean IsDeclared;
    private boolean IsInitialized;
    private  String line;

    private static String[] ParametersNames;
    private static String[] ParametersStatementNames;


    public Variables(String Line){
        this.line = Line;
    }

    public void isValid(Block Block) throws LineTypeException {
        /*do trim before all!*/
        line = line.trim();
        if (this.line.charAt(line.length() - 1) == ';') {
            VariablesArray = GetVariablesFromLine(this.line);
            /*Checks if the names are good enough*/
            if (!CheckVariablesNames(VariablesArray)) {
                throw new LineTypeException("1");
            }
            Type = GetTypeFromLine(this.line);
            if(Type.equals("final")){
                String newVariablesString = String.join(",",VariablesArray);
                VariablesArray = GetVariablesFromLine(newVariablesString);
                CheckFinalVariables(VariablesArray); //checks whether final variables are all initialized!
                Type = GetTypeFromLine(newVariablesString);
            }
            String[] value;

            /*Check each type individually*/
            switch (Type) {
                case "String":
                    for (String var : VariablesArray) {
                        if ((value = isInitialized(var)) != null) {
                            Matcher match = StringValuePattern.matcher(value[1]);
                            if (!match.matches())
                                throw new LineTypeException("1");
                            Variable variable = new Variable(var,value[1],Type,true, Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                        else{
                            Variable variable = new Variable(var,null,Type,false, Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                    }
                    break;
                case "double":
                    for (String var : VariablesArray) {
                        if ((value = isInitialized(var)) != null) {
                            Matcher match = DoubleValuePattern.matcher(value[1]);
                            if (!match.matches())
                                throw new LineTypeException("1");
                            Variable variable = new Variable(var,value[1],Type,true, Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                        else {
                            Variable variable = new Variable(var, null, Type, false, Block);
                            if (Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                    }
                    break;
                case "char":
                    for (String var : VariablesArray) {
                        if ((value = isInitialized(var)) != null) {
                            Matcher match = CharValuePattern.matcher(value[1]);
                            if (!match.matches())
                                throw new LineTypeException("1");
                            Variable variable = new Variable(var,value[1],Type,true, Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                        else {
                            Variable variable = new Variable(var, null, Type, false, Block);
                            if (Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                    }
                    break;
                case "int":
                    for (String var : VariablesArray) {
                        if ((value = isInitialized(var)) != null) {
                            Matcher match = IntValuePattern.matcher(value[1]);
                            if (!match.matches())
                                throw new LineTypeException("1");
                            Variable variable = new Variable(var,value[1],Type,true, Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                        else {
                            Variable variable = new Variable(var, null, Type, false, Block);
                            if (Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                    }
                    break;
                case "boolean":
                    for (String var : VariablesArray) {
                        if ((value = isInitialized(var)) != null) {
                            Matcher match = DoubleValuePattern.matcher(value[1]);
                            Matcher match1 = BooleanValuePattern.matcher(value[1]);
                            Matcher match2 = IntValuePattern.matcher(value[1]);
                            if (!match.matches() && !match1.matches() && !match2.matches())
                                throw new LineTypeException("1");
                            Variable variable = new Variable(var,value[1],Type,true , Block);
                            if(Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                        else {
                            Variable variable = new Variable(var, null, Type, false, Block);
                            if (Block == null)
                                LinesReader.GlobalVariablesArray.add(variable);
                            else
                                Block.AddVariable(variable);
                        }
                    }
                    break;
                default:
                    throw new LineTypeException("1");
            }

        }
        else
            throw new LineTypeException("1");
    }

    public void CheckFinalVariables(String[] variablesArray) throws LineTypeException{
        for(String var:variablesArray){
            if(isInitialized(var) == null)
                throw new LineTypeException();
        }
    }

    /*Checks if the current variables is trying being initialized and return the value itself*/
    public String[] isInitialized(String variable){
        Matcher match = isInitialized.matcher(variable);
        if(match.matches()){
            return new String[]{match.group(1), match.group(2)};
        }
        else
            return null;
    }

    public boolean CheckVariablesNames(String[] VariablesArray){
        for(String var : VariablesArray){
            String[] value;
            if((value = isInitialized(var))!= null) {
                Matcher match = NamePattern.matcher(value[0]);
                if (!match.matches())
                    return false;
            }
        }
        if(VariablesArray.length > 1){
            if (VariablesArray[1].equals("final"))
                return false;
        }
            return true;
    }

    public static String[] GetVariablesFromLine(String line){
        int startIndex;
        if(GetTypeFromLine(line).equals("final")){
            startIndex = line.indexOf(" "); // starting point of variables
            line = line.substring(startIndex, line.length()).trim(); // exclude the commas at the end
        }
        else {
            startIndex = line.indexOf(" "); // starting point of variables
            line = line.substring(startIndex, line.length() - 1).trim(); // exclude the commas at the end
        }
        return line.split(",");
    }

    public static String GetTypeFromLine(String line){
        int endIndex = line.indexOf(" ");
        String type = line.substring(0,endIndex).trim();
        return type;
    }

    public String getType(){
        return "VARIABLES";
    }


    public static void CheckType(ArrayList<String> types) throws LineTypeException {
        for (String type : types) {
            Matcher matcher = TypePattern.matcher(type);
            if (!matcher.matches())
                break;
        }
        throw new LineTypeException("1");
    }
    public static void CheckName(ArrayList<String> names)throws LineTypeException{
        for (String type : names) {
            Matcher matcher = NamePattern.matcher(type);
            if (!matcher.matches())
                break;
        }
        throw new LineTypeException("1");
    }

    public static void CheckValidParameters(Method method,String line) throws LineTypeException {
        String[] ParameterArray;
        ArrayList<String> ParametersType = new ArrayList<String>();
        ArrayList<String> ParametersName = new ArrayList<String>();
        ArrayList<String> FinalsArray = new ArrayList<String>();
        ArrayList<String> FinalsTypeArray = new ArrayList<String>();
        ArrayList<String> FinalsNameArray = new ArrayList<String>();
        ParameterArray = line.split(",\\s*");
        Pattern Divider = Pattern.compile("(\\w+)\\s(\\w+)\\s(\\w+)");

        for (String parameter : ParameterArray) {
            Matcher matcher = Divider.matcher(parameter);
            if (matcher.groupCount() == 2) {
                ParametersType.add(matcher.group(1));
                ParametersName.add(matcher.group(2));
            } else if(matcher.groupCount() == 3) /*this means we encounter a final variable*/
                {
                FinalsTypeArray.add(matcher.group(2));
                FinalsNameArray.add(matcher.group(3));
            }
            else
                throw new LineTypeException();

            /*Now we checks the data we've parsed*/
            /*Checks sizes*/
            if (ParametersType.size() != ParametersName.size())
                throw new LineTypeException("1");

            /*Throws exception if the types are wrong*/
            CheckType(ParametersType);
            CheckType(FinalsTypeArray);
            /*Throws exception if the names are wrong*/
            CheckName(ParametersName);
            CheckName(FinalsNameArray);
            /*If we got this far it means that all is good and we can
            proceed to creating the variables*/
            for(int i = 0; i < ParametersName.size() - 1; i++){
                Variable variable = new Variable(ParametersName.get(i),ParametersType.get(i),Type,false, method);
                method.AddVariable(variable);
            }
        }
    }

    /*Methods parameters*/
    public static String[] GetParameters(String Line){
        int startIndex = Line.indexOf("(");
        int endIndex = Line.indexOf(")");
        if(startIndex + 1 == endIndex)
            return null;
        else {
            Line = Line.substring(startIndex, endIndex);
            return ParametersNames = Line.split(",");
        }
    }

    /*Statement parameters*/
    public static String[] GetStatementParameters(String Line){
        int startIndex = Line.indexOf("(");
        int endIndex = Line.indexOf(")");
        Line = Line.substring(startIndex,endIndex);
        return ParametersStatementNames = Line.split("\\|{2}|\\&{2}");
    }


}