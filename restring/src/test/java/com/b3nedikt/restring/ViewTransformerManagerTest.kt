package com.b3nedikt.restring

import android.util.AttributeSet
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ViewTransformerManagerTest {

    private lateinit var transformerManager: ViewTransformerManager

    @Before
    fun setUp() {
        transformerManager = ViewTransformerManager()
    }

    @Test
    fun shouldTransformView() {
        val textView = TextView(ApplicationProvider.getApplicationContext())

        val transformer = mock<ViewTransformerManager.Transformer>()

        whenever(transformer.viewType).thenReturn(TextView::class.java)
        whenever(transformer.transform(any(), any())).thenReturn(textView)

        transformerManager.registerTransformer(transformer)

        val transformedView = transformerManager.transform(
                TextView(ApplicationProvider.getApplicationContext()),
                Mockito.mock(AttributeSet::class.java)
        )

        assertSame(textView, transformedView)
    }
}