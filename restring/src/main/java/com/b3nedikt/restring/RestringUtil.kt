package com.b3nedikt.restring

import java.util.Locale

internal object RestringUtil {

    val currentLanguage: String
        get() = Locale.getDefault().language
}
