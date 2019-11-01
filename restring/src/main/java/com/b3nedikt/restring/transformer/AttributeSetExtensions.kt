package com.b3nedikt.restring.transformer

import android.util.AttributeSet

fun AttributeSet.forEach(action: (index: Int) -> Unit) {
    (0 until attributeCount).forEach(action)
}

fun <K, V> AttributeSet.associate(transform: (index: Int) -> Pair<K, V>): Map<K, V>  {
    return (0 until attributeCount).associate(transform)
}