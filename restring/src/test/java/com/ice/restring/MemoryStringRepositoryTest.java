package com.ice.restring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class MemoryStringRepositoryTest {

    private StringRepository stringRepository;

    @Before
    public void setUp() {
        stringRepository = new MemoryStringRepository();
    }

    @Test
    public void shouldSetAndGetStringPairs() {
        final String LANGUAGE = "en";
        Map<String, String> strings = generateStrings(10);

        stringRepository.setStrings(LANGUAGE, strings);

        assertEquals(strings, stringRepository.getStrings(LANGUAGE));
    }

    @Test
    public void shouldGetSingleString() {
        final String LANGUAGE = "en";
        final int STR_COUNT = 10;
        Map<String, String> strings = generateStrings(STR_COUNT);
        stringRepository.setStrings(LANGUAGE, strings);

        for (int i = 0; i < STR_COUNT; i++) {
            assertEquals(stringRepository.getString(LANGUAGE, "key" + i), "value" + i);
        }
    }

    @Test
    public void shouldSetSingleString() {
        final String LANGUAGE = "en";
        final int STR_COUNT = 10;
        Map<String, String> strings = generateStrings(STR_COUNT);

        stringRepository.setStrings(LANGUAGE, strings);
        stringRepository.setString(LANGUAGE, "key5", "aNewValue");

        assertEquals(stringRepository.getString(LANGUAGE, "key5"), "aNewValue");
    }

    private Map<String, String> generateStrings(int count) {
        Map<String, String> strings = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            strings.put("key" + i, "value" + i);
        }
        return strings;
    }
}