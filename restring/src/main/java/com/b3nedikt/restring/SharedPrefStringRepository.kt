package com.b3nedikt.restring

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import org.json.JSONObject
import java.util.*

/**
 * A StringRepository which saves/loads the strings in Shared Preferences.
 * it also keeps the strings in memory by using MemoryStringRepository internally for faster access.
 *
 *
 * it's not ThreadSafe.
 */
internal class SharedPrefStringRepository(context: Context) : StringRepository {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val memoryStringRepository = MemoryStringRepository()

    init {
        loadStrings()
    }

    override var supportedLocales: Set<Locale> = setOf()

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        memoryStringRepository.setStrings(locale, strings)
        saveStrings(locale, strings)
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        memoryStringRepository.setString(locale, key, value)

        val keyValues = memoryStringRepository.getStrings(locale).toMutableMap()
        keyValues[key] = value
        saveStrings(locale, keyValues)
    }

    override fun getString(locale: Locale, key: String): CharSequence? {
        return memoryStringRepository.getString(locale, key)
    }

    override fun getStrings(locale: Locale): Map<String, CharSequence> {
        return memoryStringRepository.getStrings(locale)
    }

    private fun loadStrings() {
        val strings = sharedPreferences.all
        for ((locale, value) in strings) {
            if (value !is String) {
                continue
            }

            val keyValues = deserializeKeyValues(value)
            memoryStringRepository.setStrings(LocaleUtil.fromSimpleLanguageTag(locale), keyValues)
        }
    }

    private fun saveStrings(locale: Locale, strings: Map<String, CharSequence>) {
        val content = serializeKeyValues(strings)
        sharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), content)
                .apply()
    }

    private fun deserializeKeyValues(content: String) =
            JSONObject(content).run {
                keys().asSequence()
                        .map { key -> key to HtmlCompat.fromHtml(getString(key), HtmlCompat.FROM_HTML_MODE_COMPACT) }
                        .toMap()
            }

    private fun serializeKeyValues(keyValues: Map<String, CharSequence>): String {
        val stringsMap = keyValues.map { it.key to serializeCharSequence(it.value) }.toMap()
        return JSONObject(stringsMap).toString()
    }

    private fun serializeCharSequence(value: CharSequence): String {
        if(value is Spanned){
            return HtmlCompat.toHtml(value, HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        }
        return value.toString()
    }

    internal const val SHARED_PREF_NAME = "Restrings3"
}
