package ru.clevertec.finalproj.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class CacheLruTest {

    @Test
    void putTest() {
        CacheLruSync<String> cache = new CacheLruSync<>("Test", 2);
        cache.put(1L, "10");
        String res = cache.get(1L);
        Assertions.assertThat(res).isEqualTo("10");
    }

    @Test
    void getTest() {
        CacheLruSync<String> cache = new CacheLruSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.put(3L, "3");

        String res = cache.get(3L);
        Assertions.assertThat(res).isEqualTo("3");
    }

    @Test
    void valueIsAbsentTest() {
        CacheLruSync<String> cache = new CacheLruSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.get(1L);
        cache.get(1L);
        cache.get(2L);
        cache.put(3L, "3");

        String res = cache.get(1L);
        Assertions.assertThat(res).isNull();
    }

    @Test
    void deleteTest() {
        CacheLruSync<String> cache = new CacheLruSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.remove(1L);
        String res = cache.get(1L);
        Assertions.assertThat(res).isNull();
    }
}