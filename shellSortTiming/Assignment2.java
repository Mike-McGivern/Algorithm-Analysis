import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class Assignment2 {

   public static void main(String args[]) {
      int[] testArr = generateRandomArray(20);
      int[] testArrCpy = Arrays.copyOf(testArr, testArr.length);
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
         
      System.out.println("insertion sort test");
      insertionSort(testArrCpy);
      printArray(testArrCpy);
      
      testArrCpy = Arrays.copyOf(testArr, testArr.length);
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 1 test");
      shellSort1(testArrCpy);
      printArray(testArrCpy);
      
      testArrCpy = Arrays.copyOf(testArr, testArr.length);         
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 2 test");
      shellSort2(testArrCpy);
      printArray(testArrCpy);
         
      testArrCpy = Arrays.copyOf(testArr, testArr.length);   
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 3 test");
      shellSort3(testArrCpy);
      printArray(testArrCpy);
      
      testArrCpy = Arrays.copyOf(testArr, testArr.length);
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 4 test");
      shellSort4(testArrCpy);
      printArray(testArrCpy);
      
      testArrCpy = Arrays.copyOf(testArr, testArr.length);
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 5 test");
      shellSort5(testArrCpy);
      printArray(testArrCpy);
      
      testArrCpy = Arrays.copyOf(testArr, testArr.length);
      System.out.println("Unsorted test Array");
      printArray(testArrCpy);
      System.out.println("Shell sort 6 test");
      shellSort6(testArrCpy);
      printArray(testArrCpy);
      
      final int[] timingArr = generateRandomArray(1650000); 
      String[] sortNames = {"Insertion", "Shell1", "Shell2", "Shell3", "Shell4", "Shell5", "Shell6"};
      int trials = 3;
      double[][] timings = new double[sortNames.length][trials];
      for (int i = 0; i < sortNames.length; i++) {
      
         for (int j = 0; j < trials; j++) {
            int[] trialArr = Arrays.copyOf(timingArr, timingArr.length); // fresh copy per trial
            long start = System.nanoTime();
            switch (i) {
               case 0 -> insertionSort(trialArr);
               case 1 -> shellSort1(trialArr);
               case 2 -> shellSort2(trialArr);
               case 3 -> shellSort3(trialArr);
               case 4 -> shellSort4(trialArr);
               case 5 -> shellSort5(trialArr);
               case 6 -> shellSort6(trialArr);
            }
            long end = System.nanoTime();
            timings[i][j] = (end - start) / 1_000_000_000.0; 
         };
      
      }
      
      String[][] results = new String[sortNames.length + 1][trials + 2]; // +2 for name and average
      results[0][0] = "Sort";
      results[0][1] = "Trial 1";
      results[0][2] = "Trial 2";
      results[0][3] = "Trial 3";
      results[0][4] = "Average Time";
      for (int i = 0; i < sortNames.length; i++) {
         results[i + 1][0] = sortNames[i]; // First column: sort name
      
         double sum = 0.0;
         for (int j = 0; j < trials; j++) {
            results[i + 1][j + 1] = String.format("%.3f", timings[i][j]); // Middle columns: timings
            sum += timings[i][j];
         }
      
         double avg = sum / trials;
         results[i + 1][trials + 1] = String.format("%.3f", avg); // Last column: average
      }
      
      System.out.println("Size of Array: "+ timingArr.length);
      print2DArray(results); 
      System.out.println();
      
      
      //Time each of the 7 sorts using differing lengths values for arrays for each sorting method
      // expand the length of the array by 2 until it is 8 times the length of the original
      // store the timing of each run for each sort in a row of a table 
      // create a table for each sort method with a row being a size, three trials of the array and the average
      // have 4 rows total doubling the size each time
      
      int[] initSortLens = {500000, 2000000, 200000, 200000, 2000000, 2000000, 2000000};
      for(int n = 0; n < sortNames.length; n++) {
         System.out.println(sortNames[n]);
         int[] doublingTrialArr = generateRandomArray(initSortLens[n]);
         trials = 3;
         int doublingFreq = 4;    
         double[][] scalingTimings = new double[doublingFreq + 1][trials];  
         ArrayList<Integer> lenArrLst = new ArrayList<>();   
         for (int i = 0; i < doublingFreq; i++) {
            for (int j = 0; j < trials; j++) {
               int[] trialArr = Arrays.copyOf(doublingTrialArr, doublingTrialArr.length); // fresh copy per trial
               long start = System.nanoTime();
               switch (n) {
                  case 0 -> insertionSort(trialArr);
                  case 1 -> shellSort1(trialArr);
                  case 2 -> shellSort2(trialArr);
                  case 3 -> shellSort3(trialArr);
                  case 4 -> shellSort4(trialArr);
                  case 5 -> shellSort5(trialArr);
                  case 6 -> shellSort6(trialArr);
               }
               long end = System.nanoTime();
               scalingTimings[i][j] = (end - start) / 1_000_000_000.0; 
            };
            lenArrLst.add(doublingTrialArr.length);
            doublingTrialArr = generateRandomArray(doublingTrialArr.length * 2);
         }
         String[][] result = new String[lenArrLst.size() + 1][trials + 2]; // +2 for name and average
         result[0][0] = "Size";
         result[0][1] = "Trial 1";
         result[0][2] = "Trial 2";
         result[0][3] = "Trial 3";
         result[0][4] = "Average Time";
         for (int i = 0; i < lenArrLst.size(); i++) {
            result[i + 1][0] = String.valueOf(lenArrLst.get(i)); 
         
            double sum = 0.0;
            for (int j = 0; j < trials; j++) {
               result[i + 1][j + 1] = String.format("%.3f", scalingTimings[i][j]); // Middle columns: timings
               sum += scalingTimings[i][j];
            }
         
            double avg = sum / trials;
            result[i + 1][trials + 1] = String.format("%.3f", avg); // Last column: average
         }
      
         print2DArray(result);
         System.out.println();
      }
   } 
 
   public static void print2DArray(String[][] array) {
      int columns = array[0].length;
      int[] colWidths = new int[columns];
   
    // Step 1: Determine max width for each column
      for (String[] row : array) {
         for (int i = 0; i < columns; i++) {
            if(row[i] != null) 
               colWidths[i] = Math.max(colWidths[i], row[i].length());
         }
      }
   
    // Step 2: Print each row with padded columns
      for (String[] row : array) {
         for (int i = 0; i < columns; i++) {
            System.out.printf("%-" + (colWidths[i] + 2) + "s", row[i]); // +2 for spacing
         }
         System.out.println();
      }
   }
   public static void printArray(int[] arr) {
      for (int num : arr) 
         System.out.print(num + " ");
      System.out.println();
   }
   
   public static int[] generateRandomArray(int size) {
      Random rand = new Random();
      int[] array = new int[size];
      for (int i = 0; i < size; i++) {
         array[i] = rand.nextInt(); // Generates any valid int
      }
      return array;
   }

   public static void insertionSort(int[] arr) {
      for(int i = 1; i < arr.length; i++) {
         int key = arr[i];
         int j = i - 1;
         
         while(j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
         }
         
         arr[j + 1] = key;
      }
   }
   
   public static void shellSort1(int[] arr) {
      int n = arr.length;
   
    // Knuth sequence: h = 1, 4, 13, 40, ... 3n + 1
      int h = 1;
      while (h < n / 3) {
         h = 3 * h + 1;
      }  // creates highest h value that fits in the length of the array
   
      while (h >= 1) {
         for (int i = h; i < n; i++) {
            int temp = arr[i];
            int j = i;
         
            // Insertion sort logic for h-spaced elements
            while (j >= h && arr[j - h] > temp) {
               arr[j] = arr[j - h];
               j -= h;
            }
         
            arr[j] = temp;
         }
      
         h = (h - 1) / 3; // Move to next smaller Knuth gap
      }
   }
   
   public static void shellSort2(int[] arr) {
      int n = arr.length;
      int h = (n / 2) | 1; // ensures h is odd
   
      while (h >= 1) {
         for (int i = h; i < n; i++) {
            int temp = arr[i];
            int j = i;
         
            // Insertion sort logic for h-spaced elements
            while (j >= h && arr[j - h] > temp) {
               arr[j] = arr[j - h];
               j -= h;
            }
         
            arr[j] = temp;
         }
         h -= 2; // move to next smaller odd gap
      }
   }
   
   public static void shellSort3(int[] arr) {
      int n = arr.length;
      int h = n / 2; //setting h's starting point
   
      while (h >= 1) {
         for (int i = h; i < n; i++) {
            int temp = arr[i];
            int j = i;
         
            // Insertion sort logic for h-spaced elements
            while (j >= h && arr[j - h] > temp) {
               arr[j] = arr[j - h];
               j -= h;
            }
         
            arr[j] = temp;
         }
         h--; // h down by one
      }
   }
         
   public static void shellSort4(int[] arr) {
      int n = arr.length;
      int h = n / 2;
   
      while (h > 0) {
        // Perform h-sort using insertion-style logic
         for (int i = h; i < n; i++) {
            int v = arr[i];
            int j = i;
         
            while (j >= h && arr[j - h] > v) {
               arr[j] = arr[j - h];
               j -= h;
            }
            arr[j] = v;
         }
      
         h /= 2; // Move to next smaller gap
      }
   }
   
   public static void shellSort5(int[] arr) {
      int n = arr.length;
      int h = n / 2;
   
      while (h > 1) {
        // Perform h-sort using insertion-style logic
         for (int i = h; i < n; i++) {
            int v = arr[i];
            int j = i;
         
            while (j >= h && arr[j - h] > v) {
               arr[j] = arr[j - h];
               j -= h;
            }
            arr[j] = v;
         }
      
         h = (int) Math.sqrt(h); // Update h using square root
      }
   
    // Final pass with h = 1 (standard insertion sort)
      for (int i = 1; i < n; i++) {
         int v = arr[i];
         int j = i;
      
         while (j >= 1 && arr[j - 1] > v) {
            arr[j] = arr[j - 1];
            j--;
         }
         arr[j] = v;
      }
   }
   
   public static void shellSort6(int[] arr) {
      int n = arr.length;
   
    // Find the largest k such that 2^k - 1 < n
      int k = 1;
      while ((1 << k) - 1 < n) { // bit shifting is more efficient than Math.pow()
         k++;
      }
      k--; // Step back to last valid k
   
    // Apply Shell sort using in-place h-sorting
      while (k >= 1) {
         int h = (1 << k) - 1; // h = 2^k - 1
      
        // Perform h-sort using insertion-style logic
         for (int i = h; i < n; i++) {
            int v = arr[i];
            int j = i;
         
            // Shift elements that are h apart
            while (j >= h && arr[j - h] > v) {
               arr[j] = arr[j - h];
               j -= h;
            }
            arr[j] = v;
         }
      
         k--; // Move to next smaller gap
      }
   }
}