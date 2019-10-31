package com.b3nedikt.restring.example

import com.b3nedikt.restring.Restring
import java.util.*

/**
 * This is just a really simple sample of strings loader.
 * in real applications, you might call an API to get your strings.
 *
 *
 * All overridden methods will be called on background thread.
 */
class SampleStringsLoader : Restring.StringsLoader {

    override val locales = APP_LOCALES

    override fun getStrings(locale: Locale): Map<String, CharSequence> {
        val map = mutableMapOf<String, CharSequence>()
        when (locale) {
            Locale.ENGLISH -> {
                map["title"] = "Title (from restring)."
                map["subtitle"] = "Subtitle (from restring)."
            }
            Locale.US -> {
                map["title"] = "Title US (from restring)."
                map["subtitle"] = "Subtitle US (from restring)."
            }
            Locales.LOCALE_AUSTRIAN_GERMAN -> {
                map["title"] = "Titel (aus restring)."
                map["subtitle"] = "Untertitel (aus restring)."
            }
        }
        return map
    }
}