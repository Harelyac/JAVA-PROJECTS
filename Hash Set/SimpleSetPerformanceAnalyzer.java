import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * Created by Harel on 05/05/2017.
 */
public class SimpleSetPerformanceAnalyzer {
    private static final int NUM_OF_STRUCTURES = 5;
    private SimpleSet[] structuresBase = new SimpleSet[NUM_OF_STRUCTURES];
    private String[] structuresNames = new String[NUM_OF_STRUCTURES];
    private static final int CONVERT_TO_NANO_TO_MILLI = 1000000;
    private static final int WARM_UP_ITERATIONS = 70000;
    private static final int LINKED_LIST_WARM_UP = 7000;

    // Pour all the data into 1 big array
    private static String[] data1 = Ex3Utils.file2array("C:\\Users\\harel\\Desktop\\oop\\ex3.1\\src\\data1.txt");
    private static String[] data2 = Ex3Utils.file2array("C:\\Users\\harel\\Desktop\\oop\\ex3.1\\src\\data2.txt");



    private SimpleSetPerformanceAnalyzer(){


        /*Initialize array with all kinds of structures we have at the moment*/
        structuresBase[0] = new ClosedHashSet();
        structuresBase[1] = new OpenHashSet();
        structuresBase[2] = new CollectionFacadeSet(new HashSet<>());
        structuresBase[3] = new CollectionFacadeSet(new TreeSet<>());
        structuresBase[4] = new CollectionFacadeSet(new LinkedHashSet<>());

        /*Initialize array of structures names*/
        structuresNames[0] = "Close-Hash-Set";
        structuresNames[1] = "Open-Hash-Set";
        structuresNames[2] = "HashSet";
        structuresNames[3] = "TreeSet";
        structuresNames[4] = "LinkedList";

    }

    /*This method adds strings from given file to each and every data structure above*/
    private void addingStringsFromFiles(String[] data) {
        long startTime;
        long difference;
        startTime = System.nanoTime();
        for (int i = 0; i < structuresBase.length; i++) {
            System.out.println("Starting Adding data to " + structuresNames[i]);
            for (String str : data)
                if(str != null)
                    structuresBase[i].add(str);
            difference = (System.nanoTime() - startTime) / CONVERT_TO_NANO_TO_MILLI;
            System.out.println("The Adding Time for " + structuresNames[i] + " took " + difference + " MilliSeconds \n");
        }
    }


    /*This method add data and then check if certain value located inside the structures
    * while invoking the contain method of each structure and checks for operation time*/
    private void containRest(String[] data, String value){
        long startTime;
        long difference;
        /*Add all data to each structure and exclude linkedList*/
        for(int i = 0; i < structuresBase.length - 1; i++){
            for(int k = 0; k < data.length; k++)
                structuresBase[i].add(data[k]);
            /*invoke warm up first*/
            WarmItUp(structuresBase[i], value, "Rest");
            /*Now measure time start and end with same warm up method*/
            startTime = System.nanoTime();
            WarmItUp(structuresBase[i], value, "Rest");
            difference = (System.nanoTime() - startTime) / WARM_UP_ITERATIONS;
            System.out.println("The contain method on " + structuresNames[i] + " took about " + difference + " NanoSeconds \n");
        }

        /*Dealing with LinkedList*/
        /*Initialize structure - add all data to it*/
        for (String str : data){
            structuresBase[4].add(str);
        }
        startTime = System.nanoTime();
        WarmItUp(structuresBase[4],value, "LinkedList");
        difference = System.nanoTime() - startTime / LINKED_LIST_WARM_UP;
        System.out.println("The contain method on " + structuresNames[4] + " took about " + difference + " NanoSeconds \n");
    }


    /*Call the warm up method 70000 times without measuring*/
    private void WarmItUp(SimpleSet structure, String value,String type){
        switch (type){
            case "LinkedList":
                for(int i = 0; i < LINKED_LIST_WARM_UP; i++) structure.contains(value);
                break;
            case "Rest":
                for(int i = 0; i < WARM_UP_ITERATIONS; i++) structure.contains(value);
                break;
        }
    }


    public static void main(String[] args){
        SimpleSetPerformanceAnalyzer analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of adding data1.txt file into all structures \n");
        analyzer.addingStringsFromFiles(data1);
        System.out.println("-------------------------------------------------------- \n");

        analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of adding data2.txt file into all structures \n");
        analyzer.addingStringsFromFiles(data2);
        System.out.println("-------------------------------------------------------- \n");

        analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of invoking the contain method with 'hi' value from data1 \n");
        analyzer.containRest(data1,"hi");
        System.out.println("-------------------------------------------------------- \n");

        analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of invoking the contain method with '-13170890158' value from data1 \n");
        analyzer.containRest(data1,"-13170890158");
        System.out.println("-------------------------------------------------------- \n");

        analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of invoking the contain method with 'hi' value from data2 \n");
        analyzer.containRest(data2,"hi");
        System.out.println("-------------------------------------------------------- \n");

        analyzer = new SimpleSetPerformanceAnalyzer();
        System.out.println("Comparisons of invoking the contain method with '23' value from data2 \n");
        analyzer.containRest(data2,"23");
        System.out.println("-------------------------------------------------------- \n");

    }

}
