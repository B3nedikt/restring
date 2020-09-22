package dev.b3nedikt.restring.internal.repository.serializer

import dev.b3nedikt.restring.internal.repository.persistent.Serializer
import dev.b3nedikt.restring.internal.repository.util.LocaleUtil
import java.util.*

/**
 * [Serializer] for [Locale]s
 */
object LocaleSerializer : Serializer<Locale, String> {

    override fun serialize(value: Locale): String {
        return LocaleUtil.toLanguageTag(value)
    }

    override fun deserialize(value: String): Locale {
        return LocaleUtil.fromLanguageTag(value)
    }
}