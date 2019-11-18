package com.b3nedikt.restring.repository

import androidx.core.text.HtmlCompat
import com.b3nedikt.restring.PluralKeyword
import org.json.JSONObject
import java.util.*

internal data class QuantityString(
        val value: Map<PluralKeyword, CharSequence>,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {
        put("value", JSONObject().apply {
            value.forEach { put(it.key.stringValue, it.value) }
        })
        put("isText", isText)

        toString()
    }

    companion object {
        fun fromJson(jsonString: String) = JSONObject(jsonString).run {
            val valueJsonObject = getJSONObject("value")
            val isText = getBoolean("isText")

            val value = mutableMapOf<PluralKeyword, CharSequence>()
            val names = valueJsonObject.names()

            if (names != null) {
                for (i in 0 until names.length()) {
                    val name = names.getString(i)
                    val string = valueJsonObject.getString(name)

                    val text = if (isText) {
                        HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    } else {
                        string
                    }
                    value[PluralKeyword.valueOf(name.toUpperCase(Locale.ROOT))] = text
                }
            }

            QuantityString(value, isText)
        }
    }
}