package dev.b3nedikt.restring.repository.serializer

import android.text.Spanned
import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.repository.model.QuantityString

/**
 * [Serializer] for quantity string resources
 */
internal object QuantityStringsSerializer : Serializer<Map<PluralKeyword, CharSequence>, String> {

    override fun serialize(value: Map<PluralKeyword, CharSequence>): String {
        return QuantityString(value, value.any { it is Spanned }).toJson()
    }

    override fun deserialize(value: String): Map<PluralKeyword, CharSequence> {
        val resource = QuantityString.fromJson(value)
        return resource.value
    }
}