package oop.ex6.main;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
/**
 * Created by daniel hazan on 6/14/2017.
 */
public class Sjavac {
        /*Main method which only run reader and make a try/catch block*/
        public static void main(String[] args) throws Exception{
            String FilePath = args[0];
            LinesReader Reader = new LinesReader(FilePath);
            /*Main reader!*/
            try {
                Reader.ReadLinesIntoList();
                System.out.println("0");
            }
            catch (LineTypeException | IllegalArgumentException e ) {
                System.out.println("1");
            }
            catch (IOException e) {
                System.out.println("2");
            }
            }
    }
