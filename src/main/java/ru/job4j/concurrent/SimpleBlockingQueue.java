package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    public synchronized void offer(T value) {
        queue.offer(value);
        notifyAll();
    }

    public synchronized T poll() {
        while (queue.peek() == null) {
            try {
               wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}
