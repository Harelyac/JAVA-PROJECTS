package filesprocessing;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Harel on 01/06/2017.
 */

public class CommandFileHandler {
    /*Fields variables*/
    private String file_path;
    private FileReader fr;
    private BufferedReader br;
    private String currentLine;
    private int sectionDifference = 4;
    private int SectionIndexCounter = 0;
    private ArrayList<ArrayList<String>> ListOfSections = new ArrayList<ArrayList<String>>(10);
    private ArrayList<String> currentSection = new ArrayList<String>(sectionDifference);
    private ArrayList<String> tempSection;

    /*Constants fields*/
    private static final String FILTER = "FILTER";
    private static final String ORDER = "ORDER";

    public CommandFileHandler(String path){
        file_path = path;
    }

    public Exception Reader() throws IOException, SubSectionNamesException {
        fr = new FileReader(file_path);
        br = new BufferedReader(fr);

        /*Reads line from command file and parse them to sections of 4 lines each*/
        while ((currentLine = br.readLine()) != null) {
            /*Read rest 3 lines from a file and put into the section list*/
            currentSection.add(currentLine);

            for (int i = 0; i < 2; i++){
                currentLine = br.readLine();
                currentSection.add(currentLine);
            }
            /*This section check if we deal with section size 2 or 3*/
            if(currentLine != null) {
                br.mark(this.getLineOfSection(currentSection));
                if((currentLine = br.readLine()) != null) {
                    if (!currentLine.equals(FILTER)) {
                        currentSection.add(currentLine);
                        br.mark(this.getLineOfSection(currentSection));
                    }
                    /*This step mean that we moved the buffer but we
                    did not actually added it to the current section,
                    so we go back to the marked spot again*/
                    br.reset();
                }
                else
                    currentSection.add("");
            }

            /*This lines checks high level means type 2 errors*/
            if ((!CheckFilterSubSection(currentSection).equals("good")) ||
                    (!CheckOrderSubSection(currentSection).equals("good"))) {

                if(CheckFilterSubSection(currentSection).equals("filter-case-sensitive"))
                    throw  new SubSectionNamesException("bad filter name");
                else if(CheckFilterSubSection(currentSection).equals("filter-is-missing"))
                    throw new SubSectionNamesException("filter name is missing");

                if(CheckOrderSubSection(currentSection).equals("order-case-sensitive"))
                    throw new SubSectionNamesException("bad order name");
                else if(CheckOrderSubSection(currentSection).equals("order-is-missing"))
                    throw new SubSectionNamesException("order name is missing");
            }
            else
                {
                /*Adds the section to the listOfSection*/
                tempSection = new ArrayList<String>(currentSection);
                ListOfSections.add(tempSection);
                currentSection.clear();
            }
        }
        /*Close file*/
        if (br != null)
            br.close();
        if (fr != null)
            fr.close();
        return null;
    }

    /*Checks filter subsection names*/
    private String CheckFilterSubSection(ArrayList currentSection) {
        Pattern ptn1 = Pattern.compile(FILTER, Pattern.CASE_INSENSITIVE);
        if (currentSection.size()>=1) {
            Matcher mtch1 = ptn1.matcher((String) currentSection.get(0));
            if (currentSection.get(0).equals(FILTER))
                return "good";
            else if (mtch1.matches())
                return "filter-case-sensitive";
            else
                return "filter-is-missing";
        }return "filter-is-missing";
    }
    /*Checks order subsection names*/
    private String CheckOrderSubSection(ArrayList currentSection){
        Pattern ptn2 = Pattern.compile(ORDER, Pattern.CASE_INSENSITIVE);
        if (currentSection.size()>=3 && currentSection.get(currentSection.size()-1)!=null) {
            Matcher mtch2 = ptn2.matcher((String) currentSection.get(2));
            if (currentSection.get(2).equals(ORDER))
                return "good";
            else if (mtch2.matches())
                return "filter-case-sensitive";
            else
                return "order-is-missing";
        }return "order-is-missing";
    }
    /*Retrieve the next section on the array*/
    public ArrayList getNextSectionData() {
            ArrayList currentSection;
            if(ListOfSections.size() > SectionIndexCounter) {
                //currentSection = ListOfSections.get(sectionCounter);
                currentSection = ListOfSections.get(SectionIndexCounter);
                /*sectionCounter = sectionCounter + sectionDifference;*/
                SectionIndexCounter += 1;
                return currentSection;
            }
            else
                return null;
    }
    /*Retrive the line of the section - where the section starts*/
    public int getLineOfSection(ArrayList currentSection) {
        int count = 1;
        for (ArrayList array : this.ListOfSections) {
            if(!(array.equals(currentSection)))
                count += array.size();
            else
                break;
        }
        return count;
    }
}

