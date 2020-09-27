package dev.b3nedikt.restring.internal.repository.model

import android.os.Build
import dev.b3nedikt.restring.repository.generateStringArray
import dev.b3nedikt.restring.repository.generateTextArray
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class StringArrayTest {

    @Test
    fun jsonConversionWorksForStringArrays() {
        val stringArray = StringArray(generateStringArray().toList(), isText = false)

        StringArray.fromJson(stringArray.toJson()) shouldBeEqualTo stringArray
    }

    @Test
    fun jsonConversionWorksForTextArrays() {
        val textArray = StringArray(generateTextArray().toList(), isText = true)

        StringArray.fromJson(textArray.toJson()).value.map { it.toString() } shouldBeEqualTo
                textArray.value.map { it.toString() }
    }
}