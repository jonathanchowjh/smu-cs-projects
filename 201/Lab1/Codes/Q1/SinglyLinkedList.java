import java.util.*;

public class SinglyLinkedList<E extends Comparable<E>> {
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    private static class Node<E> {
        private E element;
        private Node<E> next;
    
        public Node(E e, Node<E> n){
            element = e;
            next = n;
        }
    
        public E getElement(){
            return element;
        }
    
        public Node<E> getNext(){
            return next;
        }
    
        public void setNext(Node<E> n){
            next = n;
        }
    }

    public SinglyLinkedList(){

    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public E first(){
        if (isEmpty()){
            return null;
        } 
        return head.getElement();
    }

    public E last(){
        if (isEmpty()){
            return null;
        }
        return tail.getElement();
    }

    public void addFirst(E e){
        head = new Node<>(e, head);

        if (isEmpty()){
            tail = head;
        }
        size++;
    }

    public void addLast(E e){
        Node<E> newest = new Node<>(e, null);
        if (isEmpty()){
            head = newest;
        } else {
            tail.setNext(newest);
        }
        tail = newest;
        size++;
    }

    public E removeFirst(){
        if (isEmpty()){
            return null;
        }

        E answer = head.getElement();
        head = head.getNext();
        size--;

        if (isEmpty()){
            tail = null;
        }
        return answer;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        Node<E> current = head;
        while (current != null) {
            sb.append(current.getElement());
            sb.append(" ");
            current = current.getNext();
        }
        return sb.toString();
    }

    public void swap() {
        Node<E> prev = this.head;
        Node<E> current = this.head;
        
        Node<E> biggestPrev = null;
        Node<E> smallestPrev = null;
        Node<E> biggest = this.head;
        Node<E> smallest = this.head;
        while (current != null) {
            if (current == this.head) {
                current = current.getNext();
                continue;
            }
            int compareBiggest = current.getElement().compareTo(biggest.getElement());
            int compareSmallest = current.getElement().compareTo(smallest.getElement());
            if (compareBiggest > 0) {
                biggest = current;
                biggestPrev = prev;
            }
            if (compareSmallest < 0) {
                smallest = current;
                smallestPrev = prev;
            }
            prev = prev.getNext();
            current = current.getNext();
        }

        if (smallestPrev != null) smallestPrev.setNext(biggest);
        if (biggestPrev != null) biggestPrev.setNext(smallest);
        Node<E> smallestNext = smallest.getNext();
        smallest.setNext(biggest.getNext());
        biggest.setNext(smallestNext);
        if (smallest == this.tail) this.tail = biggest;
        if (biggest == this.tail) this.tail = smallest;
        if (smallest == this.head) this.head = biggest;
        if (biggest == this.head) this.head = smallest;
    }
   
}

