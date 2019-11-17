package com.b3nedikt.restring.repository

import com.b3nedikt.restring.PluralKeyword

data class QuantityString(
        val key: String,
        val value: Map<PluralKeyword, CharSequence>,
        val isText: Boolean
) {


}