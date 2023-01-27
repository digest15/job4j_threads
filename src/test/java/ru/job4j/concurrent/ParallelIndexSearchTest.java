package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class ParallelIndexSearchTest {
    @Test
    public void whenSearchInSmallArray() {
        int expectedIndex = 4;
        Integer[] array = IntStream.range(0, 8).boxed().toArray(Integer[]::new);
        ParallelIndexSearcher<Integer> indexSearcher = new ParallelIndexSearcher<>(new ForkJoinPool(), array);
        int index = indexSearcher.indexOf(expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchInHugeArray() {
        int expectedIndex = 50001;
        Integer[] array = IntStream.range(0, 100000).boxed().toArray(Integer[]::new);
        ParallelIndexSearcher<Integer> indexSearcher = new ParallelIndexSearcher<>(new ForkJoinPool(), array);
        int index = indexSearcher.indexOf(expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchAbsentValue() {
        int expectedIndex = -1;
        Integer[] array = IntStream.range(0, 8).boxed().toArray(Integer[]::new);
        ParallelIndexSearcher<Integer> indexSearcher = new ParallelIndexSearcher<>(new ForkJoinPool(), array);
        int index = indexSearcher.indexOf(expectedIndex);
        assertThat(index).isEqualTo(expectedIndex);
    }

    @Test
    public void whenSearchInStringArray() {
        int expectedIndex = 3;
        String[] array = IntStream.range(0, 8).mapToObj(String::valueOf).toArray(String[]::new);
        ParallelIndexSearcher<String> indexSearcher = new ParallelIndexSearcher<>(new ForkJoinPool(), array);
        int index = indexSearcher.indexOf(String.valueOf(expectedIndex));
        assertThat(index).isEqualTo(expectedIndex);
    }
}