package dev.b3nedikt.restring.internal

import dev.b3nedikt.restring.ResourcesFallbackStrategy
import java.util.Locale

/**
 * Default implementation of [ResourcesFallbackStrategy]
 */
internal object DefaultResourcesFallbackStrategy : ResourcesFallbackStrategy {

    override fun getFallbackStringLocale(locale: Locale, stringKey: String): Locale? {
        if (locale.country.isNotEmpty()) {
            return Locale(locale.language)
        }
        return null
    }

    override fun getFallbackQuantityStringLocale(locale: Locale, stringKey: String): Locale? {
        if (locale.country.isNotEmpty()) {
            return Locale(locale.language)
        }
        return null
    }

    override fun getFallbackStringArrayLocale(locale: Locale, stringKey: String): Locale? {
        if (locale.country.isNotEmpty()) {
            return Locale(locale.language)
        }
        return null
    }
}
