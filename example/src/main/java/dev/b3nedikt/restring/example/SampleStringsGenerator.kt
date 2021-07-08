package dev.b3nedikt.restring.example

import dev.b3nedikt.restring.PluralKeyword
import java.util.*

/**
 * Generates example strings
 */
object SampleStringsGenerator {

    fun getStrings(locale: Locale): Map<String, CharSequence> {
        return mapOf<String, CharSequence>(
            "title" to "Title $locale (from restring)",
            "subtitle" to "Subtitle $locale (from restring).",
            "ma_string_not_in_strings_xml" to "A string resource not in strings xml $locale",
        )
    }

    fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> {
        return mapOf(
            "string_array"
                    to
                    arrayOf(
                        "String Array 1 $locale (from restring)",
                        "String Array 2 $locale (from restring)"
                    )
        )
    }

    fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> {
        return mapOf(
            "quantity_string"
                    to
                    mapOf(
                        PluralKeyword.ONE to "%d quantity string $locale (from restring)",
                        PluralKeyword.OTHER to "%d quantity strings $locale (from restring)"
                    )
        )
    }
}