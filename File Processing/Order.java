package filesprocessing;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daniel hazan on 5/30/2017.
 */
public class Order extends Section{
    Pattern p;
    Matcher m;
    static boolean crashreport;
    boolean HasREVERSEDsuffix;
    List<String> OrderedFiles;
    List<String> OrderedFilesNames;


    public Order (String myOrder, File[] FilesArray){
        this.OrderedFiles = new ArrayList<String>();
        this.OrderedFilesNames = new ArrayList<String>();
        crashreport = true;
        this.HasREVERSEDsuffix = false;
        String[] OrderParts = myOrder.split("#");
        if (OrderParts.length == 2) {
            if (OrderParts[1].equals("REVERSE")) {
                this.HasREVERSEDsuffix = true;
            }
        }
        switch (OrderParts[0]){
            case "size":
                OrderBySize(FilesArray);
                break;
            case "abs":
                OrderByName(FilesArray);
                break;
            case "type":
                OrderByType(FilesArray);
                break;
            case "":
                OrderByName(FilesArray);
                break;
            default://if any other case - report error and print in abs order
                OrderByName(FilesArray);
                crashreport = false;
                break;
        }
    }
    public List<String> getOrderedandFilteredArrayList(){
        return this.OrderedFiles;
    }

    public boolean IsWarningNeeded(){
        return crashreport;

    }
    /*Order the files array by name - abs*/
    public List<String> OrderByName(File[] FilesArray){
        for (File file:FilesArray){
                this.OrderedFiles.add(file.getAbsolutePath());
        }
        Collections.sort(this.OrderedFiles);
        if (this.HasREVERSEDsuffix) {
            Collections.reverse(this.OrderedFiles);
        }
        List<String> NewFilesNames = ConvertPathToName(this.OrderedFiles);
        this.OrderedFiles = NewFilesNames;
        return NewFilesNames;

    }

    /*This method converts path to name*/
    public List<String> ConvertPathToName(List<String> PathArray){
        //String[] NamesOfFilesArray = new String[PathArray.size()];
        List<String> NamesOfFilesArray = new ArrayList<String>();
        //int count = 0;
        for (String PathFile:PathArray){
            String splitRegex = Pattern.quote(System.getProperty("file.separator"));
            String[] PathParts = PathFile.split(splitRegex);
            int SizeOfPath = PathParts.length;
            //NamesOfFilesArray[count] = PathParts[SizeOfPath];
            NamesOfFilesArray.add(PathParts[SizeOfPath-1]);
            //count+= 1;
        }return NamesOfFilesArray;
    }
    Comparator<File> fileTypeComparator = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            int comparedByType = getTypeFromFileName(o1).compareTo(getTypeFromFileName(o2));
            if (comparedByType == 0) {//i.e the types are equal
                return o1.getName().compareTo(o2.getName());//order by abs name

            }
            return comparedByType;
        }
    };

    /*Order the filtered files by type according to defined comparator*/
    public List<String> OrderByType(File[] FilesArray){

        Arrays.sort(FilesArray, fileTypeComparator);
        for (File file:FilesArray){
            this.OrderedFiles.add(file.getName());
        }
        if (this.HasREVERSEDsuffix) {
            String[] newArray = reverseMe();
            this.OrderedFiles = new ArrayList<>(Arrays.asList(newArray));
        }
        return this.OrderedFiles;

    }
    /*Get the suffix type of certain file*/
    private String getTypeFromFileName(File file){
        //try to use matcher and end() to get the .txt!!!!!
        String mytype=null;
        try {
            p = Pattern.compile("(\\.?\\w*\\.?\\w+\\.)+(\\w*)");
            //String mee = "sdfsdtxtxt";
            String myName = file.getName();
            m = p.matcher(myName);
            m.matches();
            //System.out.println(m.matches());
            int n = m.groupCount();
            //System.out.println(m.group(n));
            mytype = m.group(n);

        }catch (IllegalStateException e){
            //System.out.println("i got here");
            mytype = ""; //if we got here - mytype is an empty string
        }
        return mytype;
    }
    Comparator<File> fileSizeComparator = new Comparator<File>() {
        @Override
        public int compare( File o1, File o2) {
            long comparedBySize = o1.length() - o2.length();
            if (comparedBySize<0)
                return -1;
            else if (comparedBySize>0)
                return 1;
            //if comparedBySize==0 , i.e the types are equal
            return o1.getName().compareTo(o2.getName());//order by abs name
        }
    };
    public List<String> OrderBySize(File[] FilesArray){
        Arrays.sort(FilesArray, fileSizeComparator);
        for (File file:FilesArray){
            this.OrderedFiles.add(file.getName());
        }
        if (this.HasREVERSEDsuffix) {
            String[] newArray = reverseMe();
            this.OrderedFiles = new ArrayList<>(Arrays.asList(newArray));
        }
        return this.OrderedFiles;
    }

    /*Important method which reverse the array*/
    public String[] reverseMe(){
        String[] newArray = new String[this.OrderedFiles.size()];
        newArray = this.OrderedFiles.toArray(newArray);
        for(int i=0; i < newArray.length / 2; i++) {
            String temp = newArray[i];
            newArray[i] = newArray[newArray.length - i - 1];
            newArray[newArray.length - i - 1] = temp;
        }
        return newArray;
    }
}
