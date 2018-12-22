package oop.ex4.data_structures;
import oop.ex4.data_structures.AvlNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Harel on 12/05/2017.
 */
public class InOrderIterator implements Iterator<Integer> {
    public LinkedList<AvlNode> fakeStack = new LinkedList<AvlNode>();

    /*This part basically makes the "stack"*/
    public void pushLeftChildren(AvlNode current){
        while(current != null){
            fakeStack.push(current);
            current = current.getLeftChild();
        }
    }

    /*Constructor*/
    public InOrderIterator(AvlNode root){
        pushLeftChildren(root);
    }


    @Override
    /*Checks if the avl has next object*/
    public boolean hasNext() {
        return !fakeStack.isEmpty();
    }

    @Override
    /*Retrieve the next object - successor*/
    public Integer next() {
        if(!hasNext()){
            throw new NoSuchElementException("All nodes have been visited");
        }
        AvlNode result = fakeStack.pop();
        pushLeftChildren(result.getRightChild());
        return result.getData();
    }

    /*we have to implement this method as part of iterator interface methods that need to be implemented*/
    public void remove(){
        throw new UnsupportedOperationException("remove");
    }
}
