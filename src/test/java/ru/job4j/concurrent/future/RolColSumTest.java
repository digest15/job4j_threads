package ru.job4j.concurrent.future;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {

    private static final int SIZE = 1000;

    private static int[][] matrics;

    @BeforeAll
    public static void initMatrix() {
        matrics = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrics[i][j] = j;
            }
        }
    }

    @Test
    public void whenCountSum() {
        long start = System.currentTimeMillis();
        RolColSum.Sums[] sums = RolColSum.sum(matrics);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void whenCountSumAsync() throws Exception {
        long start = System.currentTimeMillis();
        RolColSum.Sums[] sums = RolColSum.asyncSum(matrics);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}