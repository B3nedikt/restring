package com.b3nedikt.restring.repository

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.b3nedikt.restring.LocaleUtil
import com.b3nedikt.restring.PluralKeyword
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
                                 localesSharedPrefName: String = LOCALES_SHARED_PREF_NAME,
                                 quantityStringsSharedPrefName: String = QUANTITY_STRINGS_SHARED_PREF_NAME,
                                 stringArraysSharedPrefName: String = STRING_ARRAYS_SHARED_PREF_NAME
) : StringRepository {

    private val stringsSharedPreferences by lazy {
        context.getSharedPreferences(stringsSharedPrefName, Context.MODE_PRIVATE)
    }

    private val textsSharedPreferences by lazy {
        context.getSharedPreferences(textsSharedPrefName, Context.MODE_PRIVATE)
    }

    private val quantityStringsSharedPreferences by lazy {
        context.getSharedPreferences(quantityStringsSharedPrefName, Context.MODE_PRIVATE)
    }

    private val stringArraysSharedPreferences by lazy {
        context.getSharedPreferences(stringArraysSharedPrefName, Context.MODE_PRIVATE)
    }

    private val localesSharedPreferences by lazy {
        context.getSharedPreferences(localesSharedPrefName, Context.MODE_PRIVATE)
    }

    override var supportedLocales: Set<Locale>
        get() = loadLocales()
        set(value) = saveLocales(value)

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        saveStringsAndTexts(locale, getStrings(locale).plus(strings))
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        setStrings(locale, mapOf(key to value))
    }

    override fun getString(locale: Locale, key: String) = getStrings(locale)[key]

    override fun getStrings(locale: Locale): Map<String, CharSequence> {
        val stringsAndTexts = loadStringsAndTexts()
        return stringsAndTexts[locale] ?: emptyMap()
    }

    override fun getQuantityString(locale: Locale, key: String) = getQuantityStrings(locale)[key]

    override fun setQuantityString(locale: Locale, key: String, quantityString: Map<PluralKeyword, CharSequence>) {
        setQuantityStrings(locale, mapOf(key to quantityString))
    }

    override fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> {
        val jsonString = quantityStringsSharedPreferences
                .getString(LocaleUtil.toSimpleLanguageTag(locale), null)
                ?: return emptyMap()

        return JSONObject(jsonString).run {
            keys().asSequence()
                    .map { key -> key to QuantityString.fromJson(getString(key)).value }
                    .toMap()
        }
    }

    override fun setQuantityStrings(locale: Locale, quantityStrings: Map<String, Map<PluralKeyword, CharSequence>>) {
        val combinedQuantityStrings = getQuantityStrings(locale).plus(quantityStrings)

        val jsonString = JSONObject(
            combinedQuantityStrings
                    .map { it.key to QuantityString(it.value, it.value.entries.first().value !is String).toJson() }
                    .toMap()
        ).toString()

        quantityStringsSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), jsonString)
                .apply()
    }

    override fun getStringArray(locale: Locale, key: String) = getStringArrays(locale)[key]

    override fun setStringArray(locale: Locale, key: String, stringArray: Array<CharSequence>) {
        setStringArrays(locale, mapOf(key to stringArray))
    }

    override fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> {
        val jsonString = stringArraysSharedPreferences
                .getString(LocaleUtil.toSimpleLanguageTag(locale), null)
                ?: return emptyMap()

        return JSONObject(jsonString).run {
            keys().asSequence()
                    .map { key -> key to StringArray.fromJson(getString(key)).value.toTypedArray() }
                    .toMap()
        }
    }

    override fun setStringArrays(locale: Locale, stringArrays: Map<String, Array<CharSequence>>) {
        val combinedStringArrays = getStringArrays(locale).plus(stringArrays)

        val jsonString = JSONObject(
                combinedStringArrays
                        .map { it.key to StringArray(it.value.asList(), it.value.first() !is String).toJson() }
                        .toMap()
        ).toString()

        stringArraysSharedPreferences.edit()
                .putString(LocaleUtil.toSimpleLanguageTag(locale), jsonString)
                .apply()
    }

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
        private const val QUANTITY_STRINGS_SHARED_PREF_NAME = "com.b3nedikt.restring.Restring_Quantity_Strings"
        private const val STRING_ARRAYS_SHARED_PREF_NAME = "com.b3nedikt.restring.Restring_String_Arrays"

        private const val LOCALES_SHARED_PREF_KEY = "com.b3nedikt.restring.Restring_Locales_Key"
    }
}
