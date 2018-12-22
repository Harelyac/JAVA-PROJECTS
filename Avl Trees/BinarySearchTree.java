package oop.ex4.data_structures;
import oop.ex4.data_structures.AvlNode;
import java.util.Iterator;

/**
 * Created by Harel on 11/05/2017.
 */
public class BinarySearchTree implements Iterable<Integer> {
    private final int LEFT_VIOLATION = 2, RIGHT_VIOLATION = -2;
    private boolean Checking = true;

    public AvlNode getRoot() {
        return root;
    }

    public void setRoot(AvlNode root) {
        this.root = root;
    }

    public AvlNode root;

    public BinarySearchTree() {
        root = null;
    }

    public BinarySearchTree(int[]data) {
        for (int i = 0; i < data.length; i++) {
            /*add each value 1 by one*/
            this.add(data[i]);
        }
    }

    /*This a an add method to binary search tree, basically with some changes though its an avl after all*/
    public boolean add(int newValue) {
        AvlNode currentNode = null;
        boolean alreadyThere = false;
        int heightCounter = 0;

        /*Checks if tree is empty*/
        if (this.root == null) {
            this.root = new AvlNode(newValue);
            this.root.setHeight(0);
            return true;
        }
        /*Checks if value is equal to root and start checking subtrees after that,
        * needless to say that this method cover all sorts of initialization of a node*/
        else {
            currentNode = this.root;
            while ((currentNode != null) && (!alreadyThere)) {
                /*Check left position*/
                if (newValue < currentNode.getData()) {
                    heightCounter += 1;
                    if (currentNode.getLeftChild() == null) {
                        /*this rows makes a child, update it height and its father*/
                        currentNode.setLeftChild(new AvlNode(newValue));
                        currentNode.getLeftChild().setHeight(heightCounter);
                        currentNode.getLeftChild().setParent(currentNode);
                        currentNode.setChildCount(1);
                        /*call the balancer*/
                        balanceTree(currentNode);
                        break;
                    }
                    /*Move deeper left*/
                    currentNode = currentNode.getLeftChild();
                }
                /*Check right position*/
                else if (newValue > currentNode.getData()) {
                    heightCounter += 1;
                    if (currentNode.getRightChild() == null) {
                        /*this rows makes a child, update child height its father
                        and update the father childCount*/
                        currentNode.setRightChild(new AvlNode(newValue));
                        currentNode.getRightChild().setHeight(heightCounter);
                        currentNode.getRightChild().setParent(currentNode);
                        currentNode.setChildCount(1);
                        /*call the balancer*/
                        balanceTree(currentNode);
                        break;
                    }
                    /*Move deeper right*/
                    currentNode = currentNode.getRightChild();
                }
                /*Check middle position*/
                else {
                    /*seems like we have a duplicate*/
                    alreadyThere = true;
                }
            }
        }
        /*last check if "alreadythere" flag is up*/
        if(alreadyThere)
            return false;
        else
            return true;
    }


    /*Fix both possible violation left or right and then call to specific methods*/
    private void balanceTree(AvlNode node){
        AvlNode currentBalancedNode = node.getParent();
        int balance;

        /*This loop goes from the insertion point up above all
        ancestors and check violations*/
        while(currentBalancedNode != null){
            balance = getBalance(currentBalancedNode);
            if(balance == 2)
                fixLeftViolation(currentBalancedNode);
            if(balance == -2)
                fixRightViolation(currentBalancedNode);
            /*Go up in the tree*/
            currentBalancedNode = currentBalancedNode.getParent();
        }
    }

    private int getBalance(AvlNode node){
        if(node == null)
            return 0;
        return HeightTree(node.getLeftChild()) - HeightTree(node.getRightChild());
    }

    /*this method calculate the height of tree based of given root/node
    * this way we can check right and left subtrees for rotations purposes*/
    private int HeightTree(AvlNode node){
        if(node == null)
            return 0;
        else{
            return 1 + Math.max(HeightTree(node.getLeftChild()),HeightTree(node.getRightChild()));
        }
    }

    /*this method deal with left violation*/
    private void fixLeftViolation(AvlNode node){
        AvlNode child = node.getLeftChild();
        /*check if the violation is "LL" type so 1 right rotation is needed and fast*/
        if(getBalance(child) == 1){
            rotateRight(node);
        }
        /*check if the violation is "LR" type so 2 rotations are needed -
        * 1 left rotation and another right rotation*/
        if(getBalance(child) == -1) {
            rotateLeft(child);
            rotateRight(node);
        }
    }
    /*this method deal with right violation*/
    private void fixRightViolation(AvlNode node){
        AvlNode child = node.getRightChild();
        /*check if the violation is "RR" type so 1 left rotation is needed and fast*/
        if(getBalance(child) == -1){
            rotateLeft(node);
        }
        /*check if the violation is "RL" type so 2 rotations are needed -
        * 1 right rotation and another left rotation*/
        if(getBalance(child) == 1) {
            rotateRight(child);
            rotateLeft(node);
        }
    }

    /*it may get root node or child node*/
    private void rotateRight(AvlNode node){
        int pivot_data = node.getData();
        node.setData(node.getLeftChild().getData());
        if(node.getLeftChild().getChildCount() != 0){
            node.getLeftChild().setData(node.getLeftChild().getLeftChild().getData());
            node.getLeftChild().setLeftChild(null);}
        else
            node.setLeftChild(null);
        AvlNode pivotNode = new AvlNode(pivot_data);
        node.setRightChild(pivotNode);
        pivotNode.setParent(node);
        pivotNode.setHeight(node.getHeight() + 1);
    }

    /*it may get root node or child node*/
    private void rotateLeft(AvlNode node){
        int pivot_data = node.getData();
        node.setData(node.getRightChild().getData());
        if(node.getRightChild().getChildCount() != 0){
            node.getRightChild().setData(node.getRightChild().getRightChild().getData());
            node.getRightChild().setRightChild(null);}
        else
            node.setRightChild(null);
        AvlNode pivotNode = new AvlNode(pivot_data);
        node.setLeftChild(pivotNode);
        pivotNode.setParent(node);
        pivotNode.setHeight(node.getHeight() + 1);
    }


    /*Deletion of a node, checks number of sons and act accordingly*/
    public boolean delete(int toDelete) {
        AvlNode toDeleteObject;
        AvlNode child;
        /*If value does not exist*/
        if (contains(toDelete) == -1)
            return false;
        else {
            /*Check status of object*/
            toDeleteObject = ObjectByVal(toDelete);
            String status = getStatus(toDeleteObject); /*Check if root, inner or leaf*/
            /*deal with it accordingly*/
            end: while (Checking)
                switch (status) {
                    case "no-sons": /*same as leaf*/
                        if (toDeleteObject == this.root) {
                            this.root = null;
                            break end;
                        }
                        if (IamTheLeftChild(toDeleteObject).equals("left"))
                            toDeleteObject.getParent().setLeftChild(null);
                        else
                            toDeleteObject.getParent().setRightChild(null);
                        break end;
                    case "one-son": /*inner or root*/
                        if (IamTheLeftChild(toDeleteObject).equals("left")) {
                            if (toDeleteObject.getRightChild() == null) {
                                child = toDeleteObject.getLeftChild();
                                toDeleteObject.getParent().setLeftChild(child);
                            } else {
                                child = toDeleteObject.getRightChild();
                                toDeleteObject.getParent().setLeftChild(child);
                            }
                        } else if (IamTheLeftChild(toDeleteObject).equals("right")) {
                            if (toDeleteObject.getLeftChild() == null) {
                                child = toDeleteObject.getRightChild();
                                toDeleteObject.getParent().setRightChild(child);
                            } else {
                                child = toDeleteObject.getLeftChild();
                                toDeleteObject.getParent().setRightChild(child);
                            }
                        } else /*if we are dealing with the root*/ {
                            if (toDeleteObject.getLeftChild() == null) {
                                this.root = toDeleteObject.getRightChild();
                            } else {
                                this.root = toDeleteObject.getLeftChild();
                            }
                        }
                        break end;
                    case "two-sons": /*inner or root*/
                        AvlNode successor;
                        for (int data : this) {
                            /*find the successor*/
                            if (data > toDelete) {
                                /*Deal with successor*/
                                successor = ObjectByVal(data);
                                toDeleteObject.setData(data);
                                toDeleteObject = successor;
                                status = "one-son";
                                break;
                            }
                        }
                        break;
                }
        }
        return true;
    }


    /*This Method return the object of the node by its value*/
    private AvlNode ObjectByVal(int value){
        AvlNode currentNode = this.getRoot();
        while(currentNode != null){
            if(value == currentNode.getData()){
                /*status checker - checks if it an inner node
                * or a leaf*/
                if(currentNode.getLeftChild() == null && currentNode.getRightChild() == null)
                    currentNode.setStatus(2);
                return currentNode;
            }
            if(value > currentNode.getData()){
                currentNode = currentNode.getRightChild();
            }
            if(value < currentNode.getData()){
                currentNode = currentNode.getLeftChild();
            }
        }
        /*it will never get to this point because we checked already,
        * that this node already exists*/
        return null;
    }

    /*returns the child side according to parent*/
    private String IamTheLeftChild(AvlNode todelete){
        /*check if its the root*/
        if(todelete.equals(this))
            return "root";
        if(todelete.getParent().getData() > todelete.getData())
            return "left";
        else
            return "right";
    }

    /*gets status of node*/
    private String getStatus(AvlNode node){
        /*if its a leaf*/
        if(node.getChildCount() == 0)
            return "no-sons";
        /*inner or root*/
        if(node.getChildCount() == 1)
            return "one-son";
        /*inner or root*/
        if(node.getChildCount() == 2)
            return "two-sons";
        return null;
    }

    /*return 0 if the search val is root or returns height of other object which is matched,
    * if none of them true return -1*/
    public int contains(int searchVal) {
        int objectCounter = 0;
        /*Check if it equals to root data value*/
        if(this.root != null) {
            if (searchVal == this.getRoot().getData())
                return 0;
            /*checks if value is deeper in the tree - inner node or leaf*/
            for (int data : this) {
                if (data == searchVal) {
                    return this.ObjectByVal(searchVal).getHeight();
                }
                objectCounter += 1;
            }
        }
        return -1;
    }

    /*methods that yield a new iterator object*/
    public Iterator<Integer> iterator() {
        InOrderIterator avlIterator = new InOrderIterator(this.root);
        return avlIterator;
    }
}
