package dev.b3nedikt.restring.repository.persistent

import android.content.SharedPreferences
import android.text.Spanned
import dev.b3nedikt.restring.repository.model.StringArray

internal class StringArraysPersistentMap(
        sharedPreferences: SharedPreferences
) : ResourcesPersistentMap<Array<CharSequence>>(sharedPreferences) {

    override fun fromJson(string: String): Array<CharSequence> {
        val resource = StringArray.fromJson(string)
        return resource.value.toTypedArray()
    }

    override fun toJson(value: Array<CharSequence>): String {
        return StringArray(value.toList(), value is Spanned).toJson()
    }
}