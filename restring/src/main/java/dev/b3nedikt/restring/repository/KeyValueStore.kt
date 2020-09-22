package dev.b3nedikt.restring.repository

/**
 * Stores data as key-value pairs
 */
interface KeyValueStore<K, V> {

    /**
     * Returns the value for a given key
     */
    fun find(key: K): V?

    /**
     * Returns all values managed in this [KeyValueStore]
     */
    fun findAll(): Map<out K, V>

    /**
     * Persists the key value pair
     */
    fun save(key: K, value: V)

    /**
     * Saves all key value pairs from [from]
     */
    fun saveAll(from: Map<out K, V>)

    /**
     * Deletes the value with the given [key]
     */
    fun delete(key: K)

    /**
     * Deletes all values managed by this [KeyValueStore]
     */
    fun deleteAll()
}