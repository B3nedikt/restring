package com.b3nedikt.restring.example

import com.b3nedikt.restring.Restring
import java.util.*

/**
 * This is just a really simple sample of strings loader.
 * in real applications, you might call an API to get your strings.
 *
 *
 * All overridden methods will be called on background thread.
 */
class SampleStringsLoader : Restring.StringsLoader {

    override val languages: List<String>
        get() = listOf("en", "de", "fa")

    override fun getStrings(language: String): Map<String, String> {
        val map = HashMap<String, String>()
        when (language) {
            "en" -> {
                map["title"] = "This is title (from restring)."
                map["subtitle"] = "This is subtitle (from restring)."
            }
            "de" -> {
                map["title"] = "Das ist Titel (from restring)."
                map["subtitle"] = "Das ist Untertitel (from restring)."
            }
            "fa" -> {
                map["title"] = "In sarkhat ast (from restring)."
                map["subtitle"] = "In matn ast (from restring)."
            }
        }
        return map
    }
}