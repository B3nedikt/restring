package com.b3nedikt.restring.repository

import com.b3nedikt.restring.PluralKeyword
import com.b3nedikt.restring.StringRepository
import java.util.*

/**
 * A StringRepository which keeps the strings ONLY in memory.
 *
 *
 * it's not ThreadSafe.
 */
class MemoryStringRepository : StringRepository {

    private val strings = mutableMapOf<Locale, MutableMap<String, CharSequence>>()
    private val quantityStrings = mutableMapOf<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>>()
    private val stringArrays = mutableMapOf<Locale, MutableMap<String, Array<CharSequence>>>()

    override var supportedLocales: Set<Locale> = emptySet()

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        if (!this.strings.containsKey(locale)) {
            this.strings[locale] = mutableMapOf()
        }
        this.strings[locale] = strings.toMutableMap()
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        setStrings(locale, mapOf(key to value))
    }

    override fun getString(locale: Locale, key: String) = strings[locale]?.get(key)

    override fun getStrings(locale: Locale) = strings[locale]?.toMap() ?: mapOf()

    override fun getQuantityString(locale: Locale, key: String) = quantityStrings[locale]?.get(key)

    override fun setQuantityString(
            locale: Locale,
            key: String,
            quantityString: Map<PluralKeyword, CharSequence>) {
        setQuantityStrings(locale, mapOf(key to quantityString))
    }

    override fun getQuantityStrings(locale: Locale) = quantityStrings[locale]?.toMap() ?: mapOf()

    override fun setQuantityStrings(
            locale: Locale,
            quantityStrings: Map<String, Map<PluralKeyword, CharSequence>>) {
        if (!this.quantityStrings.containsKey(locale)) {
            this.quantityStrings[locale] = mutableMapOf()
        }
        this.quantityStrings[locale] = quantityStrings.toMutableMap()
    }

    override fun getStringArray(locale: Locale, key: String) = getStringArrays(locale)[key]

    override fun setStringArray(locale: Locale, key: String, stringArray: Array<CharSequence>) {
        setStringArrays(locale, mapOf(key to stringArray))
    }

    override fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> =
            stringArrays[locale] ?: mapOf()

    override fun setStringArrays(locale: Locale, stringArrays: Map<String, Array<CharSequence>>) {
        if (!this.stringArrays.containsKey(locale)) {
            this.stringArrays[locale] = mutableMapOf()
        }
        this.stringArrays[locale] = stringArrays.toMutableMap()
    }
}