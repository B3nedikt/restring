package dev.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import dev.b3nedikt.restring.repository.CachedStringRepository
import dev.b3nedikt.restring.repository.MemoryStringRepository
import dev.b3nedikt.restring.repository.SharedPrefStringRepository
import java.util.*


/**
 * A Android library to replace string resources dynamically
 */
object Restring {

    @JvmStatic
    var locale: Locale
        get() = RestringLocale.currentLocale
        set(value) {
            RestringLocale.currentLocale = value
        }

    private var isInitialized = false
    private lateinit var stringRepository: StringRepository

    /**
     * Initialize Restring with the specified configuration.
     *
     * @param context of the application.
     * @param config  of the Restring.
     */
    @JvmOverloads
    @JvmStatic
    fun init(context: Context, config: RestringConfig = RestringConfig.default) {
        if (isInitialized) {
            return
        }

        isInitialized = true
        initStringRepository(context, config)
    }

    /**
     * Wraps context of an activity to provide Restring features.
     *
     * @param base context of an activity.
     * @return the Restring wrapped context.
     */
    @JvmStatic
    fun wrapContext(base: Context): ContextWrapper {
        return RestringContextWrapper.wrap(base, stringRepository)
    }

    /**
     * Set strings of a language.
     *
     * @param locale the strings are for.
     * @param newStrings the strings of the language.
     */
    @JvmStatic
    fun setStrings(locale: Locale, newStrings: Map<String, String>) {
        stringRepository.setStrings(locale, newStrings)
    }

    /**
     * Set a single string for a language.
     *
     * @param locale the string is for.
     * @param key      the string key.
     * @param value    the string value.
     */
    @JvmStatic
    fun setString(locale: Locale, key: String, value: String) {
        stringRepository.setString(locale, key, value)
    }

    private fun initStringRepository(context: Context, config: RestringConfig) {
        stringRepository = config.stringRepository ?: defaultRepository(context)

        if (config.stringsLoader != null) {

            val loaderTask = StringsLoaderTask(config.stringsLoader, stringRepository)
            if (config.loadAsync) {
                loaderTask.runAsync()
            } else {
                loaderTask.runBlocking()
            }
        }
    }

    private fun defaultRepository(context: Context) = CachedStringRepository(
            cacheRepository = MemoryStringRepository(),
            persistentRepository = SharedPrefStringRepository(context)
    )

    /**
     * Loader of strings skeleton. Clients can implement this interface if they want to load
     * strings on initialization. First the list of languages will be asked, then strings of each
     * language.
     */
    interface StringsLoader {

        /**
         * List of supported languages
         */
        val locales: List<Locale>

        /**
         * Get strings of a language as keys &amp; values.
         *
         * @param locale of the strings.
         * @return the strings as (key, value).
         */
        fun getStrings(locale: Locale): Map<String, CharSequence> = emptyMap()

        /** Get quantity strings of a language as keys &amp; values.
         *
         * @param locale of the quantity strings.
         * @return the quantity strings as (key, value).
         */
        fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> = emptyMap()

        /** Get string arrays of a language as keys &amp; values.
         *
         * @param locale of the quantity strings.
         * @return the string arrays as (key, value).
         */
        fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> = emptyMap()
    }
}