package dev.b3nedikt.restring.repository.model

import android.text.Spannable
import androidx.core.text.HtmlCompat
import org.json.JSONObject

/**
 * Data class for quantity strings
 *
 * @param value the quantity string
 * @property isText if the value [CharSequence] is a text, meaning it is HTML formatted and can be
 * used by a [Spannable].
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