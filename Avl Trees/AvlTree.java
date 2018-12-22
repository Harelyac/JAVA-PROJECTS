package oop.ex4.data_structures;
import java.util.Iterator;

/**
 * Created by Harel on 08/05/2017.
 */


public class AvlTree extends BinarySearchTree {
    /*An empty constructor*/
    public AvlTree(){
        super();
    }

    /*Constructor that makes AvlTree with values from given array*/
    public AvlTree(int[] data){
        super(data);
    }

    /*Constructor that Makes a tree out of a deep copy of an avlTree*/
    public AvlTree(AvlTree avlTree){
        for(int value : avlTree){
            this.add(value);
        }
    }


    /*Instance methods*/
    /*This method insert values to the tree. while inserting no further need
    * to check if the value is already there. because its binary search tree property,
    * we will be able to track down the unwanted duplicate if its exists. because of same
    * value on those nodes.*/
    public boolean add(int newValue){
        if(super.add(newValue))
            return true;
        else
            return false;
    }

    /*This method checks if the value is inside the tree but also returns its Height
    * at the same time. for ex - 0 for root height, -1 for node non exist and else otherwise*/
    public int contains(int searchVal) {
        return super.contains(searchVal);
    }

    /*This method delete value from the tree*/
    public boolean delete(int toDelete) {
        return super.delete(toDelete);
    }

    /*find the size of the avl tree - number of nodes*/
    public int size(){
        int size = 0;
        for(int key : this)
        {
           size++;
        }
        return size;
    }

    public Iterator<Integer> iterator(){
        return super.iterator();
    }

    /*Static methods*/

    /*Find minimal nodes with a given height value*/
    public static int findMinNodes(int h){
        if(h == 1)
            return 2;
        if(h == 2)
            return 4;
        else
            return 1 + findMinNodes(h - 1) + findMinNodes(h - 2);
    }

    /*Find maximal nodes via given height value*/
    public static int findMaxNodes(int h){
        int maxNodes;
        maxNodes = (2^(h+1)) - 1;
        return maxNodes;
    }

}
