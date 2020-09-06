package dev.b3nedikt.restring.repository.persistent

interface KeyValueStore<K, V> {

    fun find(key: K): V?

    fun findAll(): Map<out K, V>

    fun save(key: K, value: V)

    fun saveAll(from: Map<out K, V>)

    fun delete(key: K)

    fun deleteAll()
}