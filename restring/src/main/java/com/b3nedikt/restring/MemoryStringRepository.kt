package com.b3nedikt.restring

import java.util.*

/**
 * A StringRepository which keeps the strings ONLY in memory.
 *
 *
 * it's not ThreadSafe.
 */
internal class MemoryStringRepository : StringRepository {

    private val strings = mutableMapOf<Locale, MutableMap<String, CharSequence>>()

    override var supportedLocales: Set<Locale> = strings.keys

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        this.strings[locale] = strings.toMutableMap()
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        if (!strings.containsKey(locale)) {
            strings[locale] = mutableMapOf()
        }
        strings[locale]?.put(key, value)
    }

    override fun getString(locale: Locale, key: String) = strings[locale]?.get(key)

    override fun getStrings(locale: Locale) = strings[locale]?.toMap() ?: mapOf()
}