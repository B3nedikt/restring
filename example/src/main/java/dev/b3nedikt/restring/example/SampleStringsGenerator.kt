package dev.b3nedikt.restring.example

import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.example.Locales.LOCALE_AUSTRIAN_GERMAN
import java.util.*

/**
 * Generates example strings
 */
object SampleStringsGenerator {

    fun getStrings(locale: Locale): Map<String, CharSequence> {
        val map = mutableMapOf<String, CharSequence>()
        when (locale) {
            Locale.ENGLISH -> {
                map["title"] = "Title (from restring)."
                map["subtitle"] = "Subtitle (from restring)."
                map["a_string_not_in_strings_xml"] = "A string resource not in strings xml $locale"
            }
            Locale.US -> {
                map["title"] = "Title US (from restring)."
                map["subtitle"] = "Subtitle US (from restring)."
                map["a_string_not_in_strings_xml"] = "A string resource not in strings xml $locale"
            }
            LOCALE_AUSTRIAN_GERMAN -> {
                map["title"] = "Titel (aus restring)."
                map["subtitle"] = "Untertitel (aus restring)."
                map["a_string_not_in_strings_xml"] = "A string resource not in strings xml $locale"
            }
        }
        return map
    }

    fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> {
        return mapOf(
                "string_array"
                        to
                        arrayOf("String Array 1 $locale (from restring)",
                                "String Array 2 $locale (from restring)")
        )
    }

    fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> {
        return mapOf(
                "quantity_string"
                        to
                        mapOf(PluralKeyword.ONE to "%d quantity string $locale (from restring)",
                                PluralKeyword.OTHER to "%d quantity strings $locale (from restring)")
        )
    }
}