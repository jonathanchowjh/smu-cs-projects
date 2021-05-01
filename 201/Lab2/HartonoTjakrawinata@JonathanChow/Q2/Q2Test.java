import java.util.*;
public class Q2Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     }

     // write your codes here

     public static String verify(String traversal1, String traversal2, String traversal3){
          ArrayList<Integer> inorder = null;
          ArrayList<Integer> preorder = null;
          ArrayList<Integer> postorder = null;
          String[] traversals = {traversal1, traversal2, traversal3};

          // DECIDE which is INORDER (sorted check)
          for (int i = 0; i < traversals.length; i++) {
               ArrayList<Integer> temp = processTraversal(traversals[i]);
               if (inorder == null && inOrder(temp)) {
                    inorder = temp;
                    traversals[i] = "Inorder";
               } else if (preorder == null) {
                    preorder = temp;
                    traversals[i] = "Preorder";
               } else if (postorder == null) {
                    postorder = temp;
                    traversals[i] = "Postorder";
               }
          }
          if (inorder == null || preorder == null || postorder == null) return "Invalid traversals";
          if ((inorder.size() != preorder.size()) || (preorder.size() != postorder.size()) || inorder.size() <= 0) return "Invalid traversals";

          // DECIDE which is PREORDER and which is POSTORDER (ROOT LOCATION MATCHING)
          if (postorder.get(postorder.size() - 1) != preorder.get(0)) {
               // if (preorder.get(0) != inorder.get(0)) return "Invalid traversals";
               ArrayList<Integer> temp = preorder;
               preorder = postorder;
               postorder = temp;
               for (int i = 0; i < traversals.length; i++) {
                    if (traversals[i].equals("Preorder")) {
                         traversals[i] = "Postorder";
                    } else if (traversals[i].equals("Postorder")) {
                         traversals[i] = "Preorder";
                    }
               }
          }

          // CALL TREE MATCH RECURSION
          boolean checkTree = checktrees(inorder, 0, preorder, 0, postorder, 0, inorder.size());
          if (!checkTree) return "Invalid traversals";
          
          // PRINT ANSWER
          StringBuilder buffer = new StringBuilder();
          buffer.append("Traversal 1 - ");
          buffer.append(traversals[0]);
          buffer.append(", Traversal 2 - ");
          buffer.append(traversals[1]);
          buffer.append(", Traversal 3 - ");
          buffer.append(traversals[2]);
          return buffer.toString();
     }

     public static boolean checktrees(ArrayList<Integer> inorder, int ino, ArrayList<Integer> preorder, int pre, ArrayList<Integer> postorder, int pos, int len) {
          if (len <= 0) return true;
          if (len == 1) return (inorder.get(ino) == preorder.get(pre)) && (preorder.get(pre) == postorder.get(pos));

          // finding current node of inorder traversal (using preorder)
          int curr = -1;
          for (int i = ino; i < ino + len; i++) {
               if (inorder.get(i) == preorder.get(pre)) {
                    curr = i - ino;
                    break;
               }
          }
          if (curr == -1) return false;
          
          // Check left and right node
          boolean left = checktrees(inorder, ino, preorder, pre + 1, postorder, pos, curr);
          boolean right = checktrees(inorder, ino + curr + 1, preorder, pre + curr + 1, postorder, pos + curr, len - curr - 1);

          return (left && right);
     }

     public static boolean inOrder(ArrayList<Integer> nums) {
          for (int i = 1; i < nums.size(); i++) {
               if (nums.get(i - 1) >= nums.get(i)) return false;
          }
          return true;
     }
     
     public static ArrayList<Integer> processTraversal(String nums) {
          ArrayList<Integer> buffer = new ArrayList<>();
          for (String num : nums.split("-")) {
               buffer.add(Integer.parseInt(num));
          }
          return buffer;
     }
}