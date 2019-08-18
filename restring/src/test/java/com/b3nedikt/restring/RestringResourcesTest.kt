package com.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.text.TextUtils
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class RestringResourcesTest {

    private lateinit var repository: StringRepository

    private lateinit var resources: Resources

    private lateinit var restringResources: RestringResources

    private val language: String
        get() = Locale.getDefault().language

    @Before
    fun setUp() {
        repository = mock()
        resources =  (ApplicationProvider.getApplicationContext() as Context).resources

        restringResources = spy(RestringResources(resources, repository))
    }

    @Test
    fun shouldGetStringFromRepositoryIfExists() {
        whenever(repository.getString(language, STR_KEY)).thenReturn(STR_VALUE)

        val stringValue = restringResources.getString(STR_RES_ID)

        assertEquals(STR_VALUE, stringValue)
    }

    @Test
    fun shouldGetStringFromResourceIfNotExists() {
        whenever(repository.getString(language, STR_KEY)).thenReturn(null)

        val stringValue = restringResources.getString(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetStringWithParamsFromRepositoryIfExists() {
        val param = "PARAM"
        whenever(repository.getString(language, STR_KEY)).thenReturn(STR_VALUE_WITH_PARAM)

        val stringValue = restringResources.getString(STR_RES_ID, param)

        assertEquals(String.format(STR_VALUE_WITH_PARAM, param), stringValue)
    }

    @Test
    fun shouldGetStringWithParamsFromResourceIfNotExists() {
        val param = "PARAM"
        whenever(repository.getString(language, STR_KEY)).thenReturn(null)

        val stringValue = restringResources.getString(STR_RES_ID, param)

        val expected = resources.getText(STR_RES_ID)
        assertEquals(expected, stringValue)
    }

    @Test
    fun shouldGetHtmlTextFromRepositoryIfExists() {
        whenever(repository.getString(language, STR_KEY)).thenReturn(STR_VALUE_HTML)

        val realValue = restringResources.getText(STR_RES_ID)

        val expected = Html.fromHtml(STR_VALUE_HTML, Html.FROM_HTML_MODE_COMPACT)
        assertTrue(TextUtils.equals(expected, realValue))
    }

    @Test
    fun shouldGetHtmlTextFromResourceIfNotExists() {
        whenever(repository.getString(language, STR_KEY)).thenReturn(null)

        val realValue = restringResources.getText(STR_RES_ID)

        val expected = resources.getText(STR_RES_ID)
        assertTrue(TextUtils.equals(expected, realValue))
    }

    @Test
    fun shouldReturnDefaultHtmlTextFromRepositoryIfResourceIdIsInvalid() {
        val def = Html.fromHtml("<b>def</b>", Html.FROM_HTML_MODE_COMPACT)
        whenever(repository.getString(language, STR_KEY)).thenReturn(null)

        val realValue = restringResources.getText(0, def)

        assertTrue(TextUtils.equals(def, realValue))
    }

    private companion object {
        private val STR_RES_ID = R.string.STR_KEY
        private const val STR_KEY = "STR_KEY"
        private const val STR_VALUE = "STR_VALUE"
        private const val STR_VALUE_WITH_PARAM = "STR_VALUE %s"
        private const val STR_VALUE_HTML = "STR_<b>value</b>"
    }
}