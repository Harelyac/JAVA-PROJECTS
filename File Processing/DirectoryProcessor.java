package filesprocessing;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Harel on 31/05/2017.
 */
public class DirectoryProcessor {
    private static String source_directory;
    private static String command_file;
    private static CommandFileHandler Handler;
    private static int SectionLine;
    private static List<String> OrderedFiles;

    /*This method is the main method of the whole project*/
    public static void main(String [] args) throws IllegalArgumentException{
        source_directory =  args[0];
        command_file = args[1];
        try {
            if ((args[0] == null || args[1] == null)){
                System.err.println("ERROR:");
                System.err.println("The arguments are not valid\n");
                throw new IllegalArgumentException();
            }
            /*Make an handler to the command file, Divide the command file into sections
            * and make section object out of each section*/
            Handler = new CommandFileHandler(command_file);
            /*Reads from the command file and check for any sort of exception*/
            Handler.Reader();

            ArrayList SectionData = Handler.getNextSectionData();
            while(SectionData != null) {
                /*if it got to this point it means that all went well*/
                SectionLine = Handler.getLineOfSection(SectionData);
                File directory = new File(source_directory);
                File[] files_list = directory.listFiles();
                files_list = CheckForNoneFile(files_list);

                Section currentSection = new Section(SectionData, files_list, SectionLine);
                currentSection.IfWarningNeeded();
                /*only after printing all the warning needed we can print the files with the given order*/
                OrderedFiles = currentSection.getOrderedAndFilteredArrayList();

                /*Print the result files to screen*/
                for (int i = 0; i < OrderedFiles.size(); i++) {
                    System.out.println(OrderedFiles.get(i));
                }
                SectionData = Handler.getNextSectionData();
            }
        }
        catch (SubSectionNamesException | IOException | IllegalArgumentException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    /*This method plays very important role by checking if are working with a file or not*/
    public static File[] CheckForNoneFile(File[] filesArray){
        ArrayList<File> newArrayFile = new ArrayList<File>();

        for(File file:filesArray){
            if(file.isFile()){
                newArrayFile.add(file);
            }
        }
        File[] newArray = new File[newArrayFile.size()];
        File[] newArrayList= newArrayFile.toArray(newArray);
        return newArrayList;
    }
}
