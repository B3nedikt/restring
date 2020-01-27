package dev.b3nedikt.restring

import java.util.*

/**
 * Default implementation of [LocaleProvider]
 */
object DefaultLocaleProvider : LocaleProvider {

    override var isInitial = true

    override var currentLocale: Locale = Locale.getDefault()
        get() {
            if (isInitial) {
                return Locale.getDefault()
            }
            return field
        }
        set(value) {
            field = value
            isInitial = false
        }
}