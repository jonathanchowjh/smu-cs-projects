public class SinglyLinkedListTest {
   public static void main(String[] args) {
        SinglyLinkedList<Integer> sll = new SinglyLinkedList<>();

        System.out.println("Add - 3");
        sll.add(3);
        display(sll);

        System.out.println("Add - 1");
        sll.add(1);
        display(sll);
        
        System.out.println("Add - 2");
        sll.add(2);
        display(sll);
        
        sll = new SinglyLinkedList<Integer>();
        System.out.println("Add - 1, 2, 3");
        sll.add(1);
        sll.add(2);
        sll.add(3);
        display(sll);

        sll = new SinglyLinkedList<Integer>();
        System.out.println("Add - 3, 2, 1");
        sll.add(3);
        sll.add(2);
        sll.add(1);
        display(sll);

        sll = new SinglyLinkedList<Integer>();
        System.out.println("Add - 2, 1, 4, 3, 5");
        sll.add(2);
        sll.add(1);
        sll.add(4);
        sll.add(3);
        sll.add(5);
        display(sll);

        System.out.println("Trim - 6"); 
        sll.trim(6);
        display(sll);
        System.out.println("Size :" + sll.size());
        System.out.println();

        System.out.println("Trim - 3"); 
        sll.trim(3);
        display(sll);
        System.out.println("Size :" + sll.size());
        System.out.println();
   }    

   public static void display(SinglyLinkedList sll){
      System.out.println("Linked List : " + sll);
      System.out.println("First Element : " + sll.first());
      System.out.println("Last Element : " + sll.last());
      System.out.println();
     }
}