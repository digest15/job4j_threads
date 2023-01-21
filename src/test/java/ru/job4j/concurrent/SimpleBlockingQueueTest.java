package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void whenPollWithEmptyQueueThenOffer() throws Exception {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Runnable consumer = () -> {
            try {
                buffer.add(queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        };
        Thread consumer1 = new Thread(consumer, "Consumer1");
        consumer1.start();
        Thread consumer2 = new Thread(consumer, "Consumer2");
        consumer2.start();

        Thread produser1 = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        },
                "Produser1");
        produser1.start();

        produser1.join();
        consumer1.join();
        consumer2.join();

        assertThat(buffer).isEqualTo(List.of(1, 2));
    }

    @Test
    public void whenOfferWithFullyQueueThenPoll() throws InterruptedException {
        int size = 3;
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(size);
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        Thread produser1 = new Thread(() -> {
            for (int i = 0; i < size + 1; i++) {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        },
                "Produser1");

        Thread consumer1 = new Thread(() -> {
            try {
                while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                    buffer.add(queue.poll());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        },
                "Consumer1");

        produser1.start();
        consumer1.start();
        produser1.join();
        consumer1.interrupt();
        consumer1.join();

        assertThat(buffer).isEqualTo(List.of(0, 1, 2, 3));
    }
}