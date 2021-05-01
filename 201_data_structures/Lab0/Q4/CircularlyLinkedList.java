public class CircularlyLinkedList<E> {
    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList(){
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
        return tail.getNext().getElement();
    }

    public E last(){
        if (isEmpty()){
            return null;
        }
        return tail.getElement();
    }

    public void rotate(){
        if (tail != null){
            tail = tail.getNext();
        }
    }
    
    public void addFirst(E e){
        if (isEmpty()){
            tail = new Node<>(e, null);
            tail.setNext(tail);
        } else {
            Node<E> newest = new Node<>(e, tail.getNext());
            tail.setNext(newest);
        }
        size++;
    }

    public void addLast(E e){
        addFirst(e);
        tail = tail.getNext();
    }

    public E removeFirst(E e){
        if (isEmpty()){
            return null;
        } 
        Node<E> head = tail.getNext();
        if (head == tail){
            tail = null;
        } else {
            tail.setNext(head.getNext());
        }
        size--;
        return head.getElement();
    }

    public String toString(){
        String s = "";
        Node<E> current = this.tail;
        current = current.getNext();
        while (current != this.tail) {
            s += current.getElement() + ", ";
            current = current.getNext();
        }
        s += current.getElement() + ", ";
        return s;
    }

    public E removeLast(){
        Node<E> current = this.tail;
        while (current.getNext() != this.tail) {
            current = current.getNext();
        }
        current.setNext(this.tail.getNext());
        this.tail = current;
        return this.tail.getElement();
    }

    public void process(int count){
        Node<E> current = this.tail.getNext();
        for (int i = 0; i < count - 1; i++) {
            System.out.printf("%d", current.getElement());
            current = current.getNext();
        }
        this.tail = current;
    }
    
}