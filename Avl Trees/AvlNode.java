package oop.ex4.data_structures;

/**
 * Created by Harel on 14/05/2017.
 */
public class AvlNode {

    /*Fields*/
    private int data;
    private int balanceFactor;
    private AvlNode RightChild;
    private AvlNode LeftChild;
    private AvlNode parent;
    private int height;
    private int status; /*0 - stand for root, 1 - for inner and 2 - for leaf*/
    private int childCount;



    /*using this to initialize the specific object that is
            * currently instantiated*/
    public AvlNode(int value)
    {
      data = value;
      LeftChild = null;
      RightChild = null;
      balanceFactor = 0;
      height = 0;
      status = 1; /*Make it an inner by default*/
      parent = null;
      childCount = 0;

    }

    /*Gets the parent node of a certain node*/
    public AvlNode getParent() {
        return parent;
    }

    /*set value for parent node*/
    public void setParent(AvlNode parent) {
        this.parent = parent;
    }

    /*get number of children from certain node*/
    public int getChildCount() {
        return childCount;
    }

    /*setting child value*/
    public void setChildCount(int childCount) {
        this.childCount = this.childCount + childCount;
    }

    /*Setting status of a node*/
    public void setStatus(int status) {
        this.status = status;
    }

    /*gets data related to node*/
    public int getData() {
        return data;
    }

    /*Sets node data*/
    public void setData(int data) {
        this.data = data;
    }

    /*gets right child*/
    public AvlNode getRightChild() {
        return RightChild;
    }

    /*set right child*/
    public void setRightChild(AvlNode rightChild) {
        RightChild = rightChild;
    }

    /*gets left child*/
    public AvlNode getLeftChild() {
        return LeftChild;
    }

    /*sets left child*/
    public void setLeftChild(AvlNode leftChild) {
        LeftChild = leftChild;
    }

    /*gets height of a node*/
    public int getHeight() {
        return height;
    }

    /*Setting new height to node*/
    public void setHeight(int height) {
        this.height = height;
    }

}
