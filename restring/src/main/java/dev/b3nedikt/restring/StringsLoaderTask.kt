package dev.b3nedikt.restring

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
    : AsyncTask<Void, Void, Map<Locale, Map<String, Any>>>() {

    fun runAsync() {
        executeOnExecutor(THREAD_POOL_EXECUTOR)
    }

    fun runBlocking() {
        saveInRepository(execute().get())
    }

    override fun doInBackground(vararg voids: Void): Map<Locale, Map<String, Any>> {
        return loadStrings()
    }

    override fun onPostExecute(langStrings: Map<Locale, Map<String, Any>>) {
        saveInRepository(langStrings)
    }

    private fun loadStrings(): MutableMap<Locale, Map<String, Any>> {
        val localizedStrings = mutableMapOf<Locale, Map<String, Any>>()

        val locales = stringsLoader.locales

        stringRepository.supportedLocales = locales.toSet()

        locales.forEach {
            val keyValues = stringsLoader.getStrings(it)
                    .plus(stringsLoader.getQuantityStrings(it))
                    .plus(stringsLoader.getStringArrays(it))

            if (keyValues.isNotEmpty()) {
                localizedStrings[it] = keyValues
            }
        }

        return localizedStrings
    }

    // I know, will be improved in the next version ;)
    @Suppress("UNCHECKED_CAST")
    private fun saveInRepository(strings: Map<Locale, Map<String, Any>>) {
        for ((key, value) in strings) {
            runCatching {
                stringRepository.setStrings(key, value as Map<String, CharSequence>)
            }.onFailure {
                runCatching {
                    stringRepository.setStringArrays(key, value as Map<String, Array<CharSequence>>)
                }.onFailure {
                    runCatching {
                        stringRepository.setQuantityStrings(key, value as Map<String, Map<PluralKeyword, CharSequence>>)
                    }
                }
            }
        }
    }
}