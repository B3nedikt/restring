package dev.b3nedikt.restring

import android.content.Context
import android.text.Html
import android.text.TextUtils
import androidx.core.text.HtmlCompat
import androidx.test.core.app.ApplicationProvider
import dev.b3nedikt.restring.internal.RestringResources
import dev.b3nedikt.restring.repository.generateQuantityString
import dev.b3nedikt.restring.repository.generateStringArray
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class RestringResourcesTest {

    private val locale = Locale.getDefault()

    private val repository = mock<StringRepository> {
        on { supportedLocales } doReturn setOf(locale)
    }

    private val context = ApplicationProvider.getApplicationContext() as Context

    private val resources = context.resources

    private val restringResources = spy(RestringResources(resources, repository, context))

    @Test
    fun shouldAssignResourceIdToStringManagedOnlyByRestring() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(
                        STR_KEY_NOT_IN_STRINGS_XML to STR_VALUE as CharSequence
                ))
        )

        val stringId = restringResources.getIdentifier(
                name = STR_KEY_NOT_IN_STRINGS_XML,
                defType = "string",
                defPackage = context.packageName
        )

        stringId.shouldNotBeNull()
    }

    @Test
    fun identicalManagedStringsShouldShareTheSameId() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(
                        STR_KEY_NOT_IN_STRINGS_XML to STR_VALUE as CharSequence
                ))
        )

        val stringId1 = restringResources.getIdentifier(
                name = STR_KEY_NOT_IN_STRINGS_XML,
                defType = "string",
                defPackage = context.packageName
        )

        val stringId2 = restringResources.getIdentifier(
                name = STR_KEY_NOT_IN_STRINGS_XML,
                defType = "string",
                defPackage = context.packageName
        )

        stringId1 shouldBeEqualTo stringId2
    }

    @Test
    fun managedStringIdsShouldBeUnique() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(
                        STR_KEY_NOT_IN_STRINGS_XML to STR_VALUE as CharSequence,
                        ANOTHER_STR_KEY_NOT_IN_STRINGS_XML to STR_VALUE as CharSequence
                ))
        )

        val stringId1 = restringResources.getIdentifier(
                name = STR_KEY_NOT_IN_STRINGS_XML,
                defType = "string",
                defPackage = context.packageName
        )

        val stringId2 = restringResources.getIdentifier(
                name = ANOTHER_STR_KEY_NOT_IN_STRINGS_XML,
                defType = "string",
                defPackage = context.packageName
        )

        stringId1 shouldNotBeEqualTo stringId2
    }

    @Test
    fun shouldGetStringFromRepositoryIfExists() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(STR_KEY to STR_VALUE as CharSequence))
        )

        val stringValue = restringResources.getString(STR_RES_ID)

        STR_VALUE shouldBeEqualTo stringValue
    }

    @Test
    fun shouldGetStringFromRepositoryIfLanguageFallbackExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        whenever(repository.strings).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(STR_KEY to STR_VALUE as CharSequence),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val stringValue = restringResources.getString(STR_RES_ID)

        STR_VALUE shouldBeEqualTo stringValue
    }

    @Test
    fun shouldGetStringArrayFromRepositoryIfLanguageFallbackExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        val expectedStringArray = generateStringArray()
        whenever(repository.stringArrays).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(STR_ARRAY_KEY to generateStringArray()),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val stringArray = restringResources.getStringArray(ARRAY_STR_RES_ID)

        assertArrayEquals(expectedStringArray, stringArray)
    }

    @Test
    fun shouldGetQuantityStringFromRepositoryIfLanguageFallbackExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        val expectedQuantityString = generateQuantityString()
        whenever(repository.quantityStrings).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(QUANTITY_STR_KEY to expectedQuantityString),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val quantityString = restringResources.getQuantityString(QUANTITY_STR_RES_ID, 0)

        assertEquals(expectedQuantityString[PluralKeyword.ZERO], quantityString)
    }

    @Test
    fun shouldGetStringFromResourceIfNotExists() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf())
        )

        val stringValue = restringResources.getString(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetStringFromResourceIfLanguageFallbackNotExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        whenever(repository.strings).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val stringValue = restringResources.getString(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetStringArrayFromResourceIfLanguageFallbackNotExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        whenever(repository.stringArrays).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val stringValue = restringResources.getStringArray(ARRAY_STR_RES_ID)

        val expected = resources.getStringArray(ARRAY_STR_RES_ID)
        assertArrayEquals(expected, stringValue)
    }
    @Test
    fun shouldGetQuantityStringFromResourceIfLanguageFallbackNotExists() {
        val fallbackLocale = Locale("de")
        val regionLocale = Locale("de", "AT")

        val repository = mock<StringRepository> {
            on { supportedLocales } doReturn setOf(fallbackLocale, regionLocale)
        }
        val restringResources = spy(RestringResources(resources, repository, context))

        whenever(repository.quantityStrings).thenReturn(
            mutableMapOf(
                fallbackLocale to mutableMapOf(),
                regionLocale to mutableMapOf()
            )
        )

        Locale.setDefault(regionLocale)
        val stringValue = restringResources.getQuantityString(QUANTITY_STR_RES_ID, 0)

        val expected = resources.getQuantityString(QUANTITY_STR_RES_ID, 0)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetStringWithParamsFromRepositoryIfExists() {
        val param = "PARAM"
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(STR_KEY to STR_VALUE_WITH_PARAM as CharSequence))
        )

        val stringValue = restringResources.getString(STR_RES_ID, param)

        assertEquals(String.format(STR_VALUE_WITH_PARAM, param), stringValue)
    }

    @Test
    fun shouldGetStringWithParamsFromResourceIfNotExists() {
        val param = "PARAM"
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf())
        )

        val stringValue = restringResources.getString(STR_RES_ID, param)

        val expected = resources.getText(STR_RES_ID)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetHtmlTextFromRepositoryIfExists() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(STR_KEY to STR_VALUE_HTML as CharSequence))
        )

        val realValue = restringResources.getText(STR_RES_ID)

        val expected = HtmlCompat.fromHtml(STR_VALUE_HTML.toString(), Html.FROM_HTML_MODE_COMPACT)

        realValue shouldBeEqualTo expected
    }

    @Test
    fun shouldGetHtmlTextFromResourceIfNotExists() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf())
        )

        val realValue = restringResources.getText(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)

        realValue shouldBeEqualTo expected
    }

    @Test
    fun shouldReturnDefaultHtmlTextFromRepositoryIfResourceIdIsInvalid() {
        val def = HtmlCompat.fromHtml("<b>def</b>", Html.FROM_HTML_MODE_COMPACT)
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf())
        )

        val realValue = restringResources.getText(0, def)

        realValue shouldBeEqualTo def
    }

    private infix fun CharSequence.shouldBeEqualTo(expected: CharSequence) = this.apply {
        assertTrue("Expected '$this' to have the same text as $expected",
                TextUtils.equals(this, expected))
    }

    private companion object {
        private val STR_RES_ID = R.string.STR_KEY
        private const val STR_KEY = "STR_KEY"
        private const val STR_VALUE = "STR_VALUE"
        private const val STR_VALUE_WITH_PARAM = "STR_VALUE %s"

        private val QUANTITY_STR_RES_ID = R.plurals.QUANTITY_STR_KEY
        private const val QUANTITY_STR_KEY = "QUANTITY_STR_KEY"

        private val ARRAY_STR_RES_ID = R.array.STR_ARRAY_KEY
        private const val STR_ARRAY_KEY = "STR_ARRAY_KEY"

        private val STR_VALUE_HTML = HtmlCompat.fromHtml("STR_<b>value</b>", HtmlCompat.FROM_HTML_MODE_COMPACT)

        private const val STR_KEY_NOT_IN_STRINGS_XML = "STR_KEY_NOT_IN_STRINGS_XML"
        private const val ANOTHER_STR_KEY_NOT_IN_STRINGS_XML = "ANOTHER_STR_KEY_NOT_IN_STRINGS_XML"
    }
}