package ru.job4j.concurrent;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearcher<T> {

    private final ForkJoinPool pool;

    private final T[] array;

    private volatile boolean isFound;

    public ParallelIndexSearcher(ForkJoinPool pool, T[] array) {
        this.pool = pool;
        this.array = array;
    }

    public int indexOf(T value) {
        isFound = false;
        return pool.invoke(new SearchTask(0, array.length, value));
    }

    private class SearchTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10;

        private final int from;
        private final int to;
        private final T value;

        public SearchTask(int from, int to, T value) {
            this.from = from;
            this.to = to;
            this.value = value;
        }

        @Override
        protected Integer compute() {
            if (to - from <= THRESHOLD) {
                return lineSearch();
            }
            int border = (to - from) / 2 + from;
            SearchTask leftTask = new SearchTask(from, border,  value);
            SearchTask rightTask = new SearchTask(border, to, value);
            leftTask.fork();
            rightTask.fork();
            int li = leftTask.join();
            int ri = rightTask.join();

            return Math.max(li, ri);
        }

        private Integer lineSearch() {
            int result = -1;
            for (int i = from; i < to && !isFound; i++) {
                if (value.equals(array[i])) {
                    result = i;
                    isFound = true;
                    break;
                }
            }
            return result;
        }
    }

}
