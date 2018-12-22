import java.util.Collection;

/**
 * Created by Harel on 01/05/2017.
 */

/*This class show itself as facade but actually the method all 4 are being
 invoked and used from collection arsenal*/
public class CollectionFacadeSet implements SimpleSet{
    private java.util.Collection<String> collection;

    /*This constructor gets the collection value and wrap it under the facade class -
    * originally it just basic Collection class which delegate 4 methods of simpleSet api*/
    public CollectionFacadeSet(java.util.Collection<String> facade){
        collection = facade;
    }

    /*add the value into the structure*/
    public boolean add(String newValue) {
        if(!contains(newValue))
            return collection.add(newValue);
        return false;
    }

    /*check if value is inside the structure*/
    public boolean contains(String searchVal) {
        return collection.contains(searchVal);
    }

    /*delete value from structure ofc only if exist*/
    public boolean delete(String toDelete) {
        if(contains(toDelete)){
            return collection.remove((toDelete));
        }
        else
            return false;
    }

    /*check size of structure*/
    public int size() {
        return collection.size();
    }
}
