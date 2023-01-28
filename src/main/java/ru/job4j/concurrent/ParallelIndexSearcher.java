package ru.job4j.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearcher<T> extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 10;

    private final T[] array;
    private final int from;
    private final int to;
    private final T value;

    public ParallelIndexSearcher(T[] array, int from, int to, T value) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public static <T> int indexOf(T[] array, T value) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new ParallelIndexSearcher<>(array, 0, array.length, value));
    }

    @Override
    protected Integer compute() {
        if (to - from <= THRESHOLD) {
            return lineSearch();
        }
        int border = (to - from) / 2 + from;
        ParallelIndexSearcher<T> leftTask = new ParallelIndexSearcher<>(array, from, border, value);
        ParallelIndexSearcher<T> rightTask = new ParallelIndexSearcher<>(array, border, to, value);
        leftTask.fork();
        rightTask.fork();
        int li = leftTask.join();
        int ri = rightTask.join();

        return Math.max(li, ri);
    }

    private Integer lineSearch() {
        int result = -1;
        for (int i = from; i < to; i++) {
            if (value.equals(array[i])) {
                result = i;
                break;
            }
        }
        return result;
    }
}
