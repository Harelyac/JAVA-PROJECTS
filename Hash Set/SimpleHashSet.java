import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by Harel on 28/04/2017.
 */
public abstract class SimpleHashSet implements SimpleSet {

    protected float lowerFactor = 0.25f; /*The lowest limit which tells us when to rehash*/
    protected float upperFactor = 0.75f; /*The highest limit which tells us when to rehash*/
    protected final int DEFAULT_INITIAL_CAPACITY = 16; /*The initial capacity of the table*/
    protected int tableCapacity = DEFAULT_INITIAL_CAPACITY;
    protected int newCapacity; /*The new capacity after rehash*/
    protected String isDeleted = ""; /*Checks if cell is deleted*/
    protected int indexOfCell;
    protected int indexOfBucket;
    protected int currentSize = 0;


    /*Add a specified element to the set if it's not already in it*/
    public abstract boolean add(String newValue);

    /*Look for a specified value in the set.*/
    public abstract boolean contains(String searchVal);

    /*Remove the input element from the set.*/
    public abstract boolean delete(String toDelete);

    /*Return the number of elements currently in the set*/
    public abstract int size();

    /*This method rehash the hashSet*/
    public abstract void reHash(String action);

    public int capacity(){
        return tableCapacity;
    }

    /*--Until this point all methods are abstract and will be implemented by other classes--*/

    /*Return the LowerLoadFactor*/
    public float getLowerLoadFactor(){
        return lowerFactor;
    }

    /*Return the upper load factor*/
    public float getUpperLoadFactor(){
        return upperFactor;
    }

    /*A capacity method further use in the upcoming hours*/
    public int capacityMinusOne(){
        return (capacity() - 1);
    }

    /*Check for rehash in both cases - add or delete value*/
    public boolean checkForRehash(String action){
        float loadFactor;
        switch (action){
            case "add":
                /*Check what will happen if we add element to the structure*/
                loadFactor = (float) (size() + 1) / (float)capacity();
                if (loadFactor > upperFactor)
                    return true;
                break;
            case "delete":
                  /*Check what will happen if we delete element from the structure*/
                loadFactor = (float) (size() - 1) / (float)capacity();
                if(loadFactor < lowerFactor)
                    return true;
                break;
        }
        return false;
    }
}
