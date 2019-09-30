package com.b3nedikt.restring

import java.util.*

internal object LocaleUtil{

    fun toSimpleLanguageTag(locale: Locale): String {
        return locale.language + "-" + locale.country
    }

    fun fromSimpleLanguageTag(locale: String): Locale {
        val language = locale.split("-")[0]
        val country = locale.split("-")[1]

        return Locale(language, country)
    }
}

