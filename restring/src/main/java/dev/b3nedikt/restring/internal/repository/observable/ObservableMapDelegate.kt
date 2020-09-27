package dev.b3nedikt.restring.internal.repository.observable

import kotlin.properties.ReadWriteProperty

/**
 * Returns a property delegate for a mutable map that where all read/write operations can
 * be observed.
 *
 * @param initialValue the value the map has when initialized, the content of this map is copied
 * into the new [ObservableMap]
 * @param defaultValue value for a given key to which a value which has previously not been
 * returned by [get] is set to
 * @param afterPut triggered after a single new value has been put into the map
 * @param afterPutAll triggered after multiple new values have been put into the map
 * @param afterRemove triggered after a single value has been removed from the map
 * @param afterClear triggered after all values have been removed from the map
 */
internal inline fun <K, V> observableMap(
        initialValue: MutableMap<K, V>? = null,
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