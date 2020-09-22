package dev.b3nedikt.restring.internal.repository.persistent

interface Serializer<T, R> {

    fun serialize(value: T): R

    fun deserialize(value: R): T
}