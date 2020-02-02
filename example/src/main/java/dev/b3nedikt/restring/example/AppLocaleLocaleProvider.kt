package dev.b3nedikt.restring.example

import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.restring.LocaleProvider

object AppLocaleLocaleProvider : LocaleProvider {

    override var isInitial = AppLocale.isInitial

    override var currentLocale
        get() = AppLocale.desiredLocale
        set(value) {
            AppLocale.desiredLocale = value
        }
}