package filesprocessing;

/**
 * Created by Harel on 03/06/2017.
 */
public class SubSectionNamesException extends Exception {
    private String ExceptionMsg;

    public SubSectionNamesException(){
        super(); //calls the super class constructor
    }

    public SubSectionNamesException(String msg){
        super(msg);
        ExceptionMsg = msg;
    }

    public String getMessage(){
        return ExceptionMsg;
    }
}
