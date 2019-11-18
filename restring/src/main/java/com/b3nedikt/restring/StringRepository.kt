package com.b3nedikt.restring

import java.util.*

/**
 * Repository of strings.
 */
interface StringRepository {

    /**
     * The [Locale]s supported by the repository
     */
    var supportedLocales: Set<Locale>

    /**
     * Get a string for a locale & key.
     *
     * @param locale the locale of the string.
     * @param key      the string resource id.
     * @return the string if exists, otherwise NULL.
     */
    fun getString(locale: Locale, key: String): CharSequence?

    /**
     * Set a single string(key, value) for a specific locale.
     *
     * @param locale the string belongs to.
     * @param key      the key of the string which is the string resource id.
     * @param value    the new string.
     */
    fun setString(locale: Locale, key: String, value: CharSequence)

    /**
     * Get all strings for a specific locale.
     *
     * @param locale the locale of the strings.
     * @return the map of string key & values. return empty map if there's no.
     */
    fun getStrings(locale: Locale): Map<String, CharSequence>

    /**
     * Set strings(key, value) for a specific locale.
     *
     * @param locale the strings belongs to.
     * @param strings  new strings for the locale.
     */
    fun setStrings(locale: Locale, strings: Map<String, CharSequence>)

    /**
     * Get a quantity string for a locale & key.
     *
     * @param locale the locale of the quantity string.
     * @param key      the quantity string resource id.
     * @return the quantity string if exists, otherwise NULL.
     */
    fun getQuantityString(locale: Locale, key: String): Map<PluralKeyword, CharSequence>?

    /**
     * Set a single quantity string(key, value) for a specific locale.
     *
     * @param locale the quantity string belongs to.
     * @param key      the key of the quantity string which is the quantity string resource id.
     * @param quantityString    the new quantity string.
     */
    fun setQuantityString(locale: Locale, key: String, quantityString: Map<PluralKeyword, CharSequence>)

    /**
     * Get all quantity string for a specific locale.
     *
     * @param locale the locale of the quantity string.
     * @return the map of quantity string key & values. return empty map if there's no.
     */
    fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>>

    /**
     * Set quantity string(key, value) for a specific locale.
     *
     * @param locale the quantity string belongs to.
     * @param quantityStrings  new quantity string for the locale.
     */
    fun setQuantityStrings(locale: Locale, quantityStrings: Map<String, Map<PluralKeyword, CharSequence>>)

    /**
     * Get a string array for a locale & key.
     *
     * @param locale the locale of the string array.
     * @param key      the string array resource id.
     * @return the string array if exists, otherwise NULL.
     */
    fun getStringArray(locale: Locale, key: String): Array<CharSequence>?

    /**
     * Set a string array(key, value) for a specific locale.
     *
     * @param locale the string array belongs to.
     * @param key      the key of the string array which is the string array resource id.
     * @param stringArray the new string array.
     */
    fun setStringArray(locale: Locale, key: String, stringArray: Array<CharSequence>)

    /**
     * Get all string arrays for a specific locale.
     *
     * @param locale the locale of the string array.
     * @return the map of string array key & values. return empty map if there's no.
     */
    fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>>

    /**
     * Set string arrays(key, value) for a specific locale.
     *
     * @param locale the string array belongs to.
     * @param stringArrays new string arrays for the locale.
     */
    fun setStringArrays(locale: Locale, stringArrays: Map<String, Array<CharSequence>>)
}
