package ru.job4j.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class CASCount {
    private final AtomicReference<Integer> count;

    public CASCount() {
        this.count = new AtomicReference<>();
        count.getAndSet(0);
    }

    public void increment() {
        int current;
        int next;
        do {
            current = count.get();
            next = current + 1;
        } while (!count.compareAndSet(current, next));
    }

    public int get() {
        int current;
        int pred;
        do {
            current = count.get();
            pred = current - 1;
        } while (!count.compareAndSet(current, pred));
        return current;
    }
}
