package dev.b3nedikt.restring.repository.model

import androidx.core.text.HtmlCompat
import org.json.JSONArray
import org.json.JSONObject

internal data class StringArray(
        val value: List<CharSequence>,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {
        put(VALUE_KEY, JSONArray(value))

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
                val string = valueJsonArray.getString(0)

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