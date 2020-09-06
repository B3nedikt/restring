package dev.b3nedikt.restring

import android.content.Context
import dev.b3nedikt.restring.repository.CachedStringRepository
import dev.b3nedikt.restring.repository.PersistentStringRepository
import java.util.*


/**
 * A Android library to replace string resources dynamically
 */
object Restring {

    private var isInitialized = false
    private lateinit var stringRepository: StringRepository

    /**
     * Map of string ids to string names. These strings are only managed by restring,
     * meaning their id is not in R.string, instead their id has been assigned by restring.
     */
    internal val managedStrings = mutableMapOf<Int, String>()

    /**
     * The [Locale] currently used by restring, this defaults to [Locale.getDefault].
     * If needed the [Locale] used by restring can be changed anytime.
     */
    @JvmStatic
    var locale: Locale
        get() = localeProvider.currentLocale
        set(value) {
            localeProvider.currentLocale = value
        }

    /**
     * The [LocaleProvider] defines the way restring determines its [locale], this
     * defaults to the [DefaultLocaleProvider].
     */
    @JvmStatic
    var localeProvider: LocaleProvider = DefaultLocaleProvider

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

        config.localeProvider?.let { localeProvider = it }

        initStringRepository(context, config)
    }

    /**
     * Wraps context of an activity to provide Restring features.
     * Will return the context as it was passed in, if it is already wrapped.
     *
     * @param base context of an activity.
     * @return the wrapped context.
     */
    @JvmStatic
    fun wrapContext(base: Context): Context {
        if (base.resources is RestringResources) return base
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
        stringRepository.strings[locale]?.putAll(newStrings)
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
        stringRepository.strings[locale]?.put(key, value)
    }

    private fun initStringRepository(context: Context, config: RestringConfig) {
        stringRepository = config.stringRepository ?: defaultRepository(context)

        config.stringsLoader?.let {
            StringsLoaderTask(it, stringRepository).run {
                if (config.loadAsync) runAsync() else runBlocking()
            }
        }
    }

    private fun defaultRepository(context: Context) = CachedStringRepository(
            persistentRepository = PersistentStringRepository(context)
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
        @JvmDefault
        fun getStrings(locale: Locale): Map<String, CharSequence> = emptyMap()

        /** Get quantity strings of a language as keys &amp; values.
         *
         * @param locale of the quantity strings.
         * @return the quantity strings as (key, value).
         */
        @JvmDefault
        fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> = emptyMap()

        /** Get string arrays of a language as keys &amp; values.
         *
         * @param locale of the quantity strings.
         * @return the string arrays as (key, value).
         */
        @JvmDefault
        fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> = emptyMap()
    }
}