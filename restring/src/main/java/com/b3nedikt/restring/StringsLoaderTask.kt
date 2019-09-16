package com.b3nedikt.restring

import android.os.AsyncTask
import java.util.*

/**
 * Try to load all strings for different languages by a StringsLoader.
 * All string loads happen on background thread, and saving into repository happens on main thread.
 *
 *
 * FIRST it retrieves all supported languages,
 * THEN it retrieves all strings(key, value) for each language.
 */
internal class StringsLoaderTask(private val stringsLoader: Restring.StringsLoader,
                                 private val stringRepository: StringRepository)
    : AsyncTask<Void, Void, Map<Locale, Map<String, String>>>() {

    fun run() {
        executeOnExecutor(THREAD_POOL_EXECUTOR)
    }

    override fun doInBackground(vararg voids: Void): Map<Locale, Map<String, String>> {
        val localizedStrings = mutableMapOf<Locale, Map<String, String>>()

        val languages = stringsLoader.locales

        stringRepository.supportedLocales = languages.toSet()

        for (lang in languages) {
            val keyValues = stringsLoader.getStrings(lang)
            if (keyValues.isNotEmpty()) {
                localizedStrings[lang] = keyValues
            }
        }

        return localizedStrings
    }

    override fun onPostExecute(langStrings: Map<Locale, Map<String, String>>) {
        for ((key, value) in langStrings) {
            stringRepository.setStrings(key, value)
        }
    }
}