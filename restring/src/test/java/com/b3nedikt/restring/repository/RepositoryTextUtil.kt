package com.b3nedikt.restring.repository

internal fun generateStrings(count: Int): Map<String, String> {
    val strings = mutableMapOf<String, String>()
    for (i in 0 until count) {
        strings["key$i"] = "value$i"
    }
    return strings
}

internal fun generateStringArrays(count: Int): Map<String, Array<CharSequence>> {
    val stringArrays = mutableMapOf<String, Array<CharSequence>>()

    for (i in 0 until count) {
        stringArrays["key$i"] = "value$i ".repeat(10).trimEnd().split( " ")
                .map { it as CharSequence }
                .toTypedArray()
    }
    return stringArrays
}