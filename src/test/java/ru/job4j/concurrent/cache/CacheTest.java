package ru.job4j.concurrent.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CacheTest {
    @Test
    public void whenAddNewBase() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        assertThat(cache.add(base)).isTrue();
    }

    @Test
    public void whenAddOldBase() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        assertThat(cache.add(base)).isTrue();
        assertThat(cache.add(base)).isFalse();
    }

    @Test
    public void whenUpdate() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.add(base);
        assertThat(cache.update(base)).isTrue();
    }

    @Test
    public void whenUpdateEmptyCache() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        assertThat(cache.update(base)).isFalse();
    }

    @Test
    public void whenUpdateTwice() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.add(base);
        assertThat(cache.update(base)).isTrue();
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    public void whenDeleteNotThrow() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.add(base);
        cache.delete(base);
    }

    @Test
    public void whenDeleteFromEmptyCacheNotThrow() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.delete(base);
    }

    @Test
    public void whenDeleteThenUpdate() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.add(base);
        cache.delete(base);
        assertThat(cache.update(base)).isFalse();
    }

    @Test
    public void whenDeleteThenAdd() {
        Cache cache = new Cache();
        Base base = new Base(1, 1, "Base");
        cache.add(base);

        cache.delete(base);
        assertThat(cache.add(base)).isTrue();
    }
}