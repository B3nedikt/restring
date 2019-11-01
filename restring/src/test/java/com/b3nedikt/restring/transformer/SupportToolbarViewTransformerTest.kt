package com.b3nedikt.restring.transformer

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SupportToolbarViewTransformerTest {

    private lateinit var transformer: SupportToolbarViewTransformer

    private val context: Context
        get() {
            val context = spy(ApplicationProvider.getApplicationContext() as Context)
            val resources = spy<Resources>(context.resources)

            doReturn(resources).`when`<Context>(context).resources
            doReturn(TITLE_ATTR_VALUE).`when`(resources).getString(TITLE_RES_ID)

            return context
        }

    @Before
    fun setUp() {
        transformer = SupportToolbarViewTransformer
    }

    @Test
    fun shouldTransformToolbar() {
        val context = context

        val view = Toolbar(context)
        transformer.apply {
            view.transform(transformer.extractAttributes(view, getAttributeSet(false)))
        }

        assertTrue(view is Toolbar)
        assertEquals((view as Toolbar).title, TITLE_ATTR_VALUE)

        transformer.apply {
            view.transform(transformer.extractAttributes(view, getAttributeSet(true)))
        }

        assertTrue(view is Toolbar)
        assertEquals((view as Toolbar).title, TITLE_ATTR_VALUE)
    }

    /*
    @Test
    fun shouldRejectOtherViewTypes() {
        val context = context
        val attributeSet = getAttributeSet(false)
        val recyclerView = androidx.recyclerview.widget.RecyclerView(context)

        transformer.apply {
            recyclerView.transform(transformer.extractAttributes(view, getAttributeSet(false)))
        }

        assertSame(view, recyclerView)
        verifyZeroInteractions(attributeSet)
    }

     */

    private fun getAttributeSet(withAppPrefix: Boolean): AttributeSet {
        val attributeSet = Mockito.mock(AttributeSet::class.java)
        `when`(attributeSet.attributeCount).thenReturn(TITLE_ATTR_INDEX + 2)

        `when`(attributeSet.getAttributeName(anyInt())).thenReturn("other_attribute")
        `when`(attributeSet.getAttributeName(TITLE_ATTR_INDEX)).thenReturn((if (withAppPrefix) "app:" else "") + TITLE_ATTR_KEY)
        `when`(attributeSet.getAttributeValue(TITLE_ATTR_INDEX)).thenReturn("@$TITLE_RES_ID")
        `when`(attributeSet.getAttributeResourceValue(eq(TITLE_ATTR_INDEX), anyInt())).thenReturn(TITLE_RES_ID)

        return attributeSet
    }

    companion object {
        private const val TITLE_ATTR_INDEX = 3
        private const val TITLE_RES_ID = 0x7f0f0123
        private const val TITLE_ATTR_KEY = "title"
        private const val TITLE_ATTR_VALUE = "TITLE_ATTR_VALUE"
    }
}
