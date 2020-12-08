package dev.b3nedikt.restring.internal.repository.persistent

import java.util.*

/**
 * A [PersistentMap] which used [Locale]s as key and maps of resource name, resource value pairs
 * as its values.
 */
internal class LocalizedResourcesPersistentMap<V>(
        val locales: MutableSet<Locale>,
        val persistentMapFactory: (locale: Locale) -> ResourcesPersistentMap<V>
) : PersistentMap<Locale, MutableMap<String, V>> {

    private val delegateMaps: MutableMap<Locale, MutableMap<String, V>> by lazy(::mutableMapOf)

    override val size: Int
        get() = locales.size

    override fun find(key: Locale): MutableMap<String, V>? {
        if (delegateMaps[key] == null) {
            locales.add(key)
            delegateMaps[key] = persistentMapFactory.invoke(key)
        }

        return delegateMaps[key]
    }

    override fun findAll(): Map<out Locale, MutableMap<String, V>> {
        return delegateMaps
    }

    override fun save(key: Locale, value: MutableMap<String, V>) {
        if (delegateMaps.containsKey(key)) return

        delegateMaps[key] = persistentMapFactory.invoke(key)
    }

    override fun saveAll(from: Map<out Locale, MutableMap<String, V>>) {
        from.forEach { save(it.key, it.value) }
    }

    override fun delete(key: Locale) {
        delegateMaps.remove(key)
    }

    override fun deleteAll() {
        delegateMaps.clear()
    }
}