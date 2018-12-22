package oop.ex6.main;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statement extends Block {
    Matcher matcher;
    Variables variables;

    public Statement(String Line) {
        super(Line);
    }

    /*Check valid parameters on statement block*/
    public boolean CheckValidParameters(String Line) {
        //check the conditions in the statement
        Pattern conditionPattern = Pattern.compile("\\d*\\.?[^\\D+]*$");//a digit : int or double
        Pattern InitializedVarPattern = Pattern.compile("\\w+$");// initialized variable from global Vars
        String[] Statementonditions = variables.GetStatementParameters(Line);
        ArrayList<Variable> AllLocalAndGlobalVars = LinesReader.GlobalVariablesArray;
        AllLocalAndGlobalVars.addAll(LocalVariablesArray);
        for (String conds : Statementonditions) {
            if (!conds.equals("false") && !conds.equals("true"))

                if (!conditionPattern.matcher(conds).matches()){

                    if (!InitializedVarPattern.matcher(conds).matches()) {

                        for (Variable var : AllLocalAndGlobalVars) {// check if the condition parameter is initialized
                            // variable from global or local variables
                            if (var.getVarName().equals(conds)) {
                                if (!var.getVarType().equals("int|double|boolean"))
                                    return false;
                            }
                        }
                        if (!GetNamesVar(AllLocalAndGlobalVars).contains(conds))
                            return false;
                    }
                }

            }return true;
    }
    /*method that get array list of object and convert to arraylis of name*/
    public ArrayList<String> GetNamesVar(ArrayList<Variable> array) {
        ArrayList<String> arrayList = new ArrayList<String >();
        for (Variable variable: array) {
            arrayList.add(variable.getVarName());
        }return arrayList;
        }

    public void AddVariable(Variable variable){
        LocalVariablesArray.add(variable);
    }
    public boolean CheckModifier(String Line){
        matcher = super.ModifierStatement.matcher(Line);
        return matcher.matches();
    }
    public boolean CheckBrackets (String Line){
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
        return "STATEMENT";
    }
    public void isValid(Block block) throws LineTypeException{
        if (!CheckModifier(super.line) && !CheckBrackets(super.line)) {
            //check parameters
            if (!CheckValidParameters(this.line)) {
                throw new LineTypeException("1");
            }
        }
    }
}

