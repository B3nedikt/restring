package dev.b3nedikt.restring.internal.repository.serializer

import android.text.Spanned
import dev.b3nedikt.restring.internal.repository.model.StringResource
import dev.b3nedikt.restring.internal.repository.persistent.Serializer

/**
 * [Serializer] for string resources
 */
object StringResourcesSerializer : Serializer<CharSequence, String> {

    override fun serialize(value: CharSequence): String {
        return StringResource(value, value is Spanned).toJson()
    }

    override fun deserialize(value: String): CharSequence {
        val stringResource = StringResource.fromJson(value)
        return stringResource.value

    }
}