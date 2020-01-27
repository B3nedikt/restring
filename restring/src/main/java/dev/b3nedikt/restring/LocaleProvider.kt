package dev.b3nedikt.restring

import java.util.*

/**
 * Manages the [Locale] restring should currently display string resources in.
 */
interface LocaleProvider {

    /**
     * If the [Locale] returned by [currentLocale] is different from [Locale.getDefault].
     */
    val isInitial: Boolean

    /**
     * The [Locale] restring should currently display string resources in.
     */
    var currentLocale: Locale
}