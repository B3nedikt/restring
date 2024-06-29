package dev.b3nedikt.restring.internal.repository.util

import android.os.Build
import java.util.Locale

/**
 * Util class for converting [Locale]s to their language tag and vice versa, uses the normal java
 * implementation on Lollipop and above.
 */
internal object LocaleUtil {

    fun toLanguageTag(locale: Locale): String {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val languageTag = locale.toLanguageTag()
            if (languageTag != "und") {
                return languageTag
            }
        }

        val language = locale.language
        val country = locale.country

        if (language.isNotEmpty() && country.isNotEmpty()) {
            return "$language-$country"
        }
        return language
    }

    fun fromLanguageTag(locale: String): Locale {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val languageTag = Locale.forLanguageTag(locale)
            if (languageTag.language != "") {
                return languageTag
            }
        }

        if (locale.contains("-")) {
            val language = locale.split("-")[0]
            val country = locale.split("-")[1]

            return Locale(language, country)
        }

        return Locale(locale)
    }
}