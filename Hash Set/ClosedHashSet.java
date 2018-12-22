/**
 * Created by Harel on 01/05/2017.
 */

import java.util.LinkedList;
import java.util.Objects;


/*This class implements the simpleHashSet class which implement by chaining cause
* also simpleSet class*/
public class ClosedHashSet extends SimpleHashSet {

    /*Fields - dataMember*/
    private String[] hashTable;

    /*Three different constructors*/

    /*empty one*/
    public ClosedHashSet(){
        hashTable = new String[DEFAULT_INITIAL_CAPACITY];
    }

    /*empty with limits included*/
    public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor){
        hashTable = new String[DEFAULT_INITIAL_CAPACITY];
        upperFactor = upperLoadFactor;
        lowerFactor = lowerLoadFactor;
    }

    /*Making the full HashSet! check if data in not null and tries to add it onto the hash set*/
    public ClosedHashSet(java.lang.String[] data){
        /*Go over all the data with the foreach new method!*/
        hashTable = new String[DEFAULT_INITIAL_CAPACITY];
        for (String value : data){
            /*No null method in data files! mean no object.equals needed!*/
            if(value!= null){
                add(value);
            }
        }
    }

    /*Adds value to the hash set, check if the structure need to be hashed before*/
    public boolean add(java.lang.String newValue) {
        int counter = 0;
        /*Check if its not inside HashSet*/
        if(!contains(newValue)){
            /*First of all - check for rehash*/
            if(checkForRehash("add")) {
            /*Make a rehash!*/
                reHash("add");
            }
            /*Do a do-while until we get an empty cell*/
            do{
                indexOfCell = clamp(newValue,counter);
                counter++;}
            while(!Objects.equals(hashTable[indexOfCell],isDeleted) && hashTable[indexOfCell] != null);
            /*Finally add it to the array*/
            hashTable[indexOfCell] = newValue;
            currentSize++;
            return true;
            }
        return false;
    }


    /*This method actually checks if we have certain value already inside the structure*/
    public boolean contains(String searchVal) {
        for(int i = 0; i < capacity(); i++){
            indexOfCell = clamp(searchVal, i);
            if(hashTable[indexOfCell] == null)
                return false;
            /*Check if we can continue search and its just another element with same hash
            code that we removed and not the one that we are looking for*/
            if(hashTable[indexOfCell].equals(isDeleted)) {
                continue;
            }
            if(hashTable[indexOfCell].equals(searchVal)){
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean delete(String toDelete) {
        /*Check availability of value to delete*/
        if(contains(toDelete) && size() > 0){
            /*mark the cell as deleted*/
            hashTable[indexOfCell] = isDeleted;
            currentSize--;
            if(checkForRehash("delete"))
                reHash("delete");
            return true;
        }
        return false;
    }

    /*Returns the current size of the structure*/
    public int size() {
        return currentSize;
    }

    /*Do the rehash thing - which mean that we make a new sized table and pour all values again to it*/
    public void reHash(String action) {
        /*determine the new capacity*/
        switch (action){
            case "add":
                newCapacity = tableCapacity * 2;
                break;
            case "delete":
                newCapacity = tableCapacity / 2;
                break;
        }

        /*make the new structure via using a temp table for help*/
        String[] tempTable = new String[newCapacity];
        tableCapacity = newCapacity;
        int index = 0;
        /*Go over all values, pour into temp table*/
        for(String object: hashTable){
            if(object != null && (!object.equals(isDeleted))) {
                /*After this check we can take the value*/
                tempTable[index] = object;
                index++;
            }
        }
        hashTable = tempTable;
    }

    public int clamp(String value, int i) {
        return (value.hashCode() + (i + i * i) / 2) & (capacityMinusOne());
    }
}
