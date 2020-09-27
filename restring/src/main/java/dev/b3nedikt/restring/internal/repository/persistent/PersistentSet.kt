package dev.b3nedikt.restring.internal.repository.persistent

import dev.b3nedikt.restring.repository.ValueSetStore
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A [MutableSet] which persists all calls to it
 */
internal interface PersistentSet<E>
    : MutableSet<E>, ReadOnlyProperty<Any?, MutableSet<E>>, ValueSetStore<E> {

    override fun add(element: E): Boolean {
        val contained = find(element) != null
        if (contained.not()) save(element)
        return contained
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val contained = findAll().containsAll(elements)
        if (contained.not()) saveAll(elements)
        return contained
    }

    override fun clear() {
        deleteAll()
    }

    override fun iterator(): MutableIterator<E> {
        return findAll().toMutableSet().iterator()
    }

    override fun remove(element: E): Boolean {
        val contained = find(element) != null
        if (contained) delete(element)
        return contained
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val allElements = findAll()
        val contained = allElements.toMutableSet().removeAll(elements)

        if (contained) elements.forEach { delete(it) }

        return contained
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val allElements = findAll()
        val changed = allElements != elements
        deleteAll()
        saveAll(elements)
        return changed
    }

    override val size: Int
        get() = findAll().size

    override fun contains(element: E): Boolean {
        return findAll().contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return findAll().containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return findAll().isEmpty()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableSet<E> {
        return this
    }
}