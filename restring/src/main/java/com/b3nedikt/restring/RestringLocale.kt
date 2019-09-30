package com.b3nedikt.restring

import java.util.*

object RestringLocale {

    internal var isInitial = true

    var currentLocale: Locale = Locale.getDefault()
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
