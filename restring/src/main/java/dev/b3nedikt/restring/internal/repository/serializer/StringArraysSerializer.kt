package dev.b3nedikt.restring.internal.repository.serializer

import android.text.Spanned
import dev.b3nedikt.restring.internal.repository.model.StringArray

/**
 * [Serializer] for string array resources
 */
internal object StringArraysSerializer : Serializer<Array<CharSequence>, String> {

    override fun serialize(value: Array<CharSequence>): String {
        return StringArray(value.toList(), value.any { it is Spanned }).toJson()
    }

    override fun deserialize(value: String): Array<CharSequence> {
        val resource = StringArray.fromJson(value)
        return resource.value.toTypedArray()
    }
}