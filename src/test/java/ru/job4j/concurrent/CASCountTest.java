package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {
    @Test
    public void whenAddMultithreading() throws InterruptedException {
        CASCount casCount = new CASCount();
        Runnable incriment = () -> IntStream.range(0, 5)
                .forEach(
                        i -> casCount.increment()
                );

        Thread thread1 = new Thread(incriment);
        Thread thread2 = new Thread(incriment);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertThat(casCount.get()).isEqualTo(10);
    }
}