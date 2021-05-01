public class Node<E> {
  private E element;
  private Node<E> next;

  public Node(E e, Node<E> next) {
    this.element = e;
    this.next = next;
  }

  public E getElement() { return this.element; }
  public void setElement(E e) { this.element = e; }
  public Node<E> getNext() { return this.next; }
  public void setNext(Node<E> n) { this.next = n; }
}
