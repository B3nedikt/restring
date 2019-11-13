package com.b3nedikt.restring.repository

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.b3nedikt.restring.StringRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CachedStringRepositoryTest {

    private lateinit var stringRepository: StringRepository

    @Before
    fun setUp() {
        val persistentRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())

        stringRepository = CachedStringRepository(
                cacheRepository = MemoryStringRepository(),
                persistentRepository = persistentRepository
        )
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.setStrings(locale, strings)

        Assert.assertEquals(strings, stringRepository.getStrings(locale))
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.setStrings(locale, strings)

        for (i in 0 until stringCount) {
            Assert.assertEquals(stringRepository.getString(locale, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.setStrings(locale, strings)
        stringRepository.setString(locale, "key5", "aNewValue")

        Assert.assertEquals(stringRepository.getString(locale, "key5"), "aNewValue")
    }

    private fun generateStrings(count: Int): Map<String, String> {
        val strings = LinkedHashMap<String, String>()
        for (i in 0 until count) {
            strings["key$i"] = "value$i"
        }
        return strings
    }
}