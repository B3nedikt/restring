package com.b3nedikt.restring

import android.content.Context
import android.content.SharedPreferences
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

    private lateinit var sharedPreferences: SharedPreferences
    private val memoryStringRepository = MemoryStringRepository()

    init {
        initSharedPreferences(context)
        loadStrings()
    }

    override var supportedLocales: Set<Locale> = setOf()

    override fun setStrings(locale: Locale, strings: Map<String, String>) {
        memoryStringRepository.setStrings(locale, strings)
        saveStrings(locale, strings)
    }

    override fun setString(locale: Locale, key: String, value: String) {
        memoryStringRepository.setString(locale, key, value)

        val keyValues = memoryStringRepository.getStrings(locale).toMutableMap()
        keyValues[key] = value
        saveStrings(locale, keyValues)
    }

    override fun getString(locale: Locale, key: String): String? {
        return memoryStringRepository.getString(locale, key)
    }

    override fun getStrings(locale: Locale): Map<String, String> {
        return memoryStringRepository.getStrings(locale)
    }

    private fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun loadStrings() {
        val strings = sharedPreferences.all
        for ((locale, value) in strings) {
            if (value !is String) {
                continue
            }

            val keyValues = deserializeKeyValues(value)
            memoryStringRepository.setStrings(localeFromString(locale), keyValues)
        }
    }

    private fun saveStrings(locale: Locale, strings: Map<String, String>) {
        val content = serializeKeyValues(strings)
        sharedPreferences.edit()
                .putString(localeToString(locale), content)
                .apply()
    }

    private fun deserializeKeyValues(content: String): Map<String, String> {
        val keyValues = mutableMapOf<String, String>()

        val jsonObject = JSONObject(content)
        jsonObject.keys()
                .forEach { key -> keyValues[key] = jsonObject.getString(key) }
        return keyValues
    }

    private fun serializeKeyValues(keyValues: Map<String, String>): String {
        return JSONObject(keyValues).toString()
    }

    private fun localeToString(locale: Locale): String {
        return locale.language + "-" + locale.country
    }

    private fun localeFromString(locale: String): Locale {
        val language = locale.split("-")[0]
        val country = locale.split("-")[1]

        return Locale(language, country)
    }

    private companion object {
        private const val SHARED_PREF_NAME = "Restrings3"
    }
}
