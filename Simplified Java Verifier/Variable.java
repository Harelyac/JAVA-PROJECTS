package oop.ex6.main;
/**
 * Created by Harel on 25/06/2017.
 */
/*Class that deal with making a variable object to put into all the arraylist of the project*/
public class Variable {
        private String VarName;
        private String VarValue;
        private String VarType;
        private boolean IsInitialized;
        private Block VariableBlock;

    public Variable(String name, String value, String type, boolean IsInitialized, Block block){
        this.VarName = name;
        this.VarType = type;
        this.VarValue = value;
        this.IsInitialized = IsInitialized;
        this.VariableBlock = block;
    }

    public String getVarValue() {
        return VarValue;
    }

    public void setVarValue(String varValue) {
        VarValue = varValue;
    }

    public String getVarName() {
        return VarName;
    }

    public boolean isInitialized() {
        return IsInitialized;
    }

    public void setInitialized(boolean initialized) {
        IsInitialized = initialized;
    }

    public String getVarType() {
        return VarType;
    }

    public void setVarType(String varType) {
        VarType = varType;
    }

    public void setVarName(String varName) {
        VarName = varName;
    }
}
