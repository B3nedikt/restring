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

    private val stringsSharedPreferences by lazy {
        context.getSharedPreferences(STRINGS_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val textsSharedPreferences by lazy {
        context.getSharedPreferences(TEXTS_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val memoryStringRepository = MemoryStringRepository()

    init {
        val result = mutableMapOf<Locale, Map<String, CharSequence>>()

        val strings = loadStrings()
        val texts = loadTexts()
        val locales: Set<Locale> = strings.keys.union(texts.keys)

        locales.forEach {
            val stringsForLocale = strings[it]
            val textsForLocale = texts[it]

            val combined = when {
                stringsForLocale != null && textsForLocale != null -> stringsForLocale + textsForLocale
                else -> stringsForLocale ?: textsForLocale
            }
            combined?.run { result[it] = combined }
        }

        result.forEach { memoryStringRepository.setStrings(it.key, it.value) }
    }

    override var supportedLocales: Set<Locale> = setOf()

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        memoryStringRepository.setStrings(locale, strings)
        saveStringsAndTexts(locale, strings)
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        memoryStringRepository.setString(locale, key, value)

        val keyValues = memoryStringRepository.getStrings(locale).toMutableMap()
        keyValues[key] = value
        saveStringsAndTexts(locale, keyValues)
    }

    private fun saveStringsAndTexts(locale: Locale, strings: Map<String, CharSequence>) {
        strings.filterValues { it is String }
                .run { saveStrings(locale, this) }

        strings.filterValues { it !is String }
                .run { saveTexts(locale, this) }
    }

    override fun getString(locale: Locale, key: String): CharSequence? {
        return memoryStringRepository.getString(locale, key)
    }

    override fun getStrings(locale: Locale): Map<String, CharSequence> {
        return memoryStringRepository.getStrings(locale)
    }

    private fun loadStrings(): MutableMap<Locale, Map<String, String>> {
        val result = mutableMapOf<Locale, Map<String, String>>()

        val strings = stringsSharedPreferences.all
        for ((locale, value) in strings) {
            if (value !is String) {
                continue
            }

            val keyValues = deserializeStringsKeyValues(value)
            result[LocaleUtil.fromSimpleLanguageTag(locale)] = keyValues
        }
        return result
    }

    private fun loadTexts(): MutableMap<Locale, Map<String, CharSequence>> {
        val result = mutableMapOf<Locale, Map<String, CharSequence>>()

        val strings = textsSharedPreferences.all
        for ((locale, value) in strings) {
            if (value !is String) {
                continue
            }

            val keyValues = deserializeTextsKeyValues(value)
            result[LocaleUtil.fromSimpleLanguageTag(locale)] = keyValues
        }
        return result
    }

    private fun saveStrings(locale: Locale, strings: Map<String, CharSequence>) {
        val content = serializeKeyValues(strings)
        stringsSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), content)
                .apply()
    }

    private fun saveTexts(locale: Locale, strings: Map<String, CharSequence>) {
        val content = serializeKeyValues(strings)
        textsSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), content)
                .apply()
    }

    private fun deserializeStringsKeyValues(content: String) =
            JSONObject(content).run {
                keys().asSequence()
                        .map { key -> key to getString(key) }
                        .toMap()
            }

    private fun deserializeTextsKeyValues(content: String) =
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
        if (value is Spanned) {
            return HtmlCompat.toHtml(value, HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        }
        return value.toString()
    }

    private companion object {
        private const val STRINGS_SHARED_PREF_NAME = "com.b3nedikt.restring.Restring_Strings"
        private const val TEXTS_SHARED_PREF_NAME = "com.b3nedikt.restring.Restring_Texts"
    }

}
