package dev.b3nedikt.restring.repository.persistent

import android.content.SharedPreferences
import android.text.Spanned
import dev.b3nedikt.restring.repository.model.StringResource

/**
 * [PersistentMap] which persists string resources to the [SharedPreferences]
 */
internal class StringResourcesPersistentMap(
        sharedPreferences: SharedPreferences
) : ResourcesPersistentMap<CharSequence>(sharedPreferences) {

    override fun fromJson(string: String): CharSequence {
        val stringResource = StringResource.fromJson(string)
        return stringResource.value
    }

    override fun toJson(value: CharSequence): String {
        return StringResource(value, value is Spanned).toJson()
    }
}