package dev.b3nedikt.restring.internal.repository.persistent

import dev.b3nedikt.restring.repository.KeyValueStore
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A [MutableMap] which persists all calls to it by acting as a [KeyValueStore]
 */
internal interface PersistentMap<K, V>
    : MutableMap<K, V>, ReadOnlyProperty<Any?, MutableMap<K, V>>, KeyValueStore<K, V> {

    override val size: Int
        get() = findAll().size

    override fun containsKey(key: K): Boolean = findAll().containsKey(key)

    override fun containsValue(value: V): Boolean = findAll().containsValue(value)

    override fun get(key: K): V? = find(key)

    override fun isEmpty(): Boolean = findAll().isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = findAll().toMutableMap().entries

    override val keys: MutableSet<K>
        get() = findAll().toMutableMap().keys

    override val values: MutableCollection<V>
        get() = findAll().toMutableMap().values

    override fun clear() {
        deleteAll()
    }

    override fun put(key: K, value: V): V? {
        val previousValue = find(key)
        save(key, value)
        return previousValue
    }

    override fun putAll(from: Map<out K, V>) {
        saveAll(from)
    }

    override fun remove(key: K): V? {
        val previousValue = find(key)
        delete(key)
        return previousValue
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableMap<K, V> {
        return this
    }
}