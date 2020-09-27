package dev.b3nedikt.restring.internal.repository.model

import android.os.Build
import dev.b3nedikt.restring.repository.generateQuantityString
import dev.b3nedikt.restring.repository.generateQuantityText
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class QuantityStringTest {

    @Test
    fun jsonConversionWorksForQuantityStrings() {
        val quantityString = QuantityString(generateQuantityString(), isText = false)

        QuantityString.fromJson(quantityString.toJson()) shouldBeEqualTo quantityString
    }

    @Test
    fun jsonConversionWorksForQuantityTexts() {
        val quantityText = QuantityString(generateQuantityText(), isText = true)

        QuantityString.fromJson(quantityText.toJson()).value.map { it.toString() } shouldBeEqualTo
                quantityText.value.map { it.toString() }
    }
}