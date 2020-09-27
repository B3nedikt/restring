package dev.b3nedikt.restring.repository

import android.os.Build
import dev.b3nedikt.restring.PluralKeyword
import junit.framework.TestCase.assertEquals
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MemoryStringRepositoryTest {

    private val stringRepository = MemoryStringsRepository()

    @Test
    fun shouldGetLocalesAfterInsertingStrings() {
        val locales = setOf(Locale.ENGLISH, Locale.FRENCH)
        val strings = generateStrings(10)

        stringRepository.strings[Locale.ENGLISH]?.putAll(strings)
        stringRepository.strings[Locale.FRENCH]?.putAll(strings)

        locales shouldContainSame stringRepository.supportedLocales
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.strings[locale]?.putAll(strings)

        strings shouldBeEqualTo stringRepository.strings[locale]
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.strings[locale]?.putAll(strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.strings[locale]?.get("key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.strings[locale]?.putAll(strings)
        stringRepository.strings[locale]?.put("key5", "aNewValue")

        assertEquals(stringRepository.strings[locale]?.get("key5"), "aNewValue")
    }

    @Test
    fun shouldGetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings: Map<String, Array<CharSequence>> = generateStringArrays(stringCount)
        stringRepository.stringArrays[locale]?.putAll(strings)

        for (i in 0 until stringCount) {
            stringRepository.stringArrays[locale]?.get("key$i")?.contentDeepToString() shouldBeEqualTo
                    strings["key$i"]?.contentDeepToString()
        }
    }

    @Test
    fun shouldSetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)

        stringRepository.stringArrays[locale]?.putAll(strings)
        val stringArray: Array<CharSequence> = arrayOf("aNewValue")
        stringRepository.stringArrays[locale]?.put("key5", stringArray)

        stringRepository.stringArrays[locale]?.get("key5")?.contentDeepToString() shouldBeEqualTo
                stringArray.contentDeepToString()
    }

    @Test
    fun shouldGetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10

        val strings = generateQuantityStrings(stringCount)
        stringRepository.quantityStrings[locale]?.putAll(strings)

        for (i in 0 until stringCount) {
            stringRepository.quantityStrings[locale]?.get("key$i") shouldBeEqualTo strings["key$i"]
        }
    }

    @Test
    fun shouldSetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateQuantityStrings(stringCount)

        stringRepository.quantityStrings[locale]?.putAll(strings)
        val quantityString = mapOf(PluralKeyword.ONE to "aNewValue")
        stringRepository.quantityStrings[locale]?.put("key5", quantityString)

        stringRepository.quantityStrings[locale]?.get("key5") shouldBeEqualTo quantityString
    }
}