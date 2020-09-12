package dev.b3nedikt.restring

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import androidx.core.text.HtmlCompat
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import dev.b3nedikt.restring.internal.RestringResources
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
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
    fun shouldGetStringFromResourceIfNotExists() {
        whenever(repository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf())
        )

        val stringValue = restringResources.getString(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)
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

        val expected = Html.fromHtml(STR_VALUE_HTML.toString(), Html.FROM_HTML_MODE_COMPACT)

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
        val def = Html.fromHtml("<b>def</b>", Html.FROM_HTML_MODE_COMPACT)
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
        private val STR_VALUE_HTML = HtmlCompat.fromHtml("STR_<b>value</b>", HtmlCompat.FROM_HTML_MODE_COMPACT)

        private const val STR_KEY_NOT_IN_STRINGS_XML = "STR_KEY_NOT_IN_STRINGS_XML"
        private const val ANOTHER_STR_KEY_NOT_IN_STRINGS_XML = "ANOTHER_STR_KEY_NOT_IN_STRINGS_XML"
    }
}