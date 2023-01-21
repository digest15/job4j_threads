package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void whenPollWithEmptyQueueThenOffer() throws Exception {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Runnable consumer = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + ", value: " + queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };
        Thread consumer1 = new Thread(consumer, "Consumer1");
        consumer1.start();
        Thread consumer2 = new Thread(consumer, "Consumer2");
        consumer2.start();

        TimeUnit.SECONDS.sleep(5);

        Thread produser1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                queue.offer(1);
                queue.offer(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },
                "Produser1");
        produser1.start();

        produser1.join();
        consumer1.join();
        consumer2.join();
    }

    @Test
    public void whenOfferWithFullyQueueThenPoll() throws InterruptedException {
        int size = 3;
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(size);
        Thread produser1 = new Thread(() -> {
            for (int i = 0; i < size + 1; i++) {
                try {
                    queue.offer(i);
                    System.out.println(Thread.currentThread().getName() + " Offer " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },
                "Produser1");

        Thread consumer1 = new Thread(() -> {
            try {
                for (int i = 0; i < size; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " Poll " + queue.poll());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },
                "Consumer1");

        produser1.start();
        consumer1.start();
        consumer1.join();
        produser1.join();
    }
}