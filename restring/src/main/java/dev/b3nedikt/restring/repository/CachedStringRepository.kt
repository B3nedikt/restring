package dev.b3nedikt.restring.repository

import dev.b3nedikt.restring.MutableStringRepository
import dev.b3nedikt.restring.internal.repository.observable.observableMap
import java.util.*
import kotlin.properties.ReadWriteProperty

/**
 * Implementation of a [MutableStringRepository] that caches strings in memory, while persisting
 * writes to the [persistentRepository]
 */
class CachedStringRepository(
        val persistentRepository: MutableStringRepository
) : MutableStringRepository {

    override val supportedLocales: Set<Locale> get() = _supportedLocales
    private val _supportedLocales: MutableSet<Locale> =
            persistentRepository.supportedLocales.toMutableSet()

    override val strings by observableResourcesMap(persistentRepository.strings)

    override val quantityStrings by observableResourcesMap(persistentRepository.quantityStrings)

    override val stringArrays by observableResourcesMap(persistentRepository.stringArrays)

    private fun <R> observableResourcesMap(
            persistentMap: MutableMap<Locale, MutableMap<String, R>>
    ): ReadWriteProperty<Any?, MutableMap<Locale, MutableMap<String, R>>> {
        return observableMap(
                defaultValue = getDefaultResourcesMap(persistentMap),
                afterPut = { key, value ->
                    persistentMap[key] = value

                    _supportedLocales.add(key)
                }
        )
    }

    private fun <T> getDefaultResourcesMap(
            persistentMap: MutableMap<Locale, MutableMap<String, T>>
    ): (key: Locale) -> MutableMap<String, T> {
        return { locale ->
            val map: MutableMap<String, T> by observableMap(
                    initialValue = persistentMap[locale],
                    afterPut = { key, value ->
                        persistentMap[locale]?.put(key, value)
                    },
                    afterPutAll = { map ->
                        persistentMap[locale]?.putAll(map)
                    },
                    afterRemove = { key ->
                        persistentMap[locale]?.remove(key)
                    },
                    afterClear = { persistentMap[locale]?.clear() }
            )
            map
        }
    }
}
