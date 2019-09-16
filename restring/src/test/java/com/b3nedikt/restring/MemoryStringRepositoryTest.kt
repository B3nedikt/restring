package com.b3nedikt.restring

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class MemoryStringRepositoryTest {

    private lateinit var stringRepository: StringRepository

    @Before
    fun setUp() {
        stringRepository = MemoryStringRepository()
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.setStrings(locale, strings)

        assertEquals(strings, stringRepository.getStrings(locale))
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.setStrings(locale, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getString(locale, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.setStrings(locale, strings)
        stringRepository.setString(locale, "key5", "aNewValue")

        assertEquals(stringRepository.getString(locale, "key5"), "aNewValue")
    }

    private fun generateStrings(count: Int): Map<String, String> {
        val strings = LinkedHashMap<String, String>()
        for (i in 0 until count) {
            strings["key$i"] = "value$i"
        }
        return strings
    }
}