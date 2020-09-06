package dev.b3nedikt.restring.repository

import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.StringRepository
import dev.b3nedikt.restring.repository.observable.observableMap
import java.util.*

class CachedStringRepository(val persistentRepository: StringRepository) : StringRepository {

    override val supportedLocales: Set<Locale> get() = _supportedLocales
    private val _supportedLocales: MutableSet<Locale> = persistentRepository.supportedLocales.toMutableSet()

    override val strings: MutableMap<Locale, MutableMap<String, CharSequence>> by observableMap(
            initialValue = persistentRepository.strings,
            defaultValue = getDefaultStringsMap(persistentRepository.strings),
            afterPut = { key, value ->
                persistentRepository.strings[key] = value

                _supportedLocales.add(key)
            }
    )

    override val quantityStrings: MutableMap<Locale, MutableMap<String, Map<PluralKeyword, CharSequence>>> by observableMap(
            initialValue = persistentRepository.quantityStrings,
            defaultValue = getDefaultStringsMap(persistentRepository.quantityStrings),
            afterPut = { key, value ->
                persistentRepository.quantityStrings[key] = value

                _supportedLocales.add(key)
            }
    )

    override val stringArrays: MutableMap<Locale, MutableMap<String, Array<CharSequence>>> by observableMap(
            initialValue = persistentRepository.stringArrays,
            defaultValue = getDefaultStringsMap(persistentRepository.stringArrays),
            afterPut = { key, value ->
                persistentRepository.stringArrays[key] = value

                _supportedLocales.add(key)
            }
    )

    private fun <T> getDefaultStringsMap(
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
