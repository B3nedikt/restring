package dev.b3nedikt.restring.internal.repository.persistent

import android.content.SharedPreferences
import android.text.Spanned
import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.internal.repository.model.QuantityString

/**
 * [PersistentMap] which persists quantity strings in the [SharedPreferences]
 */
internal class QuantityStringsResourcesPersistentMap(
        sharedPreferences: SharedPreferences
) : ResourcesPersistentMap<Map<PluralKeyword, CharSequence>>(sharedPreferences) {

    override fun fromJson(string: String): Map<PluralKeyword, CharSequence> {
        val resource = QuantityString.fromJson(string)
        return resource.value
    }

    override fun toJson(value: Map<PluralKeyword, CharSequence>): String {
        return QuantityString(value, value.any { it is Spanned }).toJson()
    }
}