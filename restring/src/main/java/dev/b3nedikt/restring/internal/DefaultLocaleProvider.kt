package dev.b3nedikt.restring.internal

import dev.b3nedikt.restring.LocaleProvider
import java.util.*

/**
 * Default implementation of [LocaleProvider]
 */
internal object DefaultLocaleProvider : LocaleProvider {

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