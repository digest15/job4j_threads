package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

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