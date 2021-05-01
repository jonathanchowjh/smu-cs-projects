public class SinglyLinkedList<E> {
	
	private Node<E> head = null;
	private Node<E> tail = null;
	private int size = 0;

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

	// Write your codes below
	public String toString(){
		String str = "";
		Node<E> current = head;
		while (current != null) {
			str += current.getElement() + ", ";
			current = current.getNext();
		}
		return str;
	}

	public E removeLast(){
		return head.getElement();
	}

	public void reverse(){       
				 
	}

	public void add(E e){
		
	}

	public void trim(int count){
	   
	}
}