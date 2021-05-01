public class CircularQueue<E> {
  private Node<E> tail;
  private int size;
  private final int MAX_SIZE = 26;
  public CircularQueue() {
    this.size = 0;
  }

  public E enqueue(E e) {
    if (this.size >= 26) return null;
    Node<E> newNode = new Node<>(e, null);
    if (this.tail == null) {
      newNode.setNext(newNode);
    } else {
      newNode.setNext(this.tail.getNext());
      this.tail.setNext(newNode);
    }
    this.tail = newNode;
    this.size++;
    return e;
  };

  public E dequeue() {
    if (this.tail == null) return null;
    E e = this.tail.getNext().getElement();
    this.tail.setNext(this.tail.getNext().getNext());
    this.size--;
    if (this.size <= 0) this.tail = null;
    return e;
  };

  public E rear() {
    if (this.tail == null) return null;
    return this.tail.getElement();
  }
  public E front() {
    if (this.tail == null) return null;
    return this.tail.getNext().getElement();
  }

  public int size() { return this.size; }
  public int MAX_SIZE() { return this.MAX_SIZE; }
}
