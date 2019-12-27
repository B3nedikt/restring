package dev.b3nedikt.restring

import java.util.*

internal object LocaleUtil {

    fun toSimpleLanguageTag(locale: Locale): String {
        val language = locale.language
        val country = locale.country

        if (language.isNotEmpty() && country.isNotEmpty()) {
            return "$language-$country"
        }
        return language
    }

    fun fromSimpleLanguageTag(locale: String): Locale {

        if (locale.contains("-")) {
            val language = locale.split("-")[0]
            val country = locale.split("-")[1]

            return Locale(language, country)
        }

        return Locale(locale)
    }
}