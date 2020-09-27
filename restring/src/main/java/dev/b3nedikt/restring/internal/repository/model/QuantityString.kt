package dev.b3nedikt.restring.internal.repository.model

import androidx.core.text.HtmlCompat
import dev.b3nedikt.restring.PluralKeyword
import org.json.JSONObject

/**
 * Data class for quantity strings
 *
 * @param value the quantity string
 * @param isText if at least one of the [CharSequence]s is a text, meaning it has styling, false
 * if all the [CharSequence] implementations are [String]s.
 */
internal data class QuantityString(
        val value: Map<PluralKeyword, CharSequence>,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {

        put(VALUE_KEY, JSONObject().apply {
            value.forEach { put(it.key.name, it.value) }
        })
        put(IS_TEXT_KEY, isText)

        toString()
    }

    companion object {

        const val VALUE_KEY = "value"
        const val IS_TEXT_KEY = "isText"

        fun fromJson(jsonString: String) = JSONObject(jsonString).run {
            val valueJsonObject = getJSONObject(VALUE_KEY)
            val isText = getBoolean(IS_TEXT_KEY)

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
                    value[PluralKeyword.valueOf(name)] = text
                }
            }

            QuantityString(value, isText)
        }
    }
}