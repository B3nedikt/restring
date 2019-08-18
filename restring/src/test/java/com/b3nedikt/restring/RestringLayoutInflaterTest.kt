package com.b3nedikt.restring

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16, 19, 21, 23, 24, 26])
class RestringLayoutInflaterTest {

    private lateinit var transformerManager: ViewTransformerManager
    private lateinit var restringLayoutInflater: RestringLayoutInflater

    @Before
    fun setUp() {

        transformerManager = mock()

        whenever(transformerManager.transform(any(), any()))
                .thenAnswer{LinearLayout(ApplicationProvider.getApplicationContext())}

        ApplicationProvider.getApplicationContext<Context>().setTheme(R.style.Theme_AppCompat)
        restringLayoutInflater = RestringLayoutInflater(
                LayoutInflater.from(ApplicationProvider.getApplicationContext()),
                ApplicationProvider.getApplicationContext(),
                transformerManager,
                false
        )
    }

    @Test
    fun shouldTransformViewsOnInflatingLayouts() {
        val viewGroup = restringLayoutInflater.inflate(R.layout.test_layout, null, false) as ViewGroup

        val captor = argumentCaptor<View>()
        verify(transformerManager, atLeastOnce()).transform(captor.capture(), any())
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            captor.allValues.contains(child)
        }
    }
}