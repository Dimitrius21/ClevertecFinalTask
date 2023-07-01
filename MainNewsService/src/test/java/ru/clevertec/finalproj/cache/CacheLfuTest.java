package ru.clevertec.finalproj.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CacheLfuTest {

    @Test
    void putTest() {
        CacheLfuSync<String> cache = new CacheLfuSync<>("Test", 2);
        cache.put(1L, "1");
        String res = cache.get(1L);
        Assertions.assertThat(res).isEqualTo("1");
    }

    @Test
    void getTest() {
        CacheLfuSync<String> cache = new CacheLfuSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.get(1L);
        cache.put(3L, "3");
        String res = cache.get(1L);
        Assertions.assertThat(res).isEqualTo("1");
    }

    @Test
    void valueIsAbsentTest() {
        CacheLfuSync<String> cache = new CacheLfuSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.get(1L);
        cache.get(1L);
        cache.get(12);
        cache.put(3L, "3");
        String res = cache.get(2L);
        Assertions.assertThat(res).isNull();
    }

    @Test
    void delete() {
        CacheLfuSync<String> cache = new CacheLfuSync<>("Test", 2);
        cache.put(1L, "1");
        cache.put(2L, "2");
        cache.get(1L);
        cache.remove(1L);
        String res = cache.get(1L);
        Assertions.assertThat(res).isNull();
    }
}