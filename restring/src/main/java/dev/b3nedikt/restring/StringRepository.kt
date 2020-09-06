package dev.b3nedikt.restring

import java.util.*

/**
 * Repository of strings.
 */
interface StringRepository {

    /**
     * The [Locale]s supported by the repository
     */
    val supportedLocales: Set<Locale>

    /**
     * The strings managed by the repository
     */
    val strings: MutableMap<Locale, MutableMap<String, CharSequence>>

    /**
     * The quantity strings managed by the repository
     */
    val quantityStrings: MutableMap<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>>

    /**
     * The string arrays managed by the repository
     */
    val stringArrays: MutableMap<Locale, MutableMap<String, Array<CharSequence>>>
}
