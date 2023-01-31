package ru.job4j.concurrent.future;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public record Sums(int rowSum, int colSum) { }

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int k = i;
            sums[i] = new Sums(
                    sum(matrix, j -> j, j -> k),
                    sum(matrix, j -> k, j -> j)
            );
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Map<Integer, CompletableFuture<Sums>> futureMap = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            int k = i;
            futureMap.put(
                    i,
                    CompletableFuture.supplyAsync(() -> sum(matrix, j -> j, j -> k))
                            .thenCombine(
                                    CompletableFuture.supplyAsync(() -> sum(matrix, j -> k, j -> j)),
                                    Sums::new
                            )
            );
        }
        Sums[] sums = new Sums[matrix.length];
        for (Integer key : futureMap.keySet()) {
            sums[key] = futureMap.get(key).get();
        }
        return sums;
    }

    private static int sum(int[][] matrix, IntToIntFunction row, IntToIntFunction col) {
        int sum = 0;
        for (int j = 0; j < matrix.length; j++) {
            sum += matrix[col.apply(j)][row.apply(j)];
        }
        return sum;
    }

    @FunctionalInterface
    private interface IntToIntFunction {
        int apply(int value);
    }
}
