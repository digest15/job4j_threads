package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

class ThreadPoolTest {
    @Test
    public void whenAddTask() throws InterruptedException {
        final CopyOnWriteArrayList<String> buffer = new CopyOnWriteArrayList<>();
        int multiplicator = 1;
        int count = Runtime.getRuntime().availableProcessors();
        final ThreadPool threadPool = new ThreadPool(multiplicator);
        Runnable task = () -> buffer.add("");
        for (int i = 0; i < count * multiplicator; i++) {
            threadPool.work(task);
        }

        threadPool.shutdown();
        TimeUnit.SECONDS.sleep(2);

        assertThat(buffer.size()).isEqualTo(count * multiplicator);
    }
}