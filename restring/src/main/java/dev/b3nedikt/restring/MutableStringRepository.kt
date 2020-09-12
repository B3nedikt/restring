package dev.b3nedikt.restring

import java.util.*

interface MutableStringRepository : StringRepository {

    /**
     * The [Locale]s supported by the repository
     */
    override val supportedLocales: Set<Locale>

    /**
     * The strings managed by the repository
     */
    override val strings: MutableMap<Locale, MutableMap<String, CharSequence>>

    /**
     * The quantity strings managed by the repository
     */
    override val quantityStrings: MutableMap<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>>

    /**
     * The string arrays managed by the repository
     */
    override val stringArrays: MutableMap<Locale, MutableMap<String, Array<CharSequence>>>
}