package dev.b3nedikt.restring.internal.repository.serializer

/**
 * Serializes arbitrary objects of type [T] into objects of type [R], and vice versa for
 * deserialization
 */
internal interface Serializer<T, R> {

    /**
     * Serialize a value of type [T] into a value of type [R]
     */
    fun serialize(value: T): R

    /**
     * Deserialize a value of type [R] into a value of type [T]
     */
    fun deserialize(value: R): T
}