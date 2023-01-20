package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void whenPollThenOffer() throws Exception {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>();
        Runnable consumer = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + ", value: " + simpleBlockingQueue.poll());
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
            simpleBlockingQueue.offer(1);
            simpleBlockingQueue.offer(2);
        },
                "Produser1");
        produser1.start();

        produser1.join();
        consumer1.join();
        consumer2.join();
    }
}