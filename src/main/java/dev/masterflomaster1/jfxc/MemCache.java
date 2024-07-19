package dev.masterflomaster1.jfxc;

import java.util.HashMap;
import java.util.Map;

public final class MemCache {

    private static final Map<String, String> STRING_POOL = new HashMap<>();
    private static final Map<String, Integer> INTEGER_POOL = new HashMap<>();

    private MemCache() { }

    public static String readString(String key, String defaultString) {
        if (STRING_POOL.containsKey(key))
            return STRING_POOL.get(key);

        return defaultString;
    }

    public static int readInteger(String key, int defaultInteger) {
        if (INTEGER_POOL.containsKey(key))
            return INTEGER_POOL.get(key);

        return defaultInteger;
    }

    public static void writeString(String key, String value) {
        STRING_POOL.put(key, value);
    }

    public static void writeInteger(String key, int value) {
        INTEGER_POOL.put(key, value);
    }

}
