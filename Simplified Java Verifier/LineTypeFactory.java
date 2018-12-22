package oop.ex6.main;
import com.sun.org.apache.xpath.internal.operations.Variable;
import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daniel hazan on 6/14/2017.
 */
public class LineTypeFactory {
    Pattern MethodPattern;
    Pattern VariablePattern;
    Pattern StatementPattern;
    Pattern OtherPattern;
    String line;
    Matcher Matcher;
    LineType mytype;

    /*this class checks the pattern with some predefined pattern and make an object out of it*/
    public LineTypeFactory(String Line) throws LineTypeException {
        MethodPattern = Pattern.compile("^void");
        VariablePattern = Pattern.compile("(final\\s*)?(int|double|char|String|boolean\\s*)");
        StatementPattern = Pattern.compile("if\\s*\\({1}[^(]|while\\s*\\({1}[^(]");
        OtherPattern = Pattern.compile("^\\t?\\s?return;$|\\w+\\(\\);$|^\\s*$|//};|^\\\\\\w+$");
        line = Line;
        mytype = CheckLineType(line);
    }
    public LineType CheckLineType(String Line) throws LineTypeException{
        if (checkIfMethod(this.MethodPattern, Line)){
            return new Method(Line);
        }if (checkIfVariable(this.VariablePattern, Line)){
            return new Variables(Line);
        }if (checkIfStatement(this.StatementPattern, Line)){
            return new Statement(Line);
        }if (checkIfOther(this.OtherPattern,Line)){
            return new Other(Line);
        }else{
            throw new LineTypeException("1");// create accurate exception
        }

    }public LineType getLineTypeObj(){
        return this.mytype;
    }

    public boolean checkIfMethod(Pattern pattern , String Line){
        this.Matcher = pattern.matcher(Line);
        return this.Matcher.lookingAt();

    }
    public boolean checkIfVariable(Pattern pattern, String Line){
        this.Matcher = pattern.matcher(Line);
        return this.Matcher.lookingAt();

    }
    public boolean checkIfStatement(Pattern pattern , String Line){
        this.Matcher = pattern.matcher(Line);
        return this.Matcher.matches();

    }
    public boolean checkIfOther(Pattern pattern, String Line){
        this.Matcher = OtherPattern.matcher(line);
        return this.Matcher.matches();
    }
}
