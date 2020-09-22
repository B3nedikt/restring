package dev.b3nedikt.restring.repository

/**
 * Stores sets of elements of type [E]
 */
interface ValueSetStore<E> {

    /**
     * Returns a element if it exists
     */
    fun find(key: E): E?

    /**
     * Returns all elements managed by this set
     */
    fun findAll(): Collection<E>

    /**
     * Persists the [element]
     */
    fun save(element: E)

    /**
     * Persists all [elements]
     */
    fun saveAll(elements: Collection<E>)

    /**
     * Deletes the [element]
     */
    fun delete(element: E)

    /**
     * Deletes all elements managed by this set
     */
    fun deleteAll()
}