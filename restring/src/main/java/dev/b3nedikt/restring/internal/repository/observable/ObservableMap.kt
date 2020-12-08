package dev.b3nedikt.restring.internal.repository.observable

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A [MutableMap] which calls back functions when changed
 *
 * @param initialMap the value the map has when initialized, this map instance will be lazily
 * queried when needed
 * @param createDefaultValue value to which a value which has previously not been returned by [get] is
 * set to
 */
internal abstract class ObservableMap<K, V>(
        private val initialMap: MutableMap<K, V>?,
        private val createDefaultValue: (key: K) -> V?
) : MutableMap<K, V>, ReadWriteProperty<Any?, MutableMap<K, V>> {

    private val delegateMap = mutableMapOf<K, V?>()

    override val size: Int
        get() = delegateMap.size + (initialMap?.size ?: 0)

    override fun containsKey(key: K): Boolean {
        return delegateMap.containsKey(key) || initialMap?.containsKey(key) == true
    }

    override fun containsValue(value: V): Boolean {
        return delegateMap.containsValue(value) || initialMap?.containsValue(value) == true
    }

    override fun get(key: K): V? {
        var value = delegateMap[key]

        if (delegateMap.containsKey(key).not()) {
            value = initialMap?.get(key)

            value?.let {
                delegateMap[key] = value
                return it
            }

            val defaultValue = createDefaultValue(key)

            if (defaultValue != null) {
                put(key, defaultValue)
            } else {
                delegateMap[key] = value
            }

            return defaultValue
        }

        return value
    }

    override fun isEmpty(): Boolean = delegateMap.isEmpty() && (initialMap?.isEmpty() == true)

    @Suppress("UNCHECKED_CAST")
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = (initialMap?.entries?.plus(delegateMap.entries.toSet())
                ?: delegateMap.entries.toSet())
                as MutableSet<MutableMap.MutableEntry<K, V>>

    override val keys: MutableSet<K>
        get() = (initialMap?.keys?.plus(delegateMap.keys) ?: delegateMap.keys)
                as MutableSet<K>

    @Suppress("UNCHECKED_CAST")
    override val values: MutableCollection<V>
        get() = (initialMap?.values?.plus(delegateMap.values.filterNotNull())
                ?: delegateMap.values.filterNotNull())
                as MutableCollection<V>


    override fun clear() {
        delegateMap.clear()
        initialMap?.clear()
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
        var previousValue = delegateMap.remove(key)

        if (previousValue == null) {
            previousValue = initialMap?.remove(key)
        }

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