package dev.b3nedikt.restring.internal.repository.model

import android.os.Build
import dev.b3nedikt.restring.repository.STRING_VALUE
import dev.b3nedikt.restring.repository.TEXT_VALUE
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class StringTest {

    @Test
    fun jsonConversionWorksForStringResources() {
        val stringResource = StringResource(STRING_VALUE, isText = false)

        StringResource.fromJson(stringResource.toJson()) shouldBeEqualTo stringResource
    }

    @Test
    fun jsonConversionWorksForTextResources() {
        val textResource = StringResource(TEXT_VALUE, isText = true)

        println(textResource.toJson())
        StringResource.fromJson(textResource.toJson()).value.toString() shouldBeEqualTo
                textResource.value.toString()
    }
}