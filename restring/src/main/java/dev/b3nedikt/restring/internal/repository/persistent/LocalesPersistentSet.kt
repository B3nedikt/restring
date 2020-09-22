package dev.b3nedikt.restring.internal.repository.persistent

import dev.b3nedikt.restring.repository.ValueSetStore
import java.util.*

/**
 * A [PersistentSet] which persists [Locale]s in a [ValueSetStore]
 */
internal class LocalesPersistentSet(
        private val valueSetStore: ValueSetStore<Locale>
) : PersistentSet<Locale> {

    override fun find(key: Locale): Locale? {
        return valueSetStore.find(key)
    }

    override fun findAll(): Collection<Locale> {
        return valueSetStore.findAll()
    }

    override fun save(element: Locale) {
        valueSetStore.save(element)
    }

    override fun saveAll(elements: Collection<Locale>) {
        valueSetStore.saveAll(elements)
    }

    override fun delete(element: Locale) {
        valueSetStore.delete(element)
    }

    override fun deleteAll() {
        valueSetStore.deleteAll()
    }
}