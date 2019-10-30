package com.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.b3nedikt.restring.transformer.TextViewViewTransformer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TextViewViewTransformerTest {

    private lateinit var transformer: TextViewViewTransformer

    private val context: Context
        get() {
            val context = spy(ApplicationProvider.getApplicationContext() as Context)
            val resources = spy<Resources>(context.resources)

            whenever(context.resources).thenReturn(resources)
            doReturn(TEXT_ATTR_VALUE).whenever(resources).getString(TEXT_RES_ID)
            doReturn(HINT_ATTR_VALUE).whenever(resources).getString(HINT_RES_ID)

            return context
        }

    @Before
    fun setUp() {
        transformer = TextViewViewTransformer()
    }

    @Test
    fun shouldTransformTextView() {
        val context = context

        var view = transformer.transform(TextView(context), getAttributeSet(false))

        assertTrue(view is TextView)
        assertEquals((view as TextView).text, TEXT_ATTR_VALUE)
        assertEquals(view.hint, HINT_ATTR_VALUE)

        view = transformer.transform(TextView(context), getAttributeSet(true))

        assertTrue(view is TextView)
        assertEquals((view as TextView).text, TEXT_ATTR_VALUE)
        assertEquals(view.hint, HINT_ATTR_VALUE)
    }

    @Test
    fun shouldTransformExtendedViews() {
        val context = context

        var view = transformer.transform(EditText(context), getAttributeSet(false))

        assertTrue(view is EditText)
        assertEquals((view as EditText).text.toString(), TEXT_ATTR_VALUE)
        assertEquals(view.hint, HINT_ATTR_VALUE)

        view = transformer.transform(EditText(context), getAttributeSet(true))

        assertTrue(view is EditText)
        assertEquals((view as EditText).text.toString(), TEXT_ATTR_VALUE)
        assertEquals(view.hint, HINT_ATTR_VALUE)
    }

    @Test
    fun shouldRejectOtherViewTypes() {
        val context = context
        val attributeSet = getAttributeSet(false)
        val recyclerView = androidx.recyclerview.widget.RecyclerView(context)

        val view = transformer.transform(recyclerView, attributeSet)

        assertSame(view, recyclerView)
        verifyZeroInteractions(attributeSet)
    }

    private fun getAttributeSet(withAndroidPrefix: Boolean): AttributeSet {
        val attributeSet = mock<AttributeSet>()
        whenever(attributeSet.attributeCount).thenReturn(TEXT_ATTR_INDEX + 2)

        whenever(attributeSet.getAttributeName(anyInt())).thenReturn("other_attribute")
        whenever(attributeSet.getAttributeName(TEXT_ATTR_INDEX)).thenReturn((if (withAndroidPrefix) "android:" else "") + TEXT_ATTR_KEY)
        whenever(attributeSet.getAttributeValue(TEXT_ATTR_INDEX)).thenReturn("@$TEXT_RES_ID")
        whenever(attributeSet.getAttributeResourceValue(eq(TEXT_ATTR_INDEX), anyInt())).thenReturn(TEXT_RES_ID)
        whenever(attributeSet.getAttributeName(HINT_ATTR_INDEX)).thenReturn((if (withAndroidPrefix) "android:" else "") + HINT_ATTR_KEY)
        whenever(attributeSet.getAttributeValue(HINT_ATTR_INDEX)).thenReturn("@$HINT_RES_ID")
        whenever(attributeSet.getAttributeResourceValue(eq(HINT_ATTR_INDEX), anyInt())).thenReturn(HINT_RES_ID)

        return attributeSet
    }

    private companion object {
        private const val TEXT_ATTR_INDEX = 3
        private const val TEXT_RES_ID = 0x7f0f0123
        private const val TEXT_ATTR_KEY = "text"
        private const val TEXT_ATTR_VALUE = "TEXT_ATTR_VALUE"

        private const val HINT_ATTR_INDEX = 2
        private const val HINT_RES_ID = 0x7f0f0124
        private const val HINT_ATTR_KEY = "hint"
        private const val HINT_ATTR_VALUE = "HINT_ATTR_VALUE"
    }
}