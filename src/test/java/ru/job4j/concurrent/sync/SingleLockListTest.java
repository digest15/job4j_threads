package ru.job4j.concurrent.sync;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.*;

class SingleLockListTest {
    @Test
    public void whenIt(){
        var init = new ArrayList<>();
        SingleLockList<Integer> list = new SingleLockList<>(init);
        list.add(1);
        var it = list.iterator();
        list.add(2);
        assertThat(1, is(it.next()));
    }

    @Test
    public void whenAdd() throws InterruptedException {
        var init = new ArrayList<Integer>();
        SingleLockList<Integer> list = new SingleLockList<>(init);
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        second.start();
        first.join();
        second.join();
        Set<Integer> rsl = new TreeSet<>();
        list.iterator().forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2)));
    }
}