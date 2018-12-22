package filesprocessing;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel hazan on 5/30/2017.
 */
public class Section {
    ArrayList StrReprOfSection;
    Filter myfilter;
    Order myorder;
    int lineNum;
    private static final String FILTER = "FILTER";
    private static final String ORDER = "ORDER";
    File[] files;
    File[] newFile;
    List<String> OrderedFiles;

    public Section(ArrayList ReprOfSection, File[] FilesArray, int line){
        /**
         * param:ReprOfSection : a string array contains two lines of the filter and Order SubSections
         * param:FileArray: an array contains the files from sourcedir which has to be filtered and ordered*/
        StrReprOfSection = ReprOfSection;
        lineNum = line;
        files = FilesArray;

        /*at this point we make filter object and start filtering the core files*/
        myfilter = (Filter)Factory(FILTER);
        List<File> FilteredFiles = myfilter.GetFilteredArray();
        File[] newFilesArray = new File[FilteredFiles.size()];
        newFile = FilteredFiles.toArray(newFilesArray);
        myorder = (Order)Factory(ORDER);
        OrderedFiles = myorder.getOrderedandFilteredArrayList();
    }

    public Section() {
    }

    public boolean IfWarningNeeded(){
        if (!this.myfilter.IsWarningNeeded()){
            //if the error occured in the Filter SubSection
            //print warning using "lineNum" field to achieve the line of error
            System.out.println("Warning in line "+ (this.lineNum+1));

        }if (!this.myorder.IsWarningNeeded()){
            //if error occured in the Order SubSection,
            //print warning using "lineNum" field to achieve the line of error
            System.out.println("Warning in line "+ (this.lineNum + 3));

        }return true;
    }
    public Filter getFilterObj(){
        return this.myfilter;
    }
    public Order getOrderObj(){
        return this.myorder;
    }

    public List<String> getOrderedAndFilteredArrayList(){
        /**this method returns an arrayList containing the names of files sorted according the
         * order & filter commandLines */
        return this.OrderedFiles;
    }

    public Section Factory(String type){
        switch(type){
            case ORDER:
                if(StrReprOfSection.size() == 4) {
                    myorder = new Order(this.StrReprOfSection.get(3).toString(), this.newFile);
                }
                else
                {
                    /*in case order is only 1 line total*/
                    myorder = new Order("abs", this.newFile);
                }
                return myorder;
            case FILTER:
                myfilter = new Filter(this.StrReprOfSection.get(1).toString(),this.files);
                return myfilter;
        }
        return null;
    }
}
