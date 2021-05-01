import java.util.*;

public class PalindromeTest {
     public static void main(String[] args) {        
          String input1 = "abc";
          System.out.println(input1);
          System.out.println("Palindrome(Stack) : " + checkPalindromeStack(input1));
          System.out.println("Palindrome(Queue) : " + checkPalindromeQueue(input1));
          System.out.println("Palindrome(Stack&Queue) : " + checkPalindromeStackAndQueue(input1));
          System.out.println();

          String input2 = "refer";
          System.out.println(input2);
          System.out.println("Palindrome(Stack) : " + checkPalindromeStack(input2));
          System.out.println("Palindrome(Queue) : " + checkPalindromeQueue(input2));
          System.out.println("Palindrome(Stack&Queue) : " + checkPalindromeStackAndQueue(input2));
          System.out.println();
          
          String input3 = "Madam";
          System.out.println(input3);
          System.out.println("Palindrome(Stack) : " + checkPalindromeStack(input3));
          System.out.println("Palindrome(Queue) : " + checkPalindromeQueue(input3));
          System.out.println("Palindrome(Stack&Queue) : " + checkPalindromeStackAndQueue(input3));
          System.out.println();    
     }

     public static boolean checkPalindromeStack(String input){
          LinkedStack<Character> stack1 = new LinkedStack<>();
          LinkedStack<Character> stack2 = new LinkedStack<>();
          // Creating Stack of Chars
          int len = input.length();
          for (int i = 0; i < len / 2; i++) {
               stack1.push(input.charAt(i));
               stack2.push(input.charAt(len - i - 1));
          }

          while (stack1.size() != 0 && stack2.size() != 0) {
               char char1 = Character.toLowerCase(stack1.pop().charValue());
               char char2 = Character.toLowerCase(stack2.pop().charValue());
               if (char1 != char2) return false;
          }
          return true;
     }

     public static boolean checkPalindromeQueue(String input){
          LinkedQueue<Character> queue1 = new LinkedQueue<>();
          LinkedQueue<Character> queue2 = new LinkedQueue<>();
          // Creating Queues of Chars
          int len = input.length();
          for (int i = 0; i < len / 2; i++) {
               queue1.enqueue(input.charAt(i));
               queue2.enqueue(input.charAt(len - i - 1));
          }

          while (queue1.size() != 0 && queue2.size() != 0) {
               char char1 = Character.toLowerCase(queue1.dequeue().charValue());
               char char2 = Character.toLowerCase(queue2.dequeue().charValue());
               if (char1 != char2) return false;
          }
          return true;
     }

     public static boolean checkPalindromeStackAndQueue(String input){
          LinkedStack<Character> stack = new LinkedStack<>();
          LinkedQueue<Character> queue = new LinkedQueue<>();
          // Creating Stack and Queue
          int len = input.length();
          for (int i = 0; i < len; i++) {
               if (i < len / 2) {
                    stack.push(input.charAt(i));
               } else {
                    queue.enqueue(input.charAt(i));
               }
          }
          // remove middle char if odd sequence
          if (len % 2 != 0) queue.dequeue();

          while (stack.size() != 0 && queue.size() != 0) {
               char char1 = Character.toLowerCase(stack.pop().charValue());
               char char2 = Character.toLowerCase(queue.dequeue().charValue());
               // System.out.printf("Stack & Queue: %c // %c\r\n", char1, char2);
               if (char1 != char2) return false;
          }
          return true;
     }
}
