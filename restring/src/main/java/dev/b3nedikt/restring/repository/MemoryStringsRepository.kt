package dev.b3nedikt.restring.repository

import dev.b3nedikt.restring.MutableStringRepository
import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.internal.repository.observable.observableMap
import java.util.*

/**
 * A [MutableStringRepository] that only keeps strings in memory
 */
class MemoryStringsRepository : MutableStringRepository {

    override val supportedLocales: Set<Locale>
        get() = strings.keys + quantityStrings.keys + stringArrays.keys

    override val strings: MutableMap<Locale, MutableMap<String, CharSequence>> by observableMap(
            defaultValue = { mutableMapOf() }
    )

    override val quantityStrings: MutableMap<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>> by observableMap(
            defaultValue = { mutableMapOf() }
    )

    override val stringArrays: MutableMap<Locale, MutableMap<String, Array<CharSequence>>> by observableMap(
            defaultValue = { mutableMapOf() }
    )
}