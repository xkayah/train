package com.mnus.common.utils;

import java.util.HashMap;

/**
 * LRU cache
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 21:32:18
 */
public class LRUCache {
    private static final HashMap<String, String> map = new HashMap<>();

    public LRUCache() {
    }

    public static String get(String key) {
        return map.get(key);
    }

    public static void put(String key, String value) {
        map.put(key, value);
    }
}
