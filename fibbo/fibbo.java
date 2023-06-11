package fibbo;

import java.util.HashMap;
import java.util.Map;

public class fibbo {
  private static Map<Integer, Long> recursiveResults = new HashMap<>();
    private static Map<Integer, Long> iterativeResults = new HashMap<>();

    public static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        }
    }

    public static long fibonacciIterative(int n) {
        if (n <= 1) {
            return n;
        }

        long fibMinus2 = 0;
        long fibMinus1 = 1;
        long fibCurrent = 0;

        for (int i = 2; i <= n; i++) {
            fibCurrent = fibMinus1 + fibMinus2;
            fibMinus2 = fibMinus1;
            fibMinus1 = fibCurrent;
        }

        return fibCurrent;
    }

    public static void main(String[] args) {
        int maxIndex = 30; // Maximum index to calculate Fibonacci number for

        System.out.println("Calculating Fibonacci results...\n");

        System.out.println("Index\tRecursive\tIterative");
        System.out.println("------------------------------");

        for (int i = 0; i <= maxIndex; i++) {
            long startTime = System.nanoTime();
            fibonacciRecursive(i);
            long endTime = System.nanoTime();
            long recursiveTime = endTime - startTime;
            recursiveResults.put(i, recursiveTime);

            startTime = System.nanoTime();
            fibonacciIterative(i);
            endTime = System.nanoTime();
            long iterativeTime = endTime - startTime;
            iterativeResults.put(i, iterativeTime);

            System.out.printf("%d\t%d ns\t\t%d ns\n", i, recursiveTime, iterativeTime);
        }
    }
}