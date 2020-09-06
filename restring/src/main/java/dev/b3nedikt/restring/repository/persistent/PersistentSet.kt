package dev.b3nedikt.restring.repository.persistent

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal abstract class PersistentSet<E> : MutableSet<E>, ReadOnlyProperty<Any?, MutableSet<E>> {

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

    abstract fun find(key: E): E?

    abstract fun findAll(): Collection<E>

    abstract fun save(element: E)

    abstract fun saveAll(elements: Collection<E>)

    abstract fun delete(element: E)

    abstract fun deleteAll()
}