package dev.b3nedikt.restring.repository

import dev.b3nedikt.restring.PluralKeyword
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class MemoryStringRepositoryTest {

    private var stringRepository = MemoryStringRepository()

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.setStrings(locale, strings)

        assertEquals(strings, stringRepository.getStrings(locale))
    }

    @Test
    fun shouldGetLocalesAfterInsertingStrings() {
        val locales = setOf(Locale.ENGLISH, Locale.FRENCH)
        val strings = generateStrings(10)

        stringRepository.setStrings(Locale.ENGLISH, strings)
        stringRepository.setStrings(Locale.FRENCH, strings)

        locales shouldBeEqualTo stringRepository.supportedLocales
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

    @Test
    fun shouldGetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings: Map<String, Array<CharSequence>> = generateStringArrays(stringCount)
        stringRepository.setStringArrays(locale, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getStringArray(locale, "key$i")?.contentDeepToString(),
                    strings["key$i"]?.contentDeepToString())
        }
    }

    @Test
    fun shouldSetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)

        stringRepository.setStringArrays(locale, strings)
        val stringArray: Array<CharSequence> = arrayOf("aNewValue")
        stringRepository.setStringArray(locale, "key5", stringArray)

        assertEquals(stringRepository.getStringArray(locale, "key5")?.contentDeepToString(),
                stringArray.contentDeepToString())
    }

    @Test
    fun shouldGetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateQuantityStrings(stringCount)
        stringRepository.setQuantityStrings(locale, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getQuantityString(locale, "key$i"), strings["key$i"])
        }
    }

    @Test
    fun shouldSetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateQuantityStrings(stringCount)

        stringRepository.setQuantityStrings(locale, strings)
        val quantityString = mapOf(PluralKeyword.ONE to "aNewValue")
        stringRepository.setQuantityString(locale, "key5", quantityString)

        assertEquals(stringRepository.getQuantityString(locale, "key5"), quantityString)
    }
}