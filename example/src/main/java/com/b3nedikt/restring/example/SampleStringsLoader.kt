package com.b3nedikt.restring.example

import com.b3nedikt.restring.PluralKeyword
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

    override fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> {
        return mapOf(
                "string_array"
                        to
                        arrayOf<CharSequence>("String Array 1 $locale (from restring)",
                                "String Array 2 $locale (from restring)")
        )
    }

    override fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> {
        return mapOf(
                "quantity_string"
                        to
                        mapOf(PluralKeyword.ONE to "%d quantity string $locale (from restring)",
                                PluralKeyword.OTHER to "%d quantity strings $locale (from restring)")
        )
    }
}