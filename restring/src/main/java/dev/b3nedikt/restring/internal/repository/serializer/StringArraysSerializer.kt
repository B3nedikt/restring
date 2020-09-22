package dev.b3nedikt.restring.internal.repository.serializer

import android.text.Spanned
import dev.b3nedikt.restring.internal.repository.model.StringArray
import dev.b3nedikt.restring.internal.repository.persistent.Serializer

/**
 * [Serializer] for string array resources
 */
object StringArraysSerializer : Serializer<Array<CharSequence>, String> {

    override fun serialize(value: Array<CharSequence>): String {
        return StringArray(value.toList(), value is Spanned).toJson()
    }

    override fun deserialize(value: String): Array<CharSequence> {
        val resource = StringArray.fromJson(value)
        return resource.value.toTypedArray()
    }
}