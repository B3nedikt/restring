package com.b3nedikt.restring

import java.util.*

/**
 * Repository of strings.
 */
internal interface StringRepository {

    /**
     * The [Locale]s supported by the repository
     */
    var supportedLocales: Set<Locale>

    /**
     * Set strings(key, value) for a specific locale.
     *
     * @param locale the strings belongs to.
     * @param strings  new strings for the locale.
     */
    fun setStrings(locale: Locale, strings: Map<String, CharSequence>)

    /**
     * set a single string(key, value) for a specific locale.
     *
     * @param locale the string belongs to.
     * @param key      the key of the string which is the string resource id.
     * @param value    the new string.
     */
    fun setString(locale: Locale, key: String, value: CharSequence)

    /**
     * Get a string for a locale & key.
     *
     * @param locale the locale of the string.
     * @param key      the string resource id.
     * @return the string if exists, otherwise NULL.
     */
    fun getString(locale: Locale, key: String): CharSequence?

    /**
     * Get all strings for a specific locale.
     *
     * @param locale the locale of the strings.
     * @return the map of string key & values. return empty map if there's no.
     */
    fun getStrings(locale: Locale): Map<String, CharSequence>
}
