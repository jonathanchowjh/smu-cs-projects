import java.util.*;

public class Q1Test {
     public static void main(String[] args) {
          int numOfIntegers, value;
          String result;

          LinkedList<Integer> input = new LinkedList<>();
          input.add(1);
          input.add(2);
          input.add(3);
          input.add(4);
          input.add(5);
          input.add(6);

          System.out.println("Input : " + input);
          System.out.println();
          
          numOfIntegers = 1;
          value = 7;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : Invalid");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          numOfIntegers = 1;
          value = 3;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : (3)");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          numOfIntegers = 2;
          value = 4;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : (1,3)");
          System.out.println("  Actual Result : " + result);
          System.out.println();
          
          numOfIntegers = 3;
          value = 6;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : (1,2,3)");
          System.out.println("  Actual Result : " + result);
          System.out.println();
          
          numOfIntegers = 2;
          value = 7;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : (1,6), (2,5), (3,4)");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          input = new LinkedList<>();
          input.add(3);
          input.add(1);
          input.add(2);
          input.add(4);

          System.out.println("************************************");
          System.out.println("Input : " + input);
          System.out.println();
          
          numOfIntegers = 2;
          value = 4;
          result = findIntegers(input, numOfIntegers, value);
          System.out.println("Find " + numOfIntegers + " integer(s) that add up to " + value);
          System.out.println("Expected Result : (1,3)");
          System.out.println("  Actual Result : " + result);
          System.out.println();    
     }    

     // write your codes here
     public static String findIntegers(LinkedList<Integer> input, int numOfIntegers, int value){
          if (input.size() < 1) return "Invalid";
          // // Find Biggest
          // int biggest = input.get(0);
          // for (int i = 1; i < input.size(); i++) {
          //      if (biggest < input.get(i)) biggest = input.get(i);
          // }
          // // Put into array (hashing)
          // int[] auxArr = new int[biggest + 1];
          // auxArr[0] = 1; // default 0 to 1
          // for (int i = 0; i < input.size(); i++) {
          //      int temp = input.get(i);
          //      auxArr[temp] = temp;
          // }
          // Sum of all but last array
          LinkedList<Integer> nums = new LinkedList<>();
          LinkedList<String> list = setOfNum(input, numOfIntegers, value, new int[0], nums, -1);
          return list != null ? list.toString().substring(1, list.toString().length() - 1) : "Invalid";
     }
     
     public static LinkedList<String> setOfNum(LinkedList<Integer> input, int count, int value, int[] auxArr, LinkedList<Integer> nums, int start) {
          // BASE CASE
          if (count <= 0) {
               int sum = 0;
               LinkedList<String> collection = new LinkedList<>();
               StringBuilder buffer = new StringBuilder();
               buffer.append("(");
               for (Integer num : nums) {
                    sum += num;
                    buffer.append(String.valueOf(num.intValue()));
                    buffer.append(",");
               }
               buffer.deleteCharAt(buffer.length() - 1);
               buffer.append(")");
               collection.add(buffer.toString());
               // System.out.println("COMPARISON SUM / VALUE : " + sum + " : " + value);
               if (sum == value) return collection;
               return null;
          }
          // RECURSIVE STEP
          LinkedList<String> collection = new LinkedList<>();
          for (int i = start + 1; i < input.size(); i++) { 
               LinkedList<Integer> num_C = (LinkedList<Integer>) nums.clone();
               num_C.add(input.get(i));
               LinkedList<String> result = setOfNum(input, count - 1, value, auxArr, num_C, i);
               if (result != null) {
                    collection.addAll(result);
               }
          }
          // RETURN VALUE
          return collection.size() > 0 ? collection : null;
     }

}
