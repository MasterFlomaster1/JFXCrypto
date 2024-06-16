package dev.masterflomaster1.sjc;

import java.util.HashMap;
import java.util.Map;

public class MemCache {

    private static final Map<String, String> stringsPool = new HashMap<>();
    private static final Map<String, Integer> integerPool = new HashMap<>();

    public static String readString(String key, String defaultString) {
        if (stringsPool.containsKey(key))
            return stringsPool.get(key);

        return defaultString;
    }

    public static int readInteger(String key, int defaultInteger) {
        if (integerPool.containsKey(key))
            return integerPool.get(key);

        return defaultInteger;
    }

    public static void writeString(String key, String value) {
        stringsPool.put(key, value);
    }

    public static void writeInteger(String key, int value) {
        integerPool.put(key, value);
    }

}
