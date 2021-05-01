import java.util.*;

public class StackTest {
   public static void main(String[] args) {        
        ArrayStack<Integer> stack = new ArrayStack<>();
     
        Integer[] input = {1, 4, 2, 5, 3};

        for (Integer i : input){
             stack.push(i);
        }
  
        System.out.println("Before Sorting :");
        System.out.println(stack);
        
        sort(stack);
        System.out.println("After Sorting :");
        System.out.println(stack);
   }    

     public static void sort(Stack<Integer> stack){
          if (!stack.isEmpty()) {
               Integer temp = stack.pop();
               sort(stack);
               sortAndPush(stack, temp);
          }
     }

     public static void sortAndPush(Stack<Integer> stack, Integer element) {
          if (stack.isEmpty() || element.intValue() > stack.top().intValue()) {
               stack.push(element);
          } else {
               Integer temp = stack.pop();
               sortAndPush(stack, element);
               stack.push(temp);
          }
     }
}
