package org.ginafro.notenoughfakepixel.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is made because of Java 8 compatibility issues with the Map.of() method.
 */
public class MapUtils {

    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(Pair<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> entry : entries) {
            map.put(entry.key, entry.value);
        }
        return Collections.unmodifiableMap(map); // Make it immutable
    }

    // Helper class to store key-value pairs
    public static class Pair<K, V> {
        final K key;
        final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public static <K, V> Pair<K, V> of(K key, V value) {
            return new Pair<>(key, value);
        }
    }

}
