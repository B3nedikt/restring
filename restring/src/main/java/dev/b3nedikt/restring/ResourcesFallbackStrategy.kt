package dev.b3nedikt.restring

import java.util.Locale

/**
 * A [ResourcesFallbackStrategy] defines the way restring decides what to do if a string
 * cannot be found in it's [Restring.stringRepository]
 */
interface ResourcesFallbackStrategy {

    /**
     * Decides what to do if a string cannot be found in a [StringRepository]s strings
     */
    fun getFallbackStringLocale(locale: Locale, stringKey: String): Locale?

    /**
     * Decides what to do if a string cannot be found in a [StringRepository]s quantityStrings
     */
    fun getFallbackQuantityStringLocale(locale: Locale, stringKey: String): Locale?

    /**
     * Decides what to do if a string cannot be found in a [StringRepository]s stringArrays
     */
    fun getFallbackStringArrayLocale(locale: Locale, stringKey: String): Locale?
}