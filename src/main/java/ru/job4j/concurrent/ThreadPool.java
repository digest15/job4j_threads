package ru.job4j.concurrent;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;

    public ThreadPool() {
        this(1);
    }

    public ThreadPool(int multiplicator) {
        int count = Runtime.getRuntime().availableProcessors();

        this.tasks = new SimpleBlockingQueue<>(count * multiplicator);

        Runnable runner = () -> {
            try {
                while (!tasks.isEmpty() || !Thread.currentThread().isInterrupted()) {
                    tasks.poll().run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < count; i++) {
            Thread worker = new Thread(runner, "Worker " + i);
            worker.start();
            threads.add(worker);
        }
    }

    public void work(Runnable job) {
        try {
            tasks.offer(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        threads.forEach(
                Thread::interrupt
        );
    }
}
