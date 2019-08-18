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
        val language = "en"
        val strings = generateStrings(10)

        stringRepository.setStrings(language, strings)

        assertEquals(strings, stringRepository.getStrings(language))
    }

    @Test
    fun shouldGetSingleString() {
        val language = "en"
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.setStrings(language, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getString(language, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val language = "en"
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.setStrings(language, strings)
        stringRepository.setString(language, "key5", "aNewValue")

        assertEquals(stringRepository.getString(language, "key5"), "aNewValue")
    }

    private fun generateStrings(count: Int): Map<String, String> {
        val strings = LinkedHashMap<String, String>()
        for (i in 0 until count) {
            strings["key$i"] = "value$i"
        }
        return strings
    }
}