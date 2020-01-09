package dev.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RestringContextWrapperTest {

    private lateinit var restringContextWrapper: RestringContextWrapper
    private lateinit var context: Context
    private lateinit var originalResources: Resources

    private lateinit var stringRepository: StringRepository

    private val locale: Locale
        get() = Locale.getDefault()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        stringRepository = mock()

        whenever(stringRepository.supportedLocales).thenReturn(setOf(locale))

        originalResources = context.resources

        restringContextWrapper = RestringContextWrapper.wrap(
                context,
                stringRepository
        )
    }

    @Test
    fun shouldWrapResourcesAndGetStringsFromRepository() {
        whenever(stringRepository.getString(locale, STR_KEY)).thenReturn(STR_VALUE)

        val real = restringContextWrapper.resources.getString(STR_RES_ID)

        assertEquals(STR_VALUE, real)
    }

    private companion object {
        private val STR_RES_ID = R.string.STR_KEY
        private const val STR_KEY = "STR_KEY"
        private const val STR_VALUE = "STR_VALUE"
    }
}