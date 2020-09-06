package dev.b3nedikt.restring.repository.observable

import kotlin.properties.ReadWriteProperty

/**
 * Returns a property delegate for a mutable map that where all read/write operations can
 * be observed.
 */
internal inline fun <K, V> observableMap(
        initialValue: MutableMap<K, V>,
        noinline defaultValue: (key: K) -> V? = { _: K -> null },
        crossinline afterPut: (key: K, value: V) -> Unit = { _: K, _: V -> },
        crossinline afterPutAll: (from: Map<out K, V>) -> Unit = {},
        crossinline afterRemove: (key: K) -> Unit = {},
        crossinline afterClear: () -> Unit = {}
): ReadWriteProperty<Any?, MutableMap<K, V>> =
        object : ObservableMap<K, V>(initialValue, defaultValue) {
            override fun afterPut(key: K, value: V) = afterPut(key, value)
            override fun afterPutAll(from: Map<out K, V>) = afterPutAll(from)
            override fun afterRemove(key: K) = afterRemove(key)
            override fun afterClear() = afterClear()
        }