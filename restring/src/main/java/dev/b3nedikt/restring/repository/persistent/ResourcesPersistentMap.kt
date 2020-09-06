package dev.b3nedikt.restring.repository.persistent

import android.content.SharedPreferences

internal abstract class ResourcesPersistentMap<T>(
        private val sharedPreferences: SharedPreferences
) : PersistentMap<String, T>() {

    override fun find(key: String): T? {
        val value = sharedPreferences.getString(key, null) ?: return null
        return fromJson(value)
    }

    override fun findAll(): Map<out String, T> {
        return sharedPreferences.all.mapValues { fromJson(it.value as String) }
    }

    override fun save(key: String, value: T) {
        sharedPreferences.edit()
                .putString(key, toJson(value))
                .apply()
    }

    override fun saveAll(from: Map<out String, T>) {
        sharedPreferences.edit()
                .run {
                    from.forEach {
                        putString(it.key, toJson(it.value))
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

    abstract fun fromJson(string: String): T

    abstract fun toJson(value: T): String
}