package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class threader {
	public static void main(String[] args) {
        int[] array = generateRandomArray(200000000, 1, 10);
        
        // Parallel Array Sum
        long startParallel = System.currentTimeMillis();
        int parallelSum = parallelArraySum(array, 4);  // Adjust the number of threads as needed
        long endParallel = System.currentTimeMillis();
        long parallelTime = endParallel - startParallel;

        // Single Thread Array Sum
        long startSingleThread = System.currentTimeMillis();
        int singleThreadSum = singleThreadArraySum(array);
        long endSingleThread = System.currentTimeMillis();
        long singleThreadTime = endSingleThread - startSingleThread;

        // Display the results
        System.out.println("Parallel Sum: " + parallelSum);
        System.out.println("Parallel Time: " + parallelTime + " milliseconds");
        System.out.println("Single Thread Sum: " + singleThreadSum);
        System.out.println("Single Thread Time: " + singleThreadTime + " milliseconds");
    }

    private static int[] generateRandomArray(int size, int minValue, int maxValue) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }
        return array;
    }

    private static int parallelArraySum(int[] array, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();
        int chunkSize = array.length / numThreads;

        // Divide the array into chunks and assign each chunk to a Callable task
        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? array.length : startIndex + chunkSize;
            int[] chunk = new int[endIndex - startIndex];
            System.arraycopy(array, startIndex, chunk, 0, chunk.length);
            Callable<Integer> task = () -> {
                int sum = 0;
                for (int num : chunk) {
                    sum += num;
                }
                return sum;
            };
            futures.add(executor.submit(task));
        }

        // Compute the sum of each chunk in parallel
        int sum = 0;
        for (Future<Integer> future : futures) {
            try {
                sum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return sum;
    }

    private static int singleThreadArraySum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }
}
