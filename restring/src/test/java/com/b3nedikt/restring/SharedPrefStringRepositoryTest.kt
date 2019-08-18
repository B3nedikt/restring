package com.b3nedikt.restring

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class SharedPrefStringRepositoryTest {

    @Before
    fun setUp() {
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val LANGUAGE = "en"
        val strings = generateStrings(10)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(LANGUAGE, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(strings, newRepository.getStrings(LANGUAGE))
    }

    @Test
    fun shouldGetSingleString() {
        val LANGUAGE = "en"
        val STR_COUNT = 10
        val strings = generateStrings(STR_COUNT)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(LANGUAGE, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        for (i in 0 until STR_COUNT) {
            assertEquals(newRepository.getString(LANGUAGE, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val LANGUAGE = "en"
        val STR_COUNT = 10
        val strings = generateStrings(STR_COUNT)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(LANGUAGE, strings)
        stringRepository.setString(LANGUAGE, "key5", "aNewValue")

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(newRepository.getString(LANGUAGE, "key5"), "aNewValue")
    }

    private fun generateStrings(count: Int): Map<String, String> {
        val strings = LinkedHashMap<String, String>()
        for (i in 0 until count) {
            strings["key$i"] = "value$i"
        }
        return strings
    }
}