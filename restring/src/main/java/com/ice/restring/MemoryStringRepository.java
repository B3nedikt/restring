package com.ice.restring;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A StringRepository which keeps the strings ONLY in memory.
 * <p>
 * it's not ThreadSafe.
 */
class MemoryStringRepository implements StringRepository {

    private Map<String, Map<String, String>> strings = new LinkedHashMap<>();

    @Override
    public void setStrings(String language, Map<String, String> newStrings) {
        strings.put(language, newStrings);
    }

    @Override
    public void setString(String language, String key, String value) {
        if (!strings.containsKey(language)) {
            strings.put(language, new LinkedHashMap<>());
        }
        strings.get(language).put(key, value);
    }

    @Override
    public String getString(String language, String key) {
        if (!strings.containsKey(language) || !strings.get(language).containsKey(key)) {
            return null;
        }
        return strings.get(language).get(key);
    }

    @Override
    public Map<String, String> getStrings(String language) {
        if (!strings.containsKey(language)) {
            return new LinkedHashMap<>();
        }

        return new LinkedHashMap<>(strings.get(language));
    }
}