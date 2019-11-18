package com.b3nedikt.restring.repository

import androidx.core.text.HtmlCompat
import org.json.JSONArray
import org.json.JSONObject

internal data class StringArray(
        val value: List<CharSequence>,
        val isText: Boolean
) {

    fun toJson() = JSONObject().run {
        put("value", JSONArray(value))

        put("isText", isText)

        toString()
    }

    companion object {
        fun fromJson(jsonString: String) = JSONObject(jsonString).run {
            val valueJsonArray = getJSONArray("value")
            val isText = getBoolean("isText")

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