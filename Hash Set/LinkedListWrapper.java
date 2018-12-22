/**
 * Created by Harel on 05/05/2017.
 */

import java.util.LinkedList;

/*This trick like any other design pattern tricks to overcome obstacles in code.
* this one deal with the problem of making an array of LinkedList type - be careful don't judge a book by its cover :)*/
public class LinkedListWrapper extends LinkedList<String>{
    /*The constructor make an object that include a object*/
    public LinkedListWrapper(){
        LinkedList<String> object = new LinkedList<String>();
    }
}
