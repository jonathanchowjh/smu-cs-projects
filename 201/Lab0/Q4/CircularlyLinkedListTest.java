public class CircularlyLinkedListTest {
   public static void main(String[] args) {
       CircularlyLinkedList<Integer> cll = new CircularlyLinkedList<Integer>();

       System.out.println("Add First - 1, 2, 3");
       cll.addFirst(1);
       cll.addFirst(2);
       cll.addFirst(3);
       display(cll);

       System.out.println("Add Last - 4, 5");
       cll.addLast(4);
       cll.addLast(5);
       display(cll);
       
       System.out.println("Remove Last");
       cll.removeLast();
       display(cll);
       
       System.out.println("Process 10 elements");
       cll.process(10);
       System.out.println();
       display(cll);
   } 

   public static void display(CircularlyLinkedList<Integer> cll){
    System.out.println("Linked List : " + cll);
    System.out.println("First Element : " + cll.first());
    System.out.println("Last Element : " + cll.last());
    System.out.println();
   }
}