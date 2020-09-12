package dev.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.b3nedikt.restring.internal.RestringContextWrapper
import org.amshove.kluent.shouldBeEqualTo
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

    private val locale = Locale.getDefault()

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
        whenever(stringRepository.strings).thenReturn(
                mutableMapOf(locale to mutableMapOf(STR_KEY to STR_VALUE as CharSequence))
        )

        val real = restringContextWrapper.resources.getString(STR_RES_ID)

        STR_VALUE shouldBeEqualTo real
    }

    private companion object {
        private val STR_RES_ID = R.string.STR_KEY
        private const val STR_KEY = "STR_KEY"
        private const val STR_VALUE = "STR_VALUE"
    }
}