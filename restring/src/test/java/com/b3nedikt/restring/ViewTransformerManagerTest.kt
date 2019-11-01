package com.b3nedikt.restring

import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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

        val transformer = mock<ViewTransformer<TextView>>()

        whenever(transformer.viewType).thenReturn(TextView::class.java)

        transformerManager.registerTransformer(transformer)

        val transformedView = transformerManager.transform(textView, mock())

        assertSame(textView, transformedView)
    }
}