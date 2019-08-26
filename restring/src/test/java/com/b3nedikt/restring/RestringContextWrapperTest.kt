package com.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class RestringContextWrapperTest {

    private lateinit var restringContextWrapper: RestringContextWrapper
    private lateinit var context: Context
    private lateinit var originalResources: Resources

    private lateinit var stringRepository: StringRepository

    private lateinit var transformerManager: ViewTransformerManager

    private val language: String
        get() = Locale.getDefault().language

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        stringRepository = mock()
        transformerManager = mock()

        originalResources = context.resources

        whenever(transformerManager.transform(any(), any())).thenAnswer { i -> i.getArgument(0) }
        restringContextWrapper = RestringContextWrapper.wrap(
                context,
                stringRepository,
                transformerManager
        )
    }

    @Test
    fun shouldWrapResourcesAndGetStringsFromRepository() {
        whenever(stringRepository.getString(language, STR_KEY)).thenReturn(STR_VALUE)

        val real = restringContextWrapper.resources.getString(STR_RES_ID)

        assertEquals(STR_VALUE, real)
    }

    @Test
    fun shouldProvideCustomLayoutInflaterToApplyViewTransformation() {
        val layoutInflater = restringContextWrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        assertTrue(layoutInflater is RestringLayoutInflater)

        val viewGroup = layoutInflater.inflate(R.layout.test_layout, null, false) as ViewGroup

        val captor = argumentCaptor<View>()
        verify(transformerManager, atLeastOnce()).transform(captor.capture(), any())
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            captor.allValues.contains(child)
        }
    }

    private companion object {
        private val STR_RES_ID = R.string.STR_KEY
        private const val STR_KEY = "STR_KEY"
        private const val STR_VALUE = "STR_VALUE"
    }
}