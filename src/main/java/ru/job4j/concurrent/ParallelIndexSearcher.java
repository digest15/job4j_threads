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
        return pool.invoke(new SearchTask(array, value));
    }

    private class SearchTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10;

        private final T[] array;
        private final T value;

        public SearchTask(T[] array, T value) {
            this.array = array;
            this.value = value;
        }

        @Override
        protected Integer compute() {
            if (array.length <= THRESHOLD) {
                return lineSearch();
            }
            T[] leftArr = Arrays.copyOfRange(array, 0, array.length / 2);
            SearchTask leftTask = new SearchTask(leftArr, value);
            leftTask.fork();
            int li = leftTask.join();

            T[] rightArr = Arrays.copyOfRange(array, array.length / 2, array.length);
            SearchTask rightTask = new SearchTask(rightArr, value);
            rightTask.fork();
            int ri = rightTask.join();

            int result;
            if (li == -1 && ri == -1) {
                result = -1;
            } else if (li == -1) {
                result = ri + leftArr.length;
            } else {
                result = li;
            }
            return result;
        }

        private Integer lineSearch() {
            int result = -1;
            for (int i = 0; i < array.length && !isFound; i++) {
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
