package com.b3nedikt.restring.repository

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SharedPrefStringRepositoryTest {

    @Test
    fun shouldSetAndGetLocales() {
        val locales = setOf(Locale.ENGLISH, Locale.FRENCH)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.supportedLocales = locales

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(locales, newRepository.supportedLocales)
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(strings, newRepository.getStrings(locale))
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        for (i in 0 until stringCount) {
            assertEquals(newRepository.getString(locale, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)
        stringRepository.setString(locale, "key5", "aNewValue")

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(newRepository.getString(locale, "key5"), "aNewValue")
    }

    @Test
    fun shouldGetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)
        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStringArrays(locale, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        for (i in 0 until stringCount) {
            assertEquals(newRepository.getStringArray(locale, "key$i")?.contentDeepToString(),
                    strings["key$i"]?.contentDeepToString())
        }
    }

    @Test
    fun shouldSetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStringArrays(locale, strings)
        val stringArray: Array<CharSequence> = arrayOf("aNewValue")
        stringRepository.setStringArray(locale, "key5", stringArray)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(newRepository.getStringArray(locale, "key5")?.contentDeepToString(),
                stringArray.contentDeepToString())
    }
}