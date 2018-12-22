package oop.ex6.main;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Other extends LineType {

    String line;
    Pattern MethodName = Pattern.compile("\\w+[^\\( |^\\s*]");
    Matcher matcher;
    Variables variables;
    public Other(String Line) {
        line = Line;
        this.matcher = MethodName.matcher(Line);



    }

    public void isValid(Block block) throws LineTypeException {
        if (this.getType().equals("METHOD_CALL")) {
            String[] Statementonditions = variables.GetStatementParameters(this.line);///////////////////////////////////
            ArrayList<Variable> AllLocalAndGlobalVars = LinesReader.GlobalVariablesArray;
            AllLocalAndGlobalVars.addAll(((Block) block).LocalVariablesArray);
            //for (String conds : Statementonditions){
            for (int i = 0; i <= Statementonditions.length; i++) {

                if (!GetNamesVar(AllLocalAndGlobalVars).contains(Statementonditions[i]))// check if the  parameter is initialized
                    // variable from global or local variables
                    throw new LineTypeException("1");
                if (!((Method) block).params[i].equals(Statementonditions[i]))// check the order of the parameters
                    throw new LineTypeException("1");
                if (!LinesReader.MethodsArray.contains(getNameMethod(this.line)))
                    throw new LineTypeException("1");

            }
            if (this.getType().equals("COMMENT") || this.getType().equals("CLOSED_BLOCK") || this.getType().equals("WHITESPACE")) {
                return;// if checking the type was correct so dont throw any exception and return!!
            }

        }
    }
    public ArrayList<String> GetNamesVar(ArrayList<Variable> array) {
            ArrayList<String> arrayList = new ArrayList<String >();


            for (Variable variable: array) {
                arrayList.add(variable.getVarName());
            }return arrayList;
        }
    public String getNameMethod(String Line){
        /**returns the method name which is invoked*/


        if (matcher.find())
            return matcher.group(0);
        return null;
    }
    public String getType() throws LineTypeException{
    //switch case according to pattern!!
        switch (line){
            case "\\s*$":
                return "WHITESPACE";
            case "^return;$":
                return "RETURN";
            case "\\t?\\};$":
                return "CLOSED_BLOCK";
            case "^\\\\\\w+$":
                return "COMMENT";
            case "\\t?\\w+\\((\\s*\\w+\\,)*(\\s*\\w+)\\)$":
                return "METHOD_CALL";
        }throw new LineTypeException("1");
    }

}
