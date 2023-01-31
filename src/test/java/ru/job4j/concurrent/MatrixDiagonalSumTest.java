package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.future.MatrixDiagonalSum;

import java.util.Arrays;
import java.util.stream.IntStream;

class MatrixDiagonalSumTest {
    @Test
    public void initMatrix() throws Exception {
        int count = 10;
        int[][] matrics = new int[count][count];
        IntStream.range(1, count)
                .forEach(i -> matrics[i][i] = i);

        Arrays.stream(MatrixDiagonalSum.asyncSum(matrics))
                .forEach(System.out::println);
    }
}