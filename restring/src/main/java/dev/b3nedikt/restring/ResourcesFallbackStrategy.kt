package dev.b3nedikt.restring

import java.util.Locale

interface ResourcesFallbackStrategy {

    fun getFallbackStringLocale(locale: Locale, stringKey: String): Locale?

    fun getFallbackQuantityStringLocale(locale: Locale, stringKey: String): Locale?

    fun getFallbackStringArrayLocale(locale: Locale, stringKey: String): Locale?
}