package oop.ex6.main;
/**
 * Created by daniel hazan on 6/14/2017.
 */
import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LinesReader {
    private FileReader fr;
    private BufferedReader br;
    //private BlockReader blockReader;
    private LineTypeFactory TypeFactory;
    /*ArrayList contains all global variables*/
    public static ArrayList<Variable> GlobalVariablesArray = new ArrayList<Variable>();
    /*Array list with names of methods*/
    public static ArrayList<String> MethodsArray = new ArrayList<String>();
    /*Array of blocks*/

    /*Variables*/
    private LineNumberReader lineNum;
    private String file_path;
    private String CurrentLine;
    private boolean IsInsideAblock = true;

    Pattern OpenBlock;
    Pattern ClosedBlock;
    Matcher matcher;
    Matcher matcher2;
    LineType CurrentBlock = null; /*The main block*/

    public LineTypeFactory CurrentFactory;
    public LineType CurrentObj;


    public LinesReader(String path) throws Exception {
        file_path = path;
        OpenBlock = Pattern.compile("\\{");
        ClosedBlock = Pattern.compile("\\};");
    }

    /*This is the main method of the project, where we read each and every line given by,
    * the file test and check validity*/
    public void ReadLinesIntoList() throws LineTypeException, IOException {
        //which contains a the Object lines (i.e objects of the interface LineType
        fr = new FileReader(file_path);
        br = new BufferedReader(fr);
        lineNum = new LineNumberReader(br);
        CurrentLine = br.readLine();
        while (CurrentLine != null) {
            CurrentFactory = new LineTypeFactory(CurrentLine);
            CurrentObj = CurrentFactory.getLineTypeObj();
            /*Checks if its not a block*/
            if (!CurrentObj.getType().equals("BLOCK")) {
                if ((CurrentObj.getType().equals("VARIABLES")) && CurrentBlock == null)
                    CurrentObj.isValid((Block) CurrentBlock);
                    CurrentLine = br.readLine();
            } else {
                CurrentBlock = CurrentObj;
                while (IsInsideAblock) {
                    br.mark(getCurrentLineInFile());//to implement this method!!!!!
                    int NumOfInnerBlocks = getNumOfInnerBlocks(br);
                    br.reset();
                    CurrentLine = br.readLine();
                    StringBuilder sb = new StringBuilder(CurrentLine);
                    sb.deleteCharAt(0);
                    CurrentLine = sb.toString();
                    for (int i = 0; i <= NumOfInnerBlocks; i++) {//start to add the blocks by order to the LinkedList
                        CurrentFactory = new LineTypeFactory(CurrentLine);
                        CurrentObj = CurrentFactory.getLineTypeObj();
                        /*Checks if its variables or others and add variables to BlockVariables list*/
                        while (!CurrentObj.getType().equals("BLOCK") && !CurrentObj.getType().equals("ClOSED_BLOCK")) {
                            CurrentLine = br.readLine();
                            CurrentFactory = new LineTypeFactory(CurrentLine);
                            CurrentObj = CurrentFactory.getLineTypeObj();
                            CurrentObj.isValid((Block)CurrentBlock);
                        }
                        if (CurrentObj.getType().equals("BLOCK")) {
                            /*We are now at a new block*/
                            if(CurrentBlock != null) {
                                ((Block) CurrentObj).setPreviousBlock((Block) CurrentBlock);
                                ((Block) CurrentObj).ConcatenateVariables(((Block) CurrentBlock).getLocalVariablesArray());
                            }
                            CurrentBlock = CurrentObj;
                            CurrentBlock.isValid((Block)CurrentBlock);
                        } else {
                            CurrentBlock = ((Block) CurrentBlock).getPreviousBlock();
                            /*Continue reading lines*/
                            CurrentLine = br.readLine();
                        }
                    }IsInsideAblock = false;
                    CurrentBlock = null;
                }
            }
        }
    }

    /*Returns the current line on file*/
    public int getCurrentLineInFile() {
        return lineNum.getLineNumber();
    }

    /*get number of blocks inside a block*/
    public int getNumOfInnerBlocks(BufferedReader CurrentBlockLine) throws IOException {
        /*search how many block are opened and closed within the current Block**/
        int NumOfOpenedBlocks = 0;
        int NumOfClosedBlocks = 0;
        matcher = OpenBlock.matcher(CurrentLine);
        String CurrentLine;
        //while (!matcher.matches()){
        while (NumOfClosedBlocks <= NumOfOpenedBlocks) {
            CurrentLine = CurrentBlockLine.readLine();
            while (matcher.matches()) {
                NumOfOpenedBlocks += 1;
                CurrentLine = CurrentBlockLine.readLine();
                matcher = OpenBlock.matcher(CurrentLine);
            }
            matcher2 = ClosedBlock.matcher(CurrentLine);
            if (matcher2.matches()) {
                NumOfClosedBlocks += 1;
            }
            if (NumOfClosedBlocks == NumOfOpenedBlocks) {
                return NumOfOpenedBlocks;
            }
        }
        throw new IOException();// if there are too many close Brackets or open
    }
}


