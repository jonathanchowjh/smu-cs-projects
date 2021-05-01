import java.util.*;

public class NumbersArray {
    public static void main(String[] args) {
        Integer[] input = {1,2,3,4,1,5};

        System.out.println("Input : " + Arrays.toString(input));
        System.out.println("Max number : " + findMax(input));
        System.out.println("Duplicate number : " + findDuplicate(input));
        System.out.println("Duplicate numbers : " + Arrays.toString(findDuplicates(input)));
        System.out.println("Unique numbers : " + Arrays.toString(findUnique(input)));       
        
        System.out.println();

        input = new Integer[] {1,2,3,4,1,2,5,3};

        System.out.println("Input : " + Arrays.toString(input));
        System.out.println("Max number : " + findMax(input));
        System.out.println("Duplicate number : " + findDuplicate(input));
        System.out.println("Duplicate numbers : " + Arrays.toString(findDuplicates(input)));
        System.out.println("Unique numbers : " + Arrays.toString(findUnique(input))); 
    }

    // Write your methods here
    public static int findMax(Integer[] input) {
        if (input.length < 1) return 0;
        int max = input[0];
        for (int i = 1; i < input.length; i++) {
            if (input[i] > max) max = input[i];
        }
        return max;
    }
    public static int findDuplicate(Integer[] input) {
        for (int i = 1; i < input.length; i++) {
            for (int j = 0; j < i; j++) {
                if (input[i] == input[j]) return input[i];
            }
        }
        return 0;
    }
    public static int[] findDuplicates(Integer[] input) {
        int[] duplicates = new int[input.length];
        int counter = 0;
        for (int i = 1; i < input.length; i++) {
            for (int j = 0; j < i; j++) {
                if (input[i] == input[j]) duplicates[counter++] = input[i];
            }
        }
        return Arrays.copyOfRange(duplicates, 0, counter);
    }
    public static int[] findUnique(Integer[] input) {
        int[] duplicates = new int[input.length];
        int[] uniques = new int[input.length];
        int counter = 0;
        int counterU = 0;
        for (int i = 1; i < input.length; i++) {
            for (int j = 0; j < i; j++) {
                if (input[i] == input[j]) duplicates[counter++] = input[i];
            }
        }
        for (int i = 0; i < input.length; i++) {
            boolean duplicate = false;
            for (int j = 0; j < counter; j++) {
                if (input[i] == duplicates[j]) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                uniques[counterU++] = input[i];
            }
        }
        return Arrays.copyOfRange(uniques, 0, counterU);
    }
}