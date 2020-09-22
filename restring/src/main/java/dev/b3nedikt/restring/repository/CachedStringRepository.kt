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
    private val _supportedLocales: MutableSet<Locale> = persistentRepository.supportedLocales.toMutableSet()

    override val strings by observableResourcesMap(persistentRepository.strings)

    override val quantityStrings by observableResourcesMap(persistentRepository.quantityStrings)

    override val stringArrays by observableResourcesMap(persistentRepository.stringArrays)

    private fun <R> observableResourcesMap(
            persistentMap: MutableMap<Locale, MutableMap<String, R>>
    ): ReadWriteProperty<Any?, MutableMap<Locale, MutableMap<String, R>>> {
        return observableMap(
                initialValue = persistentMap,
                defaultValue = getDefaultResourcesMap(persistentMap),
                afterPut = { key, value ->
                    persistentMap[key] = value

                    _supportedLocales.add(key)
                }
        )
    }

    private fun <T> getDefaultResourcesMap(
            delegateMap: MutableMap<Locale, MutableMap<String, T>>
    ): (key: Locale) -> MutableMap<String, T> {
        return { locale ->
            val map: MutableMap<String, T> by observableMap(
                    initialValue = mutableMapOf(),
                    afterPut = { key, value ->
                        delegateMap[locale]?.put(key, value)
                    },
                    afterPutAll = { map ->
                        delegateMap[locale]?.putAll(map)
                    },
                    afterRemove = { key ->
                        delegateMap[locale]?.remove(key)
                    },
                    afterClear = {
                        delegateMap[locale]?.clear()
                    }
            )
            map
        }
    }
}
