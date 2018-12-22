package oop.ex6.main;
/**
 * Created by Harel on 20/06/2017.
 */
/*predefined exception class to deal with the exception object that we send through the project*/
public class LineTypeException extends Exception {

        private String ExceptionMsg;

        public LineTypeException(){
            super(); //calls the super class constructor
        }

        public LineTypeException(String msg){
            super(msg);
            ExceptionMsg = msg;
        }

        public String getMessage(){
            return ExceptionMsg;
        }
    }

