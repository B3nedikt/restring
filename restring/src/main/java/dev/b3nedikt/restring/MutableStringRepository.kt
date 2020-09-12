package dev.b3nedikt.restring

import java.util.*

/**
 * A mutable [StringRepository]
 */
interface MutableStringRepository : StringRepository {

    override val supportedLocales: Set<Locale>

    override val strings: MutableMap<Locale, MutableMap<String, CharSequence>>

    override val quantityStrings: MutableMap<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>>

    override val stringArrays: MutableMap<Locale, MutableMap<String, Array<CharSequence>>>
}