package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class ParallelIndexSearcherTest {
    @Test
    public void whenSearchInSmallArray() {
        int expectedIndex = 4;
        Integer[] array = IntStream.range(0, 8).boxed().toArray(Integer[]::new);
        int index = ParallelIndexSearcher.indexOf(array, expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchInHugeArray() {
        int expectedIndex = 50001;
        Integer[] array = IntStream.range(0, 100000).boxed().toArray(Integer[]::new);
        int index = ParallelIndexSearcher.indexOf(array, expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchAbsentValue() {
        int expectedIndex = -1;
        Integer[] array = IntStream.range(0, 8).boxed().toArray(Integer[]::new);
        int index = ParallelIndexSearcher.indexOf(array, expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchInStringArray() {
        int expectedIndex = 3;
        String[] array = IntStream.range(0, 8).mapToObj(String::valueOf).toArray(String[]::new);
        int index = ParallelIndexSearcher.indexOf(array, String.valueOf(expectedIndex));
        assertThat(index).isEqualTo(expectedIndex);
    }
}