package dev.b3nedikt.restring.repository

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import dev.b3nedikt.restring.PluralKeyword
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CachedStringRepositoryTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val persistentRepository = SharedPrefsStringRepository { sharedPreferencesName ->
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    private val stringRepository = CachedStringRepository(
            persistentRepository = persistentRepository
    )

    @Test
    fun shouldGetLocalesAfterInsertingStrings() {
        val locales = setOf(Locale.ENGLISH, Locale.FRENCH)
        val strings = generateStrings(10)

        stringRepository.strings[Locale.ENGLISH]?.putAll(strings)
        stringRepository.strings[Locale.FRENCH]?.putAll(strings)

        stringRepository.supportedLocales shouldContainSame locales
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.strings[locale]?.putAll(strings)

        stringRepository.strings[locale]!! shouldContainSame strings
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.strings[locale]?.putAll(strings)

        stringRepository.strings[locale]!! shouldContainSame strings
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.strings[locale]?.putAll(strings)
        stringRepository.strings[locale]?.put("key5", "aNewValue")

        stringRepository.strings[locale]?.get("key5") shouldBeEqualTo "aNewValue"
    }

    @Test
    fun shouldGetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)

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

        stringRepository.stringArrays[locale]?.get("key5")!! shouldContainSame stringArray
    }

    @Test
    fun shouldGetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10

        val strings = generateQuantityStrings(stringCount)
        stringRepository.quantityStrings[locale]?.putAll(strings)

        stringRepository.quantityStrings[locale]!! shouldContainSame strings
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