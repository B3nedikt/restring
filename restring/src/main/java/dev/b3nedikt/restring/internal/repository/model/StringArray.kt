package dev.b3nedikt.restring.internal.repository.model

import androidx.core.text.HtmlCompat
import org.json.JSONArray
import org.json.JSONObject

/**
 * Data class for string arrays
 *
 * @param value the string array as a list
 * @param isText f at least one of the [CharSequence]s is a text, meaning it has styling, false
 * if all the [CharSequence] implementations are [String]s.
 */
internal data class StringArray(
        val value: List<CharSequence>,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {
        val stringArray = if (isText) value.map { text -> text.toString() } else value

        put(VALUE_KEY, JSONArray(stringArray))

        put(IS_TEXT_KEY, isText)

        toString()
    }

    companion object {

        const val VALUE_KEY = "value"
        const val IS_TEXT_KEY = "isText"

        fun fromJson(jsonString: String) = JSONObject(jsonString).run {
            val valueJsonArray = getJSONArray(VALUE_KEY)
            val isText = getBoolean(IS_TEXT_KEY)

            val value = mutableListOf<CharSequence>()

            for (i in 0 until valueJsonArray.length()) {
                val string = valueJsonArray.getString(i)

                val text = if (isText) {
                    HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_COMPACT)
                } else {
                    string
                }
                value.add(text)
            }

            StringArray(value, isText)
        }
    }
}