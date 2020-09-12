package dev.b3nedikt.restring.repository.observable

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A [MutableMap] which calls back functions when changed
 *
 * @param initialValue the value the map has when initialized, the content of this map is copied
 * into the new [ObservableMap]
 * @param defaultValue value to which a value which has previously not been returned by [get] is
 * set to
 */
internal abstract class ObservableMap<K, V>(
        initialValue: MutableMap<K, V>,
        private val defaultValue: (key: K) -> V?
) : MutableMap<K, V>, ReadWriteProperty<Any?, MutableMap<K, V>> {

    private val delegateMap = mutableMapOf<K, V>().apply {
        putAll(initialValue)
    }

    override val size: Int
        get() = delegateMap.size

    override fun containsKey(key: K): Boolean = delegateMap.containsKey(key)

    override fun containsValue(value: V): Boolean = delegateMap.containsValue(value)

    override fun get(key: K): V? {
        val value = delegateMap[key]

        if (value == null) {
            defaultValue(key)?.let {
                put(key, it)
                return it
            }
        }

        return value
    }

    override fun isEmpty(): Boolean = delegateMap.isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = delegateMap.entries

    override val keys: MutableSet<K>
        get() = delegateMap.keys

    override val values: MutableCollection<V>
        get() = delegateMap.values

    override fun clear() {
        delegateMap.clear()
        afterClear()
    }

    override fun put(key: K, value: V): V? {
        val previousValue = delegateMap.put(key, value)
        afterPut(key, value)
        return previousValue
    }

    override fun putAll(from: Map<out K, V>) {
        delegateMap.putAll(from)
        afterPutAll(from)
    }

    override fun remove(key: K): V? {
        val previousValue = delegateMap.remove(key)
        afterRemove(key)
        return previousValue
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableMap<K, V> {
        return this
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableMap<K, V>) {
        putAll(value)
    }

    protected abstract fun afterPut(key: K, value: V)

    protected abstract fun afterPutAll(from: Map<out K, V>)

    protected abstract fun afterRemove(key: K)

    protected abstract fun afterClear()
}