package com.b3nedikt.restring.repository

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.b3nedikt.restring.LocaleUtil
import com.b3nedikt.restring.StringRepository
import org.json.JSONObject
import java.util.*

/**
 * A StringRepository which saves/loads the strings in Shared Preferences.
 *
 * it's not ThreadSafe.
 */
class SharedPrefStringRepository(context: Context,
                                 stringsSharedPrefName: String = STRINGS_SHARED_PREF_NAME,
                                 textsSharedPrefName: String = TEXTS_SHARED_PREF_NAME,
                                 localesSharedPrefName: String = LOCALES_SHARED_PREF_NAME
) : StringRepository {

    private val stringsSharedPreferences by lazy {
        context.getSharedPreferences(stringsSharedPrefName, Context.MODE_PRIVATE)
    }

    private val textsSharedPreferences by lazy {
        context.getSharedPreferences(textsSharedPrefName, Context.MODE_PRIVATE)
    }

    private val localesSharedPreferences by lazy {
        context.getSharedPreferences(localesSharedPrefName, Context.MODE_PRIVATE)
    }

    override var supportedLocales: Set<Locale>
        get() = loadLocales()
        set(value) = saveLocales(value)

    private fun loadLocales() =
            localesSharedPreferences
                    .getStringSet(LOCALES_SHARED_PREF_KEY, null)
                    ?.map { LocaleUtil.fromSimpleLanguageTag(it) }
                    ?.toSet()
                    ?: emptySet()

    private fun saveLocales(locales: Set<Locale>) =
            locales.map { LocaleUtil.toSimpleLanguageTag(it) }
                    .toSet()
                    .run {
                        localesSharedPreferences
                                .edit()
                                .putStringSet(LOCALES_SHARED_PREF_KEY, this)
                                .apply()
                    }

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        saveStringsAndTexts(locale, strings)
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        val result = loadStringsAndTexts().toMutableMap()

        val map = mutableMapOf<String, CharSequence>()
        map[key] = value

        val newStringMap = mutableMapOf<Locale, Map<String, CharSequence>>()
        newStringMap[locale] = map

        result.putAll(newStringMap)

        saveStringsAndTexts(locale, result[locale] ?: emptyMap())
    }

    override fun getString(locale: Locale, key: String) = loadStringsAndTexts()[locale]?.get(key)

    override fun getStrings(locale: Locale): Map<String, CharSequence> {
        val stringsAndTexts = loadStringsAndTexts()
        return stringsAndTexts[locale] ?: emptyMap()
    }

    private fun saveStringsAndTexts(locale: Locale, strings: Map<String, CharSequence>) {
        strings.filterValues { it is String }
                .run { saveStrings(locale, this) }

        strings.filterValues { it !is String }
                .run { saveTexts(locale, this) }
    }

    private fun saveStrings(locale: Locale, strings: Map<String, CharSequence>) {
        val content = serializeKeyValues(strings)
        stringsSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), content)
                .apply()
    }

    private fun saveTexts(locale: Locale, texts: Map<String, CharSequence>) {
        val content = serializeKeyValues(texts)
        textsSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), content)
                .apply()
    }

    private fun loadStringsAndTexts(): Map<Locale, Map<String, CharSequence>> {
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
        return result
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
        private const val LOCALES_SHARED_PREF_NAME = "com.b3nedikt.restring.Restring_Locales"

        private const val LOCALES_SHARED_PREF_KEY = "com.b3nedikt.restring.Restring_Locales_Key"
    }
}
