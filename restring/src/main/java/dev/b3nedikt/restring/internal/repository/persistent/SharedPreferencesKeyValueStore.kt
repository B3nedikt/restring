package dev.b3nedikt.restring.internal.repository.persistent

import android.content.SharedPreferences
import dev.b3nedikt.restring.repository.KeyValueStore
import dev.b3nedikt.restring.internal.repository.serializer.Serializer

/**
 * [KeyValueStore] which stores key/value pairs in the shared preferences
 */
internal class SharedPreferencesKeyValueStore<V>(
        private val sharedPreferences: SharedPreferences,
        private val serializer: Serializer<V, String>,
) : KeyValueStore<String, V> {

    override fun find(key: String): V? {
        val value = sharedPreferences.getString(key, null) ?: return null
        return serializer.deserialize(value)
    }

    override fun findAll(): Map<out String, V> {
        return sharedPreferences.all.mapValues { serializer.deserialize(it.value as String) }
    }

    override fun save(key: String, value: V) {
        sharedPreferences.edit()
                .putString(key, serializer.serialize(value))
                .apply()
    }

    override fun saveAll(from: Map<out String, V>) {
        sharedPreferences.edit()
                .run {
                    from.forEach {
                        putString(it.key, serializer.serialize(it.value))
                    }
                    apply()
                }
    }

    override fun delete(key: String) {
        sharedPreferences.edit()
                .remove(key)
                .apply()
    }

    override fun deleteAll() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}