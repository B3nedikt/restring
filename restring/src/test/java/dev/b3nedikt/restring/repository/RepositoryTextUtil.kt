package dev.b3nedikt.restring.repository

import androidx.core.text.HtmlCompat
import dev.b3nedikt.restring.PluralKeyword

internal fun generateStrings(count: Int): Map<String, String> {
    val strings = mutableMapOf<String, String>()
    for (i in 0 until count) {
        strings["key$i"] = "value$i"
    }
    return strings
}

internal fun generateStringArrays(count: Int): Map<String, Array<CharSequence>> {
    val stringArrays = mutableMapOf<String, Array<CharSequence>>()

    for (i in 0 until count) {
        stringArrays["key$i"] = generateStringArray()
    }
    return stringArrays
}

internal fun generateStringArray(): Array<CharSequence> {
    return generateSequence { STRING_VALUE }
            .take(10)
            .toList()
            .toTypedArray()
}

internal fun generateTextArray(): Array<CharSequence> {
    return generateSequence { TEXT_VALUE }
            .take(10)
            .toList()
            .toTypedArray()
}

internal fun generateQuantityStrings(count: Int): Map<String, Map<PluralKeyword, CharSequence>> {
    val quantityStrings = mutableMapOf<String, Map<PluralKeyword, CharSequence>>()

    for (i in 0 until count) {
        quantityStrings["key$i"] = generateQuantityString()
    }
    return quantityStrings
}

internal fun generateQuantityString(): MutableMap<PluralKeyword, CharSequence> {
    return PluralKeyword
            .values()
            .associate { pluralKeyword -> pluralKeyword to STRING_VALUE as CharSequence }
            .toMutableMap()
}

internal fun generateQuantityText(): MutableMap<PluralKeyword, CharSequence> {
    return PluralKeyword
            .values()
            .associate { pluralKeyword -> pluralKeyword to TEXT_VALUE as CharSequence }
            .toMutableMap()
}

internal const val STRING_VALUE = "value"
internal val TEXT_VALUE = HtmlCompat.fromHtml("STR <b>value</b>", HtmlCompat.FROM_HTML_MODE_COMPACT)