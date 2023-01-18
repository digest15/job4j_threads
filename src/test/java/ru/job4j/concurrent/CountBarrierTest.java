package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CountBarrierTest {
    @Test
    void whenStartTwoThreads() {
        CountBarrier countBarrier = new CountBarrier(2);
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " started");
            countBarrier.count();
            countBarrier.await();
        };

        Thread first = new Thread(task);
        System.out.println("Запуск потока 1");
        first.start();

        Thread second = new Thread(task);
        System.out.println("Запуск потока 2");
        second.start();
    }
}