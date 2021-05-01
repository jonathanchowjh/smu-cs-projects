
public class SinglyLinkedListTest {
   public static void main(String[] args) {
        SinglyLinkedList<Integer> sll = new SinglyLinkedList<Integer>();
         
        sll.addFirst(1);
        sll.addFirst(2);
        sll.addFirst(3);
        System.out.println("Before Swap :");
        display(sll);
        sll.swap();
        System.out.println("After Swap :");
        display(sll);
        System.out.println("------------------------");

        sll = new SinglyLinkedList<Integer>();
        sll.addFirst(3);
        sll.addFirst(2);
        sll.addFirst(1);
        sll.addFirst(5);
        System.out.println("Before Swap :");
        display(sll);
        sll.swap();
        System.out.println("After Swap :");
        display(sll);
        System.out.println("------------------------");
        
        sll = new SinglyLinkedList<Integer>();
        sll.addFirst(2);
        sll.addFirst(1);
        sll.addFirst(4);
        sll.addFirst(3);
        sll.addFirst(5);
        System.out.println("Before Swap :");
        display(sll);
        sll.swap();
        System.out.println("After Swap :");
        display(sll);
   }    

   public static <E extends Comparable<E>> void display(SinglyLinkedList<E> sll){
      System.out.println("Linked List : " + sll);
      System.out.println("First Element : " + sll.first());
      System.out.println("Last Element : " + sll.last());
      System.out.println();
     }
}
