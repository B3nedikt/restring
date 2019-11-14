package com.b3nedikt.restring

import android.os.AsyncTask
import com.b3nedikt.restring.repository.CachedStringRepository
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
    : AsyncTask<Void, Void, Map<Locale, Map<String, CharSequence>>>() {

    fun runAsync() {
        executeOnExecutor(THREAD_POOL_EXECUTOR)
    }

    fun runBlocking() {
        saveInRepository(execute().get())
    }

    override fun doInBackground(vararg voids: Void): Map<Locale, Map<String, CharSequence>> {
        return loadStrings()
    }

    override fun onPostExecute(langStrings: Map<Locale, Map<String, CharSequence>>) {
        saveInRepository(langStrings)
    }

    private fun loadStrings(): MutableMap<Locale, Map<String, CharSequence>> {
        val localizedStrings = mutableMapOf<Locale, Map<String, CharSequence>>()

        val languages = stringsLoader.locales

        stringRepository.supportedLocales = languages.toSet()

        for (lang in languages) {
            val keyValues = stringsLoader.getStrings(lang)

            if (keyValues.isNotEmpty()) {
                localizedStrings[lang] = keyValues
            }else{
                if(stringRepository is CachedStringRepository)
                {
                    val keyValues=stringRepository.persistentRepository.getStrings(lang)
                    if(keyValues.isNotEmpty())
                        localizedStrings[lang] = keyValues
                }

            }
        }
        return localizedStrings
    }

    private fun saveInRepository(langStrings: Map<Locale, Map<String, CharSequence>>) {
        for ((key, value) in langStrings) {
            stringRepository.setStrings(key, value)
        }
    }
}