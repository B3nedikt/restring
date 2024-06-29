package dev.b3nedikt.restring.repository.util

import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import dev.b3nedikt.restring.internal.repository.util.LocaleUtil
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk= [KITKAT, TIRAMISU, UPSIDE_DOWN_CAKE])
class LocaleUtilTest {

    @Test
    fun testAustrianGermanGetsConvertedCorrectly(){
        LocaleUtil.toLanguageTag(
            LocaleUtil.fromLanguageTag("de_AT")
        ) shouldBeEqualTo "de_at"
    }

    @Test
    fun testBelgianDutchGetsConvertedCorrectly(){
        LocaleUtil.toLanguageTag(
            LocaleUtil.fromLanguageTag("nl_BE")
        ) shouldBeEqualTo "nl_be"
    }
}