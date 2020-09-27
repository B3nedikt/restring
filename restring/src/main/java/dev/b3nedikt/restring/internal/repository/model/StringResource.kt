package dev.b3nedikt.restring.internal.repository.model

import androidx.core.text.HtmlCompat
import org.json.JSONObject

/**
 * Data class for quantity strings
 *
 * @param value the quantity string
 * @property isText if the [CharSequence]s is a text, meaning it has styling, false
 * if the [CharSequence] implementations is a [String]s.
 */
internal data class StringResource(
        val value: CharSequence,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {
        put(VALUE_KEY, value)

        put(IS_TEXT_KEY, isText)

        toString()
    }

    companion object {

        const val VALUE_KEY = "value"
        const val IS_TEXT_KEY = "isText"

        fun fromJson(jsonString: String) = JSONObject(jsonString).run {
            val string = getString(VALUE_KEY)
            val isText = getBoolean(IS_TEXT_KEY)

            val value = if (isText) {
                HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_COMPACT)
            } else {
                string
            }

            StringResource(value, isText)
        }
    }
}