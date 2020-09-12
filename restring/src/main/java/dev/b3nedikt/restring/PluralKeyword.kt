package dev.b3nedikt.restring

import android.content.res.Resources
import android.icu.text.PluralRules
import android.os.Build
import java.util.*

/**
 * Keyword of a quantity string. See
 * https://developer.android.com/guide/topics/resources/string-resource#Plurals
 */
enum class PluralKeyword {

    /**
     * When the language requires special treatment of the number 0 (as in Arabic)
     */
    ZERO,

    /**
     * 	When the language requires special treatment of numbers like one (as with the number 1
     * 	in English and most other languages; in Russian, any number ending in 1 but not ending
     * 	in 11 is in this class).
     */
    ONE,

    /**
     * When the language requires special treatment of numbers like two
     * (as with 2 in Welsh, or 102 in Slovenian).
     */
    TWO,

    /**
     * When the language requires special treatment of "small" numbers (as with 2, 3, and 4 in
     * Czech; or numbers ending 2, 3, or 4 but not 12, 13, or 14 in Polish).
     */
    FEW,

    /**
     * When the language requires special treatment of "large" numbers (as with numbers ending
     * 11-99 in Maltese).
     */
    MANY,

    /**
     * When the language does not require special treatment of the given quantity
     * (as with all numbers in Chinese, or 42 in English).
     */
    OTHER;

    companion object {

        /**
         * Creates a [PluralKeyword]
         */
        fun fromQuantity(resources: Resources, locale: Locale, quantity: Int): PluralKeyword =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PluralRules.forLocale(locale).select(quantity.toDouble())
                } else {
                    resources.getQuantityString(R.plurals.quantity_strings, quantity)
                }.run { valueOf(toUpperCase(Locale.ROOT)) }
    }
}