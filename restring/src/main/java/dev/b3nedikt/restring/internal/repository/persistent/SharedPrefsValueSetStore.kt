package dev.b3nedikt.restring.internal.repository.persistent

import android.content.SharedPreferences
import dev.b3nedikt.restring.repository.ValueSetStore
import dev.b3nedikt.restring.internal.repository.serializer.Serializer
import dev.b3nedikt.restring.internal.repository.util.LocaleUtil

/**
 * [ValueSetStore] that stores value sets in the [SharedPreferences]
 *
 * @param sharedPreferences the [SharedPreferences] we store the values in
 * @param serializer the [Serializer] used to serialize & deserialize a single element of the value
 *  set
 * @param stringKey the name of the preference where the value set will be stored to as a string set
 */
internal class SharedPrefsValueSetStore<E>(
        private val sharedPreferences: SharedPreferences,
        private val serializer: Serializer<E, String>,
        private val stringKey: String
) : ValueSetStore<E> {

    override fun find(key: E): E? {
        val localeString = sharedPreferences
                .getStringSet(stringKey, null)
                ?.find { LocaleUtil.fromLanguageTag(it) == key }
                ?: return null

        return serializer.deserialize(localeString)
    }

    override fun findAll(): Collection<E> {
        return sharedPreferences
                .getStringSet(stringKey, null)
                ?.map { serializer.deserialize(it) }
                ?: emptySet()
    }

    override fun save(element: E) {
        val oldSet = findAll()
        val newSet = (oldSet + element)
                .map { serializer.serialize(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet(stringKey, newSet)
                .apply()
    }

    override fun saveAll(elements: Collection<E>) {
        val oldSet = findAll()
        val newSet = (oldSet + elements)
                .map { serializer.serialize(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet(stringKey, newSet)
                .apply()
    }

    override fun delete(element: E) {
        val oldSet = findAll()
        val newSet = (oldSet - element)
                .map { serializer.serialize(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet(stringKey, newSet)
                .apply()
    }

    override fun deleteAll() {
        sharedPreferences.edit().clear().apply()
    }
}