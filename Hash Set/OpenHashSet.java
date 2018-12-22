import javax.management.OperationsException;

/**
 * Created by Harel on 01/05/2017.
 */
public class OpenHashSet extends SimpleHashSet {
    /*Only declare*/
    protected LinkedListWrapper[] hashTable;

    /*Initialize the hashSet*/
    public OpenHashSet(){
        hashTable = new LinkedListWrapper[DEFAULT_INITIAL_CAPACITY];
    }

    /*Initialize and also include limits for rehash*/
    public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
        hashTable = new LinkedListWrapper[DEFAULT_INITIAL_CAPACITY];
        upperFactor = upperLoadFactor;
        lowerFactor = lowerLoadFactor;
    }

    /*Same method and again go over all data and pour it into the structure*/
    public OpenHashSet(java.lang.String[] data){
        hashTable = new LinkedListWrapper[DEFAULT_INITIAL_CAPACITY];
        for (String value : data) {
            /*No null method in data files! mean no object.equals needed!*/
            if (value != null) {
                add(value);
            }
        }
    }

    public boolean add(java.lang.String newValue){
        /*Check for troubles first if its not already inside the openHash*/
        if(!contains(newValue)) {
            /*Getting a clamped index*/
            if (checkForRehash("add"))
                reHash("add");
            indexOfBucket = clamp(newValue);
            if (hashTable[indexOfBucket] == null) {
                hashTable[indexOfBucket] = new LinkedListWrapper();
                hashTable[indexOfBucket].add(newValue);
                currentSize++;
                return true;
            }
            else{
                hashTable[indexOfBucket].add(newValue);
                currentSize++;
                return true;
            }

        }
        return false;
    }

    /*simple method which checks for existing bucket i.e
    linked list and checks for certain value inside it*/
    public boolean contains(String searchVal) {
        indexOfBucket = clamp(searchVal);

        /*now we check if the "bucket - the linked list* exist and
        if there is the cell we want inside it*/
        return hashTable[indexOfBucket] != null && hashTable[indexOfBucket].contains(searchVal);
    }

    @Override
    public boolean delete(String toDelete) {
        if(contains(toDelete)){
            if(checkForRehash("delete"))
            {
                reHash("delete");
            }
            /*reveal the bucket which from we want to delete the value*/
            indexOfBucket = clamp(toDelete);
            hashTable[indexOfBucket].remove(toDelete);
            currentSize--;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void reHash(String action) {
        LinkedListWrapper[] tempHashTable;
        /*determine the new capacity*/
        switch (action){
            case "add":
                newCapacity = tableCapacity * 2;
                break;
            case "delete":
                newCapacity = tableCapacity / 2;
                break;
        }
        tempHashTable = new LinkedListWrapper[newCapacity];
        /*Set also the new capacity*/
        tableCapacity = newCapacity;
        int bucketIndex;
        /*Go over each bucket-linkedList on hashSet*/
        for(LinkedListWrapper bucket : hashTable){
            if(bucket != null ) {
                String head = bucket.pollFirst();
                if (head != null) {
                    bucketIndex = clamp(head);
                    /*have to check prior that its not null! by default its null!*/
                    if (tempHashTable[bucketIndex] == null)
                        tempHashTable[bucketIndex] = new LinkedListWrapper();
                    /*Iterate until we get to null - end of linkedList*/
                    while (head != null) {
                        tempHashTable[bucketIndex].add(head);
                        head = bucket.poll();
                    }
                }
            }
        }
        hashTable = tempHashTable;
    }

    public int clamp(String value) {
        return  (value.hashCode()&(capacityMinusOne()));
    }
}
