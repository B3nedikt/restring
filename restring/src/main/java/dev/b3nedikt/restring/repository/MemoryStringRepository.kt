package dev.b3nedikt.restring.repository

import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.StringRepository
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
        initStringsAndLocales(locale)
        this.strings[locale] = strings.toMutableMap()
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        initStringsAndLocales(locale)
        this.strings[locale]?.put(key, value)
    }

    override fun getString(locale: Locale, key: String) = strings[locale]?.get(key)

    override fun getStrings(locale: Locale) = strings[locale]?.toMap() ?: mapOf()

    override fun getQuantityString(locale: Locale, key: String) = quantityStrings[locale]?.get(key)

    override fun setQuantityString(
            locale: Locale,
            key: String,
            quantityString: Map<PluralKeyword, CharSequence>) {
        initQuantityStringsAndLocales(locale)
        this.quantityStrings[locale]?.put(key, quantityString)
    }

    override fun getQuantityStrings(locale: Locale) = quantityStrings[locale]?.toMap() ?: mapOf()

    override fun setQuantityStrings(
            locale: Locale,
            quantityStrings: Map<String, Map<PluralKeyword, CharSequence>>) {
        initQuantityStringsAndLocales(locale)
        this.quantityStrings[locale] = quantityStrings.toMutableMap()
    }

    override fun getStringArray(locale: Locale, key: String) = getStringArrays(locale)[key]

    override fun setStringArray(locale: Locale, key: String, stringArray: Array<CharSequence>) {
        initStringArraysAndLocales(locale)
        this.stringArrays[locale]?.put(key, stringArray)
    }

    override fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> =
            stringArrays[locale] ?: mapOf()

    override fun setStringArrays(locale: Locale, stringArrays: Map<String, Array<CharSequence>>) {
        initStringArraysAndLocales(locale)
        this.stringArrays[locale] = stringArrays.toMutableMap()
    }

    private fun initStringsAndLocales(locale: Locale) {
        if (!this.strings.containsKey(locale)) {
            this.strings[locale] = mutableMapOf()
        }
        initLocales(locale)
    }

    private fun initQuantityStringsAndLocales(locale: Locale) {
        if (!this.quantityStrings.containsKey(locale)) {
            this.quantityStrings[locale] = mutableMapOf()
        }
        initLocales(locale)
    }

    private fun initStringArraysAndLocales(locale: Locale) {
        if (!this.stringArrays.containsKey(locale)) {
            this.stringArrays[locale] = mutableMapOf()
        }
        initLocales(locale)
    }

    private fun initLocales(locale: Locale) {
        if (supportedLocales.contains(locale).not()) {
            supportedLocales = supportedLocales.toMutableSet().apply {
                add(locale)
            }
        }
    }
}