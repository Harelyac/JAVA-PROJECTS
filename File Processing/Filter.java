package filesprocessing;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daniel hazan on 5/30/2017.
 */
public class Filter extends Section {
    Pattern p;
    Matcher m;
    boolean crashreport;
    boolean HasNotSuffix;
    List<File> FilteredFiles;
    static String[] FilterParts;

    /*In this filer we've used regex manipulation and not the normal checks that we could
    *  here we check for the 3 type of math we should check*/
    public Filter(String myfilter, File[] FileArray) {
        FilteredFiles = new ArrayList<File>();
        p = Pattern.compile("\\w+\\#*\\w*|\\w+\\#\\.?\\w*\\.?\\w*\\#*\\w*|\\w+\\#\\d+\\.?\\d*\\#\\d+\\.?\\d+\\#*\\w*");
        m = p.matcher(myfilter);
        this.HasNotSuffix = false;
        this.crashreport = true;//initialize the crashreport to be true
        if (m.matches()) {
            FilterParts = SplitFilter(myfilter);
            int SizeArray = FilterParts.length;
            if (SizeArray==2 || SizeArray==3){ //check if the NOT suffix comes at the end of the filter
                if (FilterParts[SizeArray-1].equals("NOT"))
                    this.HasNotSuffix = true;
            }
            /*Check all the possibilities inside this magical switch-case*/
            switch (FilterParts[0]){
                case "greater_than":
                    LeastBoundFilter(FilterParts[1],FileArray);
                    break;
                case "smaller_than":
                    UpperBoundFilter(FilterParts[1],FileArray);
                    break;
                case "between":
                    FilterBySize(FilterParts[1],FilterParts[2],FileArray);
                    break;
                case "file":
                    FilterByFileName(FilterParts[1],FileArray);
                    break;
                case "contains":
                    FilterByContains(FilterParts[1], FileArray);
                    break;
                case "prefix" :
                    FilterByPrefix(FilterParts[1], FileArray);
                    break;
                case "suffix" :
                    FilterBySuffix(FilterParts[1], FileArray);
                    break;
                case "writable":
                    FilterByWritable(FilterParts[1],FileArray);
                    break;
                case "executable":
                    FilterByExecutable(FilterParts[1], FileArray);
                    break;
                case "hidden":
                    FilterByHidden(FilterParts[1], FileArray);
                    break;
                case "all" :
                    FilterByAll(FileArray);
                    break;
                case "" :
                    FilterByAll(FileArray);
                    break;
                default:
                    FilterByAll(FileArray);
                    this.crashreport = false;
                    break;
            }
        }else {
            FilterByAll(FileArray);//if the first filter not matched - filter by all
            this.crashreport = false;
        }
    }
    /*Return a flag to say if warning is needed*/
    public boolean IsWarningNeeded() {
        /**checks if anything from exceptionType1 occured.if crashreport turned to false -
         * Section class will print a warning message and all files by filter all */
        return crashreport;

    }
    /*Gets the filtered array*/
    public List<File> GetFilteredArray(){
        return this.FilteredFiles;
    }

    /*Filter the files by default*/
    public List<File> FilterByAll(File[] FileArray) {
        for (File file:FileArray) {
            if (HasNotSuffix==true && this.crashreport==true)
                break;
            this.FilteredFiles.add(file);
        }
        return this.FilteredFiles;
    }

    /*Filter with Lower flag*/
    public List<File> LeastBoundFilter(String filter,File[] FileArray){

            for (File file : FileArray) {
                if (!this.HasNotSuffix) {
                    if (IsGreaterThan(file, Double.parseDouble(filter)))
                        this.FilteredFiles.add(file);

                }else{
                    if (IsSmallerThan(file,Double.parseDouble(filter)))
                        this.FilteredFiles.add(file);
                }
            }
            return this.FilteredFiles;

    }
    /*Filter by greater flag*/
    public List<File> UpperBoundFilter(String filter , File[] FileArray){
        for (File file:FileArray) {
            if (!this.HasNotSuffix) {
                if (IsSmallerThan(file, Double.parseDouble(filter)))
                    this.FilteredFiles.add(file);
            } else {
                if (IsGreaterThan(file, Double.parseDouble(filter)))
                    this.FilteredFiles.add(file);
            }
        }
            return this.FilteredFiles;
    }
    /*Filter the files by size*/
    public List<File> FilterBySize(String minSize, String MaxSize, File[] FileArray){
        if (Double.parseDouble(minSize)>Double.parseDouble(MaxSize)) {
            this.crashreport = false;
            return FilterByAll(FileArray);

        }else if (Double.parseDouble(minSize)<0 ||Double.parseDouble(MaxSize)<0){
            this.crashreport = false;
            return FilterByAll(FileArray);
        }
        for (File file :FileArray){
            if (!this.HasNotSuffix) {
                if (IsGreaterThan(file, Double.parseDouble(minSize)) && IsSmallerThan(file, Double.parseDouble(MaxSize))) {
                    this.FilteredFiles.add(file);
                }
            }else{
                if (IsSmallerThan(file, Double.parseDouble(minSize)) || IsGreaterThan(file, Double.parseDouble(MaxSize)))
                    this.FilteredFiles.add(file);
            }
        }return FilteredFiles;
    }
    /*This method filter the files by name*/
    public List<File> FilterByFileName(String filter, File[] FileArray){
        for (File file:FileArray){
            if (!this.HasNotSuffix) {
                if (hasName(file, filter))
                    this.FilteredFiles.add(file);
            }else{
                if (!hasName(file, filter))
                    this.FilteredFiles.add(file);
            }
        }return this.FilteredFiles;

    }
    /*Check files if the user get write permission*/
    public List<File> FilterByWritable(String filter,File[] FileArray){
        if (!(filter.equals("YES")||filter.equals("NO"))) {
            this.crashreport = false;
            FilterByAll(FileArray);
        }
        for (File file: FileArray){
            if (!this.HasNotSuffix) {
                if (file.canWrite() && filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if (!file.canWrite() && filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }else{
                if (!file.canWrite() && filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if (file.canWrite() && filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }
        }return this.FilteredFiles;
    }
    /*Checks for execute permissions*/
    public List<File> FilterByExecutable (String filter, File[] FileArray){
        if (!(filter.equals("YES")||filter.equals("NO"))) {
            this.crashreport = false;
            FilterByAll(FileArray);
        }
        for (File file : FileArray){
            if (!this.HasNotSuffix) {
                if (file.canExecute() && filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if ((!file.canExecute()) && filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }else{
                if ((!file.canExecute()) && filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if (file.canExecute() && filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }
        }return this.FilteredFiles;
    }

    /*Check if the file is hidden from a user*/
    public List<File> FilterByHidden (String filter, File[] FileArray){
        if (!(filter.equals("YES")||filter.equals("NO"))) {
            this.crashreport = false;
            FilterByAll(FileArray);
            return null;
        }for (File file : FileArray){
            if (!this.HasNotSuffix) {
                if (file.isHidden() &&filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if (!file.isHidden()&& filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }else{
                if (!file.isHidden() && filter.equals("YES"))
                    this.FilteredFiles.add(file);
                if (file.isHidden() && filter.equals("NO"))
                    this.FilteredFiles.add(file);
            }
        }return this.FilteredFiles;
    }


    public String[] SplitFilter(String filterLine){
        String[] LineParts = filterLine.split("#");
        return LineParts;

    }

    /*Filter if files contains certain value*/
    public List<File> FilterByContains(String searchVal , File[] FileArray){

        for (File file:FileArray){
            if (!this.HasNotSuffix) {
                if (isContain(file, searchVal))
                    this.FilteredFiles.add(file);
            }else{
                if (!isContain(file, searchVal))
                    this.FilteredFiles.add(file);
            }
        }return this.FilteredFiles;

    }
    private boolean IsGreaterThan(File file, double minSize){
        if (file.length()>=minSize*1024 && (FilterParts[0].equals("between") || FilterParts[0].equals("smaller_than")))
            return true;
        if (file.length()> minSize*1024 && FilterParts[0].equals("greater_than"))
            return true;
        return false;

    }
    private boolean IsSmallerThan(File file,double maxSize){
        if (file.length()<= maxSize*1024 && (FilterParts[0].equals("between") || FilterParts[0].equals("greater_than")))
            return true;
        if (file.length()< maxSize*1024 && FilterParts[0].equals("smaller_than"))
            return true;
        return false;
    }
    private boolean hasName(File file, String name){
        String FileName = file.getName();
        this.p = Pattern.compile(name);
        this.m = p.matcher(FileName);
        /**
        if (name.equals(FileName.substring(m.start(),m.end())))//check if the name is the only string FileName contains
         */
        if (name.equals(FileName))
            return true;
        return false;

    }
    private boolean isContain(File file , String searchVal){
        /**check if the current File matches to the filter*/
        String FileName = file.getName();
        this.p = Pattern.compile(searchVal);
        this.m = p.matcher(FileName);
        return m.matches();
    }
    private boolean hasPrefix(File file , String prefix){
        String FileName = file.getName();
        this.p = Pattern.compile(prefix);
        this.m = p.matcher(FileName);
        return m.lookingAt();//search prefix at the beginning of FileName
    }
    private boolean hasSuffix(File file, String suffix){
        String FileName = file.getName();
        this.p = Pattern.compile(suffix);
        this.m = p.matcher(FileName);
        /**
         * int NumOfGroups = m.groupCount;
         * if (suffix.equals(m.group(NumOfGroups)))   //check the last matching
         *     return true;
         * return false*/
        m.reset();
        while (m.find()) {

            if (m.end() == FileName.length() )
                return true;

        }return false;
    }
    public List<File> FilterByPrefix(String prefix , File[] FileArray){
        for (File file:FileArray){
            if (!this.HasNotSuffix) {
                if (hasPrefix(file, prefix)) {
                    this.FilteredFiles.add(file);
                }
            }else {
                if (!hasPrefix(file, prefix)) {
                    this.FilteredFiles.add(file);
                }
            }
        }return this.FilteredFiles;

    }
    public List<File> FilterBySuffix(String suffix, File[] FileArray){
        for (File file:FileArray){
            if (!this.HasNotSuffix) {
                if (hasSuffix(file, suffix)) {
                    this.FilteredFiles.add(file);
                }
            }else{
                if (!hasSuffix(file, suffix)) {
                    this.FilteredFiles.add(file);
                }
            }
        }return this.FilteredFiles;


    }

}
